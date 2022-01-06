package com.example.weatherapplication.API

import com.example.weatherapplication.data.TodayForecast.WeatherResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    companion object{
        const val API_KEY = "41afd91e8508faf248e58bef14ffea2d"
    }


    @GET("?appid=${API_KEY}&units=metric")
    fun getWeatherByCity(@Query("q") city: String): Call<WeatherResult>


    // https://api.openweathermap.org/data/2.5/onecall?lat=33.88&lon=35.49&exclude=minutely,hourly,current&appid=41afd91e8508faf248e58bef14ffea2d&units=metric

}