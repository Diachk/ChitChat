<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Http\Requests;
use DB;
use Log;

class Contacts extends Controller
{
    function resolveContacts(Request $request) {
        $data = $request->getContent();
        $phoneNumbers = json_decode($data)->numbers;

        $numberOfContactsToResolve = (isset($phoneNumbers) ?
            count($phoneNumbers) : 0 );

        Log::info('Incoming request to resolve '
            .$numberOfContactsToResolve
            .' contacts');

        $contacts = [];

        if($numberOfContactsToResolve > 0) {
            $contacts = DB::table('users')
                ->select("name", "status_message", "last_seen")
                ->whereIn("phone_number" , $phoneNumbers)
                ->get();
        }

        $numberOfContactsResolved = count($contacts);

        Log::info($numberOfContactsResolved.' contacts resolved');

        $result["contacts"] = $contacts;

        return response()->json($result);
    }
}
