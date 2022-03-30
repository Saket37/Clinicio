<?php

namespace App\Http\Controllers;

use App\Models\APIResponse;
use Illuminate\Contracts\Foundation\Application;
use Illuminate\Contracts\Routing\ResponseFactory;
use Illuminate\Http\Request;
use Illuminate\Http\Response;
use Illuminate\Support\Facades\DB;
use PHPUnit\Exception;


class SlotsController extends Controller
{
    /* function get(Request $request, int $id): Response|Application|ResponseFactory
     {

         $data = DB::table('slots')
             ->join('availabilities', 'availabilities.id', '=', 'avail_id')
             ->where('doc_id', '=', $id)
             ->get('slots.*');
         return APIResponse::Success($data);
     }*/

    /**
     * Get all the slots for a doctor on a particular date and time
     * @param Request $request
     * @param int $id = doctor id
     * @return Response|Application|ResponseFactory
     */
    function getSlots(Request $request, int $id): Response|Application|ResponseFactory
    {
        try {
            $current_time = date("Y-m-d H:i:s", strtotime('now'));
            $date = $request->get('date');
            $time = $request->get('time');

            $slots = DB::table('slots')
                ->join('availabilities', 'availabilities.id', '=', 'avail_id')
                ->where('availabilities.doc_id', '=', $id)
                ->where('availabilities.date', '=', $date)
                ->where('availabilities.start_time', '=', $time)
                ->where('slots.start_time', '>', $current_time)
                ->exists();
            if ($slots == 0) {
                return APIResponse::Error('Slots not found', status: 404);
            }

            $data = DB::table('slots')
                ->join('availabilities', 'availabilities.id', '=', 'avail_id')
                ->where('availabilities.doc_id', '=', $id)
                ->where('availabilities.date', '=', $date)
                ->where('availabilities.start_time', '=', $time)
                ->where('slots.start_time', '>', $current_time)
                ->select('slots.*')
                ->get();

            return APIResponse::Success($data);
        } catch (Exception $e) {

            return APIResponse::InternalError();
        }
    }


}
