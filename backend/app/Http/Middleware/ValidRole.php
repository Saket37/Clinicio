<?php

namespace App\Http\Middleware;

use App\Models\APIResponse;
use Closure;
use Illuminate\Http\Request;

class ValidRole
{
    /**
     * Handle an incoming request.
     * return HTTP Status code 403 when user is not authorized
     * @param Request $request
     * @param Closure $next
     * @param string $role = user role
     * @return mixed
     */
    public function handle(Request $request, Closure $next, string $role)
    {
        if ($request->user()->role != $role) {
            abort(APIResponse::Error("Not Authorized", status: 403));
        }
        return $next($request);
    }
}
