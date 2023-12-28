<?php

namespace App\Http\Controllers\v1;

use DateTime;
use Exception;
use Carbon\Carbon;
use GuzzleHttp\Client;
use Illuminate\Http\Request;
use MongoDB\Client as MongoClient;
use App\Http\Controllers\Controller;

class TelegramController extends Controller
{
    public function send_alert()
    {

        $client = new Client();
        $Mongoclient = new MongoClient();
        $collection = $Mongoclient->Heal->patient;

        $user = $collection->find()->toArray();
        $user = $user[$collection->countDocuments() - 1];

        // Set the API URL and chat ID
        $bot_api = env('BOT_API_KEY');
        $apiUrl = 'https://api.telegram.org/bot' . $bot_api . '/sendMessage';
        $chatId = env('CHANNEL_ID');
        // dd($chatId, $bot_api);

        // $project_name = $msg_content->project_name;
        // $domain_name = $msg_content->domain_name;
        // $severity_level = $msg_content->severity;
        // $error_msg = $msg_content->error_msg;

        // $formattedMessage = "*Alert Manager*\n\n*Project Name*: $project_name\n*Domain Name*: $domain_name\n*Severity Level*: $severity_level\n*Error Message*: $error_msg";
        $formattedMessage = "*--- Risk Advisor ---*\n\n*Name* : " . $user['name'] . "\n*Age* : " . now()->year - Carbon::parse($user->dob)->year . "\n*Blood Group* : " . $user['blood_group'] . "\n*Known Issues* : " . $user['health_status'] . "\n*Risk Status* : Dangerous";
        // Set the message text

        try {
            // Send the POST request with form data
            $response = $client->post($apiUrl, [
                'form_params' => [
                    'chat_id' => $chatId,
                    'text' => $formattedMessage,
                    'parse_mode' => 'Markdown',
                ],
            ]);

            // Get the response body
            $responseBody = $response->getBody();
            $new_collection = $Mongoclient->Heal->risk_history;
            $data = [
                'msg' => json_encode(str($responseBody)),
                'created_at' => (new DateTime())->format('Y-m-d H:i:s'),
                'updated_at' => (new DateTime())->format('Y-m-d H:i:s'),
            ];

            $response = $new_collection->insertOne($data);
            // Process the response as needed
            // ...
            
            //return $response->getInsertedCount();
            return $responseBody;
        } catch (Exception $e) {
            // Handle any exceptions that occur
            return $e;
            // ...
        }
    }
}
