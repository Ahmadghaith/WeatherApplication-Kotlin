package com.example.weatherapplication.API

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET

interface WeatherApi {

    @GET("?q=beirut&appid=41afd91e8508faf248e58bef14ffea2d&units=metric")
    fun getWeatherByCity(): Call<JsonObject>
}