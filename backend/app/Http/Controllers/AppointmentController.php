<?php

namespace App\Http\Controllers;

use App\Models\APIResponse;
use App\Models\Appointment;
use App\Models\SlotStatus;
use Illuminate\Contracts\Foundation\Application;
use Illuminate\Contracts\Routing\ResponseFactory;
use Illuminate\Http\Request;
use Illuminate\Http\Response;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Validator;
use PHPUnit\Exception;

class AppointmentController
{
    /**
     * Create Appointment function for patients
     * return HTTP Status 201 when appointment is created successfully for a particular doctor
     * @param Request $request
     * @param int $id = slot id
     * @return Response|Application|ResponseFactory
     */
    function createAppointment(Request $request, int $id): Response|Application|ResponseFactory

    {
        DB::beginTransaction();
        try {
            if ($request->user()->id !== $id) {
                return APIResponse::Error('UnAuthorized', status: 403);
            }
            $patient_data = DB::table('patient_extras')
                ->where('patient_id', '=', $id)
                ->exists();
            if ($patient_data == 0) {
                return APIResponse::Error('Patient Weight or Blood Group not found', status: 404);
            }

            $validated = Validator::make($request->all(), [
                'slot_id' => 'required|unique:appointments',
                'booked_by' => 'required',
                'reason' => 'required',
            ]);
            if ($validated->fails()) {
                return APIResponse::Error('Validation failed', $validated->errors()->toArray(), 422);
            }
            $slot_id = $request->post('slot_id');


            /*if (DB::table('appointments')
                ->join('users', 'users.id', '=', $id)
                ->where('users.role', '=', Role::PATIENT)) {
                return APIResponse::Error("Not Authorized", status: 403);
            }*/


            DB::table('slots')->where('id', '=', $slot_id)->update(array('status' => SlotStatus::BOOKED));
            $appointment = new Appointment();
            $appointment->fill($request->all());
            $appointment->save();
            DB::commit();
            return APIResponse::Success($appointment);
        } catch (Exception $e) {
            DB::rollBack();
            return APIResponse::InternalError();
        }
    }

    /**
     * GET Slot info function to fetch doctor name and slot time
     * return HTTP Status code 200 when successful
     * @param Request $request
     * @param int $slot_id
     * @return Response|Application|ResponseFactory
     */
    function getSlotInfo(Request $request, int $slot_id): Response|Application|ResponseFactory
    {
        /*$slot_id = $request->get('id');*/
        try {

            $data = DB::table('slots')
                ->join('availabilities', 'availabilities.id', '=', 'avail_id')
                ->join('users', 'users.id', '=', 'availabilities.doc_id')
                ->where('slots.id', '=', $slot_id)
                ->select('slots.start_time', 'users.name')
                ->get();
            return APIResponse::Success($data);
        } catch (Exception $e) {

            return APIResponse::InternalError();
        }
    }

    /**
     * GET upcoming appointments made by patients for the particular day.
     * if no appointments are found returns HTTP Status code 404.
     * @param Request $request
     * @param int $doc_id = doctor id
     * @return Response|Application|ResponseFactory
     */
    function getPatientAppointment(Request $request, int $doc_id): Response|Application|ResponseFactory
    {
        try {
            if ($request->user()->id !== $doc_id) {
                return APIResponse::Error('UnAuthorized', status: 403);
            }
            $current_time = date("Y-m-d H:i:s", strtotime('now'));
            $today = date("Y-m-d", strtotime("today"));

            $appointment = DB::table('availabilities', 'Avail')
                ->join('slots', 'slots.avail_id', '=', 'Avail.id')
                ->join('appointments', 'appointments.slot_id', '=', 'slots.id')
                ->where('Avail.doc_id', '=', $doc_id)
                ->where('Avail.date', '=', $today)
                ->where('slots.start_time', '>=', $current_time)
                ->where('slots.status', '=', 'BOOKED')
                ->exists();
            if ($appointment == 0) {
                return APIResponse::Error('Patient Appointment Not found', status: 404);
            }

            $data = DB::table('availabilities', 'Avail')
                ->join('slots', 'slots.avail_id', '=', 'Avail.id')
                ->join('appointments', 'appointments.slot_id', '=', 'slots.id')
                ->join('users', 'users.id', '=', 'appointments.booked_by')
                ->join('patient_extras', 'patient_extras.patient_id', '=', 'users.id')
                ->where('Avail.doc_id', '=', $doc_id)
                ->where('Avail.date', '=', $today)
                ->where('slots.start_time', '>=', $current_time)
                ->where('slots.status', '=', 'BOOKED')
                ->orderBy('slots.id', 'ASC')
                ->select('users.name', 'users.phone', 'users.address', 'slots.start_time', 'appointments.reason', 'appointments.notes', 'patient_extras.blood_group', 'patient_extras.weight')
                ->get();
            return APIResponse::Success($data);
        } catch (Exception $e) {

            return APIResponse::InternalError();
        }
    }

    /**
     * GET appointment history made by a particular patient
     * @param Request $request
     * @param int $id = patient id
     * @return Response|Application|ResponseFactory
     */
    function getPatientAppointmentHistory(Request $request, int $id): Response|Application|ResponseFactory
    {
        try {
            if ($request->user()->id !== $id) {
                return APIResponse::Error('UnAuthorized', status: 403);
            }
            $patient_appointment = DB::table('appointments')
                ->where('booked_by', '=', $id)
                ->exists();
            if ($patient_appointment == 0) {
                return APIResponse::Error('Patient Appointment History Not found', status: 404);
            }

            $data = DB::table('appointments')
                ->join('slots', 'slots.id', '=', 'appointments.slot_id')
                ->join('availabilities', 'availabilities.id', '=', 'slots.avail_id')
                ->join('users', 'users.id', '=', 'availabilities.doc_id')
                ->join('doctor_extras', 'doctor_extras.doctor_id', '=', 'users.id')
                ->where('appointments.booked_by', '=', $id)
                ->orderBy('slots.id', 'ASC')
                ->select('users.name', 'appointments.reason', 'slots.start_time', 'doctor_extras.speciality', 'doctor_extras.degree')
                ->get();
            return APIResponse::Success($data);
        } catch (Exception $e) {

            return APIResponse::InternalError();
        }
    }

    function deleteBookedSlots(Request $request, int $id, int $slot_id): Response|Application|ResponseFactory
    {
        DB::beginTransaction();
        try {
            if ($request->user()->id !== $id) {
                return APIResponse::Error('UnAuthorized', status: 403);
            }
            $exists = DB::table('appointments')
                ->where('slot_id', '=', $slot_id)
                ->where('booked_by', '=', $id)
                ->exists();
            if ($exists == 0) {
                return APIResponse::Error("No appointment found", status: 404);
            }

            DB::table('appointments')
                ->where('slot_id', '=', $slot_id)
                ->where('booked_by', '=', $id)
                ->delete();

            DB::table('slots')
                ->where('id', '=', $slot_id)
                ->update(array('status' => SlotStatus::AVAILABLE));
            $data = (object)array('message' => 'Appointment Cancelled Successfully.');
            DB::commit();
            return APIResponse::Success($data);

        } catch (Exception $e) {

            return APIResponse::InternalError();
        }
    }

    function getBookedSlots(Request $request, int $id): Response|Application|ResponseFactory
    {
        try {
            if ($request->user()->id !== $id) {
                return APIResponse::Error('UnAuthorized', status: 403);
            }
            $current_time = date("Y-m-d H:i:s", strtotime('now'));
            $exists = DB::table('appointments')
                ->join('slots', 'slots.id', '=', 'appointments.slot_id')
                ->where('booked_by', '=', $id)
                ->where('slots.start_time', '>=', $current_time)->exists();
            if ($exists == 0) {
                return APIResponse::Error("No appointment found", status: 404);
            }

            $data = DB::table('appointments')
                ->join('slots', 'slots.id', '=', 'appointments.slot_id')
                ->join('availabilities', 'availabilities.id', '=', 'slots.avail_id')
                ->join('users', 'users.id', '=', 'availabilities.doc_id')
                ->join('doctor_extras', 'doctor_extras.doctor_id', '=', 'users.id')
                ->where('appointments.booked_by', '=', $id)
                ->where('slots.start_time', '>=', $current_time)
                ->select('appointments.slot_id', 'users.name', 'appointments.reason', 'slots.start_time', 'doctor_extras.speciality', 'doctor_extras.degree')
                ->orderBy('slots.id', 'ASC')
                ->get();
            return APIResponse::Success($data);
        } catch (Exception $e) {

            return APIResponse::InternalError();
        }

    }
}
