<?php

namespace App\Models;

use Illuminate\Contracts\Foundation\Application;
use Illuminate\Contracts\Routing\ResponseFactory;
use Illuminate\Contracts\Support\Jsonable;
use Illuminate\Http\Response;
use JsonSerializable;

class APIResponse implements Jsonable, JsonSerializable
{

    private bool $success;
    private object|null $data;
    private object|null $error;

    private function __construct()
    {

    }

    /**
     * Responds HTTP Status 500 to client when there is unknown error
     * @return Response|Application|ResponseFactory
     */
    public static function InternalError(): Response|Application|ResponseFactory
    {
        return self::Error('Unknown Error Occurred, try again later', status: 500);
    }

    /**
     * Responds with particular HTTP Status code when and error occurs
     * @param string $msg
     * @param array|null $fields
     * @param int $status
     * @param array $headers
     * @return Application|ResponseFactory|Response
     */
    public static function Error(string $msg, array|null $fields = null, int $status = 500, array $headers = []): Application|ResponseFactory|Response
    {
        $resp = new APIResponse();

        $resp->success = false;
        $resp->data = null;

        if ($fields !== null) {
            $temp = array();
            foreach ($fields as $field => $field_err) {
                array_push($temp, array($field => $field_err[0]));
            }

            $fields = $temp;
        }

        $resp->error = (object)array(
            'message' => $msg,
            'fields' => $fields
        );


        return response($resp, $status, $headers);
    }

    /**
     * Responds with particular HTTP Status code when success
     * @param object|null $data
     * @param int $status
     * @param array $headers
     * @return Application|ResponseFactory|Response
     */
    public static function Success(object|null $data = null, int $status = 200, array $headers = []): Application|ResponseFactory|Response
    {
        $resp = new APIResponse();

        $resp->success = true;
        $resp->data = $data;
        $resp->error = null;

        return response($resp, $status, $headers);
    }

    /**
     *
     * @param int $options
     * @return bool|string
     */
    public function toJson($options = 0): bool|string
    {
        return json_encode($this->jsonSerialize(), $options);
    }

    /**
     * @return array
     */
    public function jsonSerialize(): array
    {
        return get_object_vars($this);
    }
}
