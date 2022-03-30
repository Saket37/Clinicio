<?php

namespace App\Http\Controllers;

use App\Models\APIResponse;
use App\Models\User;
use Illuminate\Contracts\Foundation\Application;
use Illuminate\Contracts\Routing\ResponseFactory;
use Illuminate\Http\Request;
use Illuminate\Http\Response;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Validator;
use PHPUnit\Exception;

class UserController extends Controller
{


    /**
     * Register User function
     * returns HTTP Status code 201 when user is registered successfully
     *  return HTTP Status code 422 when validation fails
     * @param Request $request
     * @return Response|Application|ResponseFactory
     */
    public function create(Request $request): Response|Application|ResponseFactory
    {
        DB::beginTransaction();
        try {
            $validated = Validator::make($request->all(), [
                    'name' => 'required|max:100|regex:/^[A-Za-z ]+$/',
                    'email' => 'required|email|unique:users',
                    'password' => 'required|regex:/^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$/',
                    'phone' => 'required|unique:users|regex:/^[9876][0-9]{9}$/',
                    'address' => 'required',
                    'role' => 'required|in:DOCTOR,PATIENT'
                ]
            );

            if ($validated->fails()) {
                return APIResponse::Error('Validation failed', $validated->errors()->toArray(), 422);
            }

            $user = new User();
            $user->fill($request->all());
            $user->save();

            DB::commit();
            return APIResponse::Success($user, 201);
        } catch (Exception $e) {
            DB::rollBack();
            return APIResponse::InternalError();
        }
    }


    /**
     * GET doctor info function
     * returns doctor info when the bearer token is sent of the logged-in user
     * @param Request $request
     * @return Response|Application|ResponseFactory
     */
    public function getDoctor(Request $request): Response|Application|ResponseFactory
    {
        try {
            $id = $request->user()->id;

            $user = User::with('doctorExtras')->where('id', '=', $id)->first();
            return APIResponse::Success($user);
        } catch (Exception $e) {
            return APIResponse::InternalError();
        }

    }


    /**
     * GET patient info function
     * returns patient info when the bearer token is sent of the logged-in user
     * @param Request $request
     * @return Response|Application|ResponseFactory
     */
    public function getPatient(Request $request): Response|Application|ResponseFactory
    {
        try {
            $id = $request->user()->id;

            $user = User::with('patientExtras')->where('id', '=', $id)->first();
            return APIResponse::Success($user);
        } catch (Exception $e) {
            return APIResponse::InternalError();
        }
    }


    /**
     * Update user function
     * user can't update role and password through this function
     * @param Request $request
     * @param int $id = user id
     * @return Response|Application|ResponseFactory
     */
    public function updateUser(Request $request, int $id): Response|Application|ResponseFactory
    {
        /*if ($request->has('role') || $request->has('password')) {
            return APIResponse::Error('Password and role cannot be updated', null, 403);
        }*/
        DB::beginTransaction();
        try {
            if ($request->user()->id !== $id) {
                return APIResponse::Error('UnAuthorized', status: 403);
            }

            $validated = Validator::make($request->all(), [
                    'name' => 'max:100|regex:/^[A-Za-z ]+$/',
                    'email' => 'email',
                    'phone' => 'regex:/^[9876][0-9]{9}$/'
                ]
            );

            if ($validated->fails()) {
                return APIResponse::Error('Validation failed', $validated->errors()->toArray());
            }

            $user = User::find($id);
            if ($user === null) {
                return APIResponse::Error('User not found', null, 404);
            }

            $data = $request->all();
            unset($data['password']);
            unset($data['role']);

            $user->update($data);
            DB::commit();
            return APIResponse::Success($user);
        } catch (Exception $e) {
            DB::rollBack();
            return APIResponse::InternalError();
        }
    }


    /**
     * Login User function
     * return HTTP Status code 200 when login is successful
     * when the login is successful a token is created
     * return HTTP Status code 422 when validation fails
     * @param Request $request
     * @return Response|Application|ResponseFactory
     */
    public function login(Request $request): Response|Application|ResponseFactory
    {
        DB::beginTransaction();
        try {
            $validated = Validator::make($request->all(), [
                'email' => 'required|email',
                'password' => 'required',
                'device_name' => 'required',
            ]);
            if ($validated->fails()) {
                return APIResponse::Error('Validation failed', $validated->errors()->toArray(), 422);
            }

            $user = User::where('email', $request->email)->first();

            /* var_dump($request->password);
             var_dump($user->password);
             var_dump(Hash::needsRehash($user->password));
             var_dump(password_get_info($user->password));
             var_dump(Hash::check($request->password, $user->password));
             var_dump(password_verify($request->password, $user->password));*/

            if ($user === null || !Hash::check($request->password, $user->password)) {

                $fields = array(
                    'email' => ['email or password is incorrect'],
                    'password' => ['email or password is incorrect'],
                );
                return APIResponse::Error('The provided credentials are incorrect', $fields, 401);
            }

            $token = $user->createToken($request->device_name, [$user->role])->plainTextToken;

            $data = (object)array(
                'token' => $token,
                'user' => $user
            );
            DB::commit();
            return APIResponse::Success($data);
        } catch (Exception $e) {
            DB::rollBack();
            return APIResponse::InternalError();
        }
    }


    /**
     * Logout User function
     * return HTTP Status code 200 when user is logged out successfully
     * token is deleted when user logs out
     * @param Request $request
     * @return Response|Application|ResponseFactory
     */
    public function logout(Request $request): Response|Application|ResponseFactory
    {
        try {
            $request->user()->currentAccessToken()->delete();

            $data = (object)array('message' => 'User is logged out successfully.');
            return APIResponse::Success($data);
        } catch (Exception $e) {
            return APIResponse::InternalError();
        }
    }

}
