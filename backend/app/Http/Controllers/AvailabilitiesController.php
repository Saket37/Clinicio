<?php

namespace App\Http\Controllers;

use App\Models\APIResponse;
use App\Models\Availability;
use App\Models\Enum;
use App\Models\Slot;
use Illuminate\Contracts\Foundation\Application;
use Illuminate\Contracts\Routing\ResponseFactory;
use Illuminate\Http\Request;
use Illuminate\Http\Response;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Validator;
use Illuminate\Validation\Rule;
use PHPUnit\Exception;

class AvailabilitiesController extends Controller
{
    /**
     * Create Availability function for doctors
     * return HTTP Status code 201 when availability is created successfully
     * return HTTP Status code 422 when validation fails
     * @param Request $request
     * @param int $id = doctor id
     * @return Response|Application|ResponseFactory
     */
    function createAvailability(Request $request, int $id): Response|Application|ResponseFactory
    {
        DB::beginTransaction();
        try {
            if ($request->user()->id !== $id) {
                return APIResponse::Error('UnAuthorized', status: 403);
            }

            $doctor_data = DB::table('doctor_extras')
                ->where('doctor_id', '=', $id)
                ->exists();
            if ($doctor_data == 0) {
                return APIResponse::Error('Doctor Speciality or degree not found', status: 404);
            }

            $validated = Validator::make($request->all(), [
                'day' => ['required', Rule::in(Enum::DAY)],
                'date' => 'required',
                'start_time' => 'required',
                'end_time' => 'required',
                'interval' => 'required',
            ]);
            if ($validated->fails()) {
                return APIResponse::Error('Validation failed', $validated->errors()->toArray(), 422);
            }

            $date = $request->post('date');
            $start = $request->post('start_time');
            $end = $request->post('end_time');

            $count = DB::table('availabilities')
                ->where('doc_id', '=', $id)
                ->where('date', '=', $date)
                ->whereRaw('(? BETWEEN start_time AND end_time OR ? BETWEEN start_time AND end_time)', [$start, $end])
                ->count('id');
            if ($count > 0) {
                return APIResponse::Error("Schedule for the given time already exists", status: 409);
            }


            $availability = new Availability();
            $availability->fill($request->all());
            $availability->save();
            DB::commit();
            return APIResponse::Success($availability, status: 201);
        } catch (Exception $e) {
            DB::rollBack();
            return APIResponse::InternalError();
        }

    }

    /*function update(int $id, Request $request): Response|Application|ResponseFactory
    {
        DB::beginTransaction();
        try {
            $availability = Availability::find($id);
            if ($availability === null) {
                return APIResponse::Error('Slots not found', null, 404);
            }
            $validated = Validator::make($request->all(), [
                'day' => ['required', Rule::in(Enum::DAY)],
                'date' => 'required',
                'start_time' => 'required',
                'end_time' => 'required',
                'interval' => 'required',
            ]);
            if ($validated->fails()) {
                return APIResponse::Error('Validation failed', $validated->errors()->toArray(), 422);
            }
            DB::table();
           DB::commit();
            return APIResponse::Success();
        } catch (Exception $e) {
            DB::rollBack();
            return APIResponse::Error('Unknown error occurred', status: 500);
        }
    }*/


    /**
     * Generate Slots for available doctors for tomorrow and day after tomorrow where generated is false
     * @return Response|Application|ResponseFactory
     */
    function generateSlots(): Response|Application|ResponseFactory
    {
        DB::beginTransaction();
        try {
//            $today = date("Y-m-d", strtotime("today"));

            $tomorrow = date("Y-m-d", strtotime("tomorrow"));
            $dayAfterTomorrow = date("Y-m-d", strtotime("tomorrow + 1 day"));

            $result = DB::table('availabilities')
                ->whereIn('date', [$tomorrow, $dayAfterTomorrow])
                ->where('generated', '=', false)
                ->get()->all();


            $slots = array();
            $availability_ids = array();
            foreach ($result as $availability) {
                $date = $availability->date;
                $start_time = $availability->start_time;
                $end_time = $availability->end_time;
                $start_time_min = strtotime($availability->start_time);
                $end_time_min = strtotime($availability->end_time);
                $interval = $availability->interval;
                // TODO handle date change b/w slots
                $time_diff = $end_time_min - $start_time_min;
                $slot_count = $time_diff / ($interval * 60);
                $id = $availability->id;
                for ($i = 0; $i < $slot_count; $i++) {
                    $start = "$date $start_time";
                    $end_interval = date("H:i:s", $start_time_min + ($interval * 60));
                    $end = "$date $end_interval";

                    $slot = array(
                        'start_time' => $start,
                        'end_time' => $end,
                        'avail_id' => $id
                    );
                    array_push($slots, $slot);
                    $start_time = "$end_interval";
                    $start_time_min = strtotime($start_time);

                }
                array_push($availability_ids, $id);
            }
            Slot::insert($slots);
            DB::table('availabilities')->whereIN('id', $availability_ids)->update(array('generated' => true));

            DB::commit();
            return APIResponse::Success(null);
        } catch (Exception $e) {
            DB::rollBack();
            return APIResponse::InternalError();
        }
    }

    /**
     * GET available doctors list function for today, tomorrow and day after tomorrow
     * @param Request $request
     * @return Response|Application|ResponseFactory
     */
    function getDoctors(Request $request): Response|Application|ResponseFactory
    {
        try {
            $current_time = date("Y-m-d H:i:s", strtotime('now'));
            $today = date("Y-m-d", strtotime("today"));
            $tomorrow = date("Y-m-d", strtotime("tomorrow"));
            $dayAfterTomorrow = date("Y-m-d", strtotime("tomorrow + 1 day"));

            $doctor_list = DB::table('availabilities', 'A')
                ->join('slots', 'slots.avail_id', '=', 'A.id')
                ->whereIn('date', [$today, $tomorrow, $dayAfterTomorrow])
                ->where('generated', '=', true)
                ->where('slots.start_time', '>', $current_time)
                ->exists();
            if ($doctor_list == 0) {
                return APIResponse::Error('Doctor Availability not found', status: 404);
            }

            $data = DB::table('availabilities', 'A')
                ->join('users', 'users.id', '=', 'A.doc_id')
                ->join('doctor_extras', 'doctor_extras.doctor_id', '=', 'A.doc_id')
                ->join('slots', 'slots.avail_id', '=', 'A.id')
                ->whereIn('date', [$today, $tomorrow, $dayAfterTomorrow])
                ->where('generated', '=', true)
                ->where('slots.start_time', '>', $current_time)
                ->distinct()
                ->select('users.id', 'users.name', 'doctor_extras.speciality', 'degree')
                ->get();
            return APIResponse::Success($data);
        } catch (Exception $e) {

            return APIResponse::InternalError();
        }
    }

    /**
     * GET availability date of the required doctor
     * return HTTP Status code 200 when successful
     * @param Request $request
     * @param int $id = doctor id
     * @return Response|Application|ResponseFactory
     */
    function getAvailabilityDate(Request $request, int $id): Response|Application|ResponseFactory
    {

        try {
            $current_time = date("Y-m-d H:i:s", strtotime('now'));
            $today = date("Y-m-d", strtotime("today"));
            $tomorrow = date("Y-m-d", strtotime("tomorrow"));
            $dayAfterTomorrow = date("Y-m-d", strtotime("tomorrow + 1 day"));

            $availability = DB::table('availabilities', 'A')
                ->join('slots', 'slots.avail_id', '=', 'A.id')
                ->whereIn('date', [$today, $tomorrow, $dayAfterTomorrow])
                ->where('A.generated', '=', true)
                ->where('A.doc_id', '=', $id)
                ->where('slots.start_time', '>', $current_time)
                ->exists();
            if ($availability == 0) {
                return APIResponse::Error('Doctor Availability not found', status: 404);
            }

            $data = DB::table('availabilities', 'A')
                ->join('slots', 'slots.avail_id', '=', 'A.id')
                ->whereIn('date', [$today, $tomorrow, $dayAfterTomorrow])
                ->where('A.generated', '=', true)
                ->where('A.doc_id', '=', $id)
                ->where('slots.start_time', '>', $current_time)
                ->distinct()
                ->get('A.date');


            return APIResponse::Success($data);
        } catch (Exception $e) {

            return APIResponse::InternalError();
        }
    }

    /**
     * GET availability time of the doctor on a particular date
     * return HTTP Status code 200 when successful
     * request date as a parameter
     * @param Request $request
     * @param int $id = doctor id
     * @return Response|Application|ResponseFactory
     */
    function getAvailabilityTime(Request $request, int $id): Response|Application|ResponseFactory
    {

        try {
            $date = $request->get('date');
            $current_time = date("Y-m-d H:i:s", strtotime('now'));

            $availability = DB::table('availabilities', 'A')
//                ->join('slots', 'slots.avail_id', '=', 'A.id')
                ->where('doc_id', '=', $id)
                ->where('generated', '=', true)
                ->where('A.date', '=', $date)
//                ->where('slots.end_time', '>', $current_time)
                ->exists();
            if ($availability == 0) {
                return APIResponse::Error('Doctor Availability not found', status: 404);
            }

            $data = DB::table('availabilities', 'A')
//                ->join('slots', 'slots.avail_id', '=', 'A.id')
                ->where('doc_id', '=', $id)
                ->where('generated', '=', true)
                ->where('date', '=', $date)
                ->orderBy('start_time', 'ASC')
//                ->where('slots.end_time', '>', $current_time)
                ->select('start_time', 'end_time')->get();


            return APIResponse::Success($data);
        } catch (Exception $e) {
            return APIResponse::InternalError();
        }
    }
}
