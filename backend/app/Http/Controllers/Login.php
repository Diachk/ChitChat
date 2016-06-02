<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Http\Requests;
use Auth;
use Log;
use DB;

class Login extends Controller
{
    function getUser(Request $request) {


        $email = $request->input('email');
        $password = $request->input('password');
        
        $login_response['success'] = false;
        $login_response['user'] = null;

        Log::info("Login attempt username = ".$email." password = ".$password);


        $name = DB::table('users')
            ->where('email', $email)
            ->where('password', $password)
            ->value('name');

        if (isset($name)) {
            // Authentication passed...
            $login_response['success'] = true;
            $login_response['user'] = $name;
            Log::info("Authentication passed");
        }
        else {
            Log::info("Authentication failed");
        }


        return response()->json($login_response);
    }
}
