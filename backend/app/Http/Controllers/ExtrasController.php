<?php

namespace App\Http\Controllers;

use App\Models\APIResponse;
use App\Models\BloodGroup;
use App\Models\Role;
use App\Models\Speciality;
use Illuminate\Contracts\Foundation\Application;
use Illuminate\Contracts\Routing\ResponseFactory;
use Illuminate\Http\Request;
use Illuminate\Http\Response;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Validator;
use Illuminate\Validation\Rule;
use PHPUnit\Exception;

class ExtrasController extends Controller
{


    /**
     * Upsert Doctor extras function
     * return HTTP Status code 200 when fields are updated or inserted
     * returns HTTP Status code when validation fails
     * @param Request $request
     * @param int $id = doctor id
     * @return Response|Application|ResponseFactory
     */
    public function upsertDoctor(Request $request, int $id): Response|Application|ResponseFactory
    {
        DB::beginTransaction();
        try {
            if ($request->user()->id !== $id) {
                return APIResponse::Error('UnAuthorized', status: 403);
            }
            $table = '';
            $unique = '';
            $values = [];
            $role = $request->user()->role;
            if ($role == Role::DOCTOR) {
                $table = 'doctor_extras';
                $unique = 'doctor_id';
                $values = array(
                    'speciality' => $request->post('speciality'),
                    'degree' => $request->post('degree'),
                    'doctor_id' => $id
                );
                $validated = Validator::make($request->all(), [
                    'speciality' => Rule::in(Speciality::SPECIALITY)
                ]);
                if ($validated->fails()) {
                    return APIResponse::Error('Validation failed', $validated->errors()->toArray(), 422);
                }
            }

            $extras = DB::table($table)->upsert($values, $unique, $values);
            /*if ($extras === 0) {
                return APIResponse::Error("Something went wrong.", status: 500);
            }*/
            DB::commit();
            return APIResponse::Success();
        } catch (Exception $e) {
            DB::rollBack();
            return APIResponse::InternalError();
        }

    }


    /**
     * Upsert Patient extras function
     * return HTTP Status code 200 when fields are updated or inserted
     * returns HTTP Status code when validation fails
     * @param Request $request
     * @param int $id
     * @return Response|Application|ResponseFactory
     */
    public function upsertPatient(Request $request, int $id): Response|Application|ResponseFactory
    {
        DB::beginTransaction();
        try{
            if ($request->user()->id !== $id) {
                return APIResponse::Error('UnAuthorized', status: 403);
            }
        $table = '';
        $unique = '';
        $values = [];
        $role = $request->user()->role;

        if ($role == Role::PATIENT) {
            $table = 'patient_extras';
            $unique = 'patient_id';
            $values = array(
                'blood_group' => $request->post('blood_group'),
                'weight' => $request->post('weight'),
                'patient_id' => $id
            );
            $validated = Validator::make($request->all(), [
                'speciality' => Rule::in(BloodGroup::BLOOD_GROUP)
            ]);
            if ($validated->fails()) {
                return APIResponse::Error('Validation failed', $validated->errors()->toArray(), 422);
            }
        }

        $extras = DB::table($table)->upsert($values, $unique, $values);
        /*if ($extras === 0) {
            return APIResponse::Error("Something went wrong.", status: 500);
        }*/
            DB::commit();
        return APIResponse::Success();
        } catch (Exception $e) {
            DB::rollBack();
            return APIResponse::InternalError();
        }

    }

}
