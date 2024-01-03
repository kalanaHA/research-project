<?php

use App\Http\Controllers\MongoController;
use App\Http\Controllers\v1\TelegramController;
use App\Http\Controllers\InferenceController;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider and all of them will
| be assigned to the "api" middleware group. Make something great!
|
*/

Route::get('/risk-alert', [TelegramController::class, 'send_alert']);
Route::get('/diet-plan', [MongoController::class, 'diet']);
Route::get('/exercise-plan', [MongoController::class, 'exercise']);
Route::get('/risk-history', [MongoController::class, 'risk']);
Route::post('/latest-readings', [MongoController::class, 'fetch']);
Route::get('/report', [MongoController::class, 'generatePDF']);
Route::post('/register', [MongoController::class, 'register']);
Route::get('/user', [MongoController::class, 'getUser']);
Route::post('/perform-inference', [InferenceController::class, 'performInference']);

