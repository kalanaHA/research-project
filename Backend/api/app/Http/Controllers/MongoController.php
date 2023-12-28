<?php

namespace App\Http\Controllers;

use DateTime;
use stdClass;
use Carbon\Carbon;
use MongoDB\Client;
use Illuminate\Http\Request;

class MongoController extends Controller
{
    public function fetch(Request $request)
    {
        $start_date = Carbon::parse($request->input('start_date'));
        $end_date = Carbon::parse($request->input('end_date'));

        $client = new Client();
        $collection = $client->Heal->health;
        $data = $collection->find([], ['sort' => ['created_at' => -1]])->toArray();

        $data = array_filter($data, function ($document) use ($start_date, $end_date) {
            $document->created_time = $document['created_at']->toDateTime()->format('Y-m-d H:i:s');
            $document = (array) $document;
            $document['_id'] = $document['_id']->__toString();
            $document['created_at'] = $document['created_at']->toDateTime()->format('Y-m-d H:i:s');
            $document['updated_at'] = $document['updated_at']->toDateTime()->format('Y-m-d H:i:s');
            // return $start_date < $document['created_at'] && $document['created_at'] < $end_date;
            return $start_date <= $document['created_at'] && $document['created_at'] <= $end_date;
        });
        return response()->json($data);
    }
    public function getUser()
    {

        $client = new Client();
        $collection = $client->Heal->patient;
        $data = $collection->find([], ['sort' => ['created_at' => -1], 'limit' => 1])->toArray();

        $data = array_map(function ($document) {
            $document = (array) $document;
            $document['_id'] = $document['_id']->__toString();
            // $document['created_at'] = $document['created_at']->toDateTime()->format('Y-m-d H:i:s');
            // $document['updated_at'] = $document['updated_at']->toDateTime()->format('Y-m-d H:i:s');
            return $document;
        }, $data);
        return response()->json($data);
    }
    public function diet()
    {
        $client = new Client(); // Create a new MongoDB client
        $collection = $client->Heal->diet_plan; // Select your MongoDB collection
        $data = $collection->find([], ['sort' => ['created_at' => -1], 'limit' => 50])->toArray(); // Retrieve all documents from the collection

        $data = array_map(function ($document) {
            $document = (array) $document;
            $document['diet_plan'] = json_decode($document['diet_plan']);
            $document['_id'] = $document['_id']->__toString();
            $document['created_at'] = $document['created_at']->toDateTime()->format('Y-m-d H:i:s');
            $document['updated_at'] = $document['updated_at']->toDateTime()->format('Y-m-d H:i:s');
            return $document;
        }, $data);
        return response()->json($data);
    }

    public function exercise()
    {
        $client = new Client(); // Create a new MongoDB client
        $collection = $client->Heal->exercise_plan; // Select your MongoDB collection
        $data = $collection->find([], ['sort' => ['created_at' => -1], 'limit' => 50])->toArray(); // Retrieve all documents from the collection    
        $data = array_map(function ($document) {
            $document = (array) $document;
            $document['exercise_plan'] = json_decode($document['exercise_plan']);
            $document['_id'] = $document['_id']->__toString();
            $document['created_at'] = $document['created_at']->toDateTime()->format('Y-m-d H:i:s');
            $document['updated_at'] = $document['updated_at']->toDateTime()->format('Y-m-d H:i:s');
            return $document;
        }, $data);
        return response()->json($data);
    }
    public function risk()
    {
        $client = new Client(); // Create a new MongoDB client
        $collection = $client->Heal->risk_level; // Select your MongoDB collection
        $data = $collection->find([], ['sort' => ['created_at' => -1], 'limit' => 50])->toArray(); // Retrieve all documents from the collection

        // Convert MongoDB documents to arrays
        $data = array_map(function ($document) {
            $document = (array) $document;
            $document['_id'] = $document['_id']->__toString();
            $document['created_at'] = $document['created_at']->toDateTime()->format('Y-m-d H:i:s');
            $document['updated_at'] = $document['updated_at']->toDateTime()->format('Y-m-d H:i:s');
            return $document;
        }, $data);

        return response()->json($data);
    }

    public function generatePDF()
    {
        $client = new Client();
        $collection = $client->Heal->patient;

        $user = $collection->find()->toArray();
        $user = $user[$collection->countDocuments() - 1];
        return view('graph', compact('user'));
    }

    public function register(Request $request)
    {
        $client = new Client();
        $collection = $client->Heal->patient;

        $data = [
            'name' => $request->input('name'),
            'dob' => $request->input('dob'),
            'blood_group' => $request->input('blood_group'),
            'health_status' => $request->input('health_status'),
            'address' => $request->input('address'),
            'created_at' => (new DateTime())->format('Y-m-d H:i:s'),
            'updated_at' => (new DateTime())->format('Y-m-d H:i:s'),
        ];

        $response = $collection->insertOne($data);
        if ($response->getInsertedCount() > 0) {
            $data = new stdClass();
            $data->msg = "success";
            return response()->json($data);
        } else {
            $data = new stdClass();
            $data->msg = "error";
            return response()->json($data);
        }
    }
}
