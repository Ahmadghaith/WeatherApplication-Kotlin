package com.example.weatherapplication.API

import com.example.weatherapplication.data.HourlyForecast.HourlyResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface HourlyForecastApi {

    companion object{
        const val API_KEY = "41afd91e8508faf248e58bef14ffea2d"
    }


    @GET("?appid=${API_KEY}&units=metric&cnt=16")
    fun getHourlyWeather(@Query("q") cityname: String): Call<HourlyResult>
}