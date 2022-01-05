package com.example.weatherapplication.API

import com.example.weatherapplication.data.DailyForecast.DailyForecast
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface DailyForecastApi {

    companion object{
        const val API_KEY = "41afd91e8508faf248e58bef14ffea2d"
    }


    @GET("?appid=${API_KEY}&units=metric")
    fun getDailyWeather(@Query("q") cityname: String): Call<DailyForecast>

}
