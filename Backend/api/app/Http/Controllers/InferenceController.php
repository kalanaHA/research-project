<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Http;

class InferenceController extends Controller
{
    public function performInference(Request $request)
    {
        // Uncomment this part if you want to see the request data
        // dd($request);

        // Retrieve data from the Android app's request
        $data = [
            'Diabetic' => $request->input('Diabetic'),
            'Alcohol_Level' => $request->input('Alcohol_Level'),
            'Weight' => $request->input('Weight'),
            'MRI_Delay' => $request->input('MRI_Delay')
        ];

        // Prepare the data to be sent to the Python script
        $dataJson = json_encode($data);

        // Run the Python script for inference (uncomment this when you have the script)
        $response = Http::post('http://localhost:5000/inference', ['data' => $dataJson]);

        if ($response->successful()) {
            $inferenceResult = $response->json();
            return response()->json(['result' => $inferenceResult]);
        } else {
            return response()->json(['error' => 'Inference request failed'], 500);
        }
        // Return the inference result to the Android app
        return response()->json(['result' => 'Inference result goes here']);
    }
}
