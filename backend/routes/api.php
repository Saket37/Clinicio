<?php

use App\Http\Controllers\AppointmentController;
use App\Http\Controllers\AvailabilitiesController;
use App\Http\Controllers\ExtrasController;
use App\Http\Controllers\SlotsController;
use App\Http\Controllers\UserController;
use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/

Route::post('/users', [UserController::class, 'create']);
Route::post('/users/login', [UserController::class, 'login']);
Route::post('/users/logout', [UserController::class, 'logout'])->middleware('auth:sanctum');


Route::get('/doctors/me', [UserController::class, 'getDoctor'])->middleware('auth:sanctum')
    ->middleware("allowed_role:DOCTOR");

Route::get('/patients/me', [UserController::class, 'getPatient'])->middleware('auth:sanctum')
    ->middleware("allowed_role:PATIENT");

// Availabilities for doctor
Route::post('/doctors/{id}/availabilities', [AvailabilitiesController::class, 'createAvailability'])
    ->middleware('auth:sanctum')->middleware("allowed_role:DOCTOR");


// Generates slot for available time slots for doctors
Route::post('/cron/generate/slots', [AvailabilitiesController::class, 'generateSlots']);

// Get Doctor List and their respective slots for Patients
Route::get('/doctors', [AvailabilitiesController::class, 'getDoctors'])
    ->middleware('auth:sanctum')
    ->middleware("allowed_role:PATIENT");

Route::get('/doctors/{id}/availabilities/date', [AvailabilitiesController::class, 'getAvailabilityDate'])
    ->middleware('auth:sanctum')->middleware("allowed_role:PATIENT");

Route::get('/doctors/{id}/availabilities/time', [AvailabilitiesController::class, 'getAvailabilityTime'])
    ->middleware('auth:sanctum')->middleware("allowed_role:PATIENT");

Route::get('/doctors/{id}/slots', [SlotsController::class, 'getSlots'])->middleware('auth:sanctum')
    ->middleware('auth:sanctum')->middleware("allowed_role:PATIENT");


// Update user info
Route::post('/users/{id}', [UserController::class, 'updateUser'])
    ->middleware('auth:sanctum');

Route::post('/doctors/{id}/extras', [ExtrasController::class, 'upsertDoctor'])
    ->middleware('auth:sanctum')->middleware("allowed_role:DOCTOR");

Route::post('/patients/{id}/extras', [ExtrasController::class, 'upsertPatient'])
    ->middleware('auth:sanctum')->middleware("allowed_role:PATIENT");

// book appointment for patients
Route::post('/patients/{id}/appointment', [AppointmentController::class, 'createAppointment'])
    ->middleware('auth:sanctum')->middleware("allowed_role:PATIENT");

Route::get('/slots/{id}/info', [AppointmentController::class, 'getSlotInfo'])
    ->middleware('auth:sanctum')->middleware("allowed_role:PATIENT");

Route::get('/patients/{id}/history', [AppointmentController::class, 'getPatientAppointmentHistory'])
    ->middleware('auth:sanctum')->middleware("allowed_role:PATIENT");

// delete appointment for patient
Route::post('/patients/{id}/slot/{slot_id}', [AppointmentController::class, 'deleteBookedSlots'])
    ->middleware('auth:sanctum')->middleware("allowed_role:PATIENT");

Route::get('/patients/{id}/slot', [AppointmentController::class, 'getBookedSlots'])
    ->middleware('auth:sanctum')->middleware("allowed_role:PATIENT");

// List of appointments for doctor
Route::get('/doctor/{id}/appointments', [AppointmentController::class, 'getPatientAppointment'])
    ->middleware('auth:sanctum')->middleware("allowed_role:DOCTOR");





