package com.example.weatherapplication.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.weatherapplication.API.DailyForecastApi
import com.example.weatherapplication.API.WeatherApi
import com.example.weatherapplication.R
import com.example.weatherapplication.data.DailyForecast.DailyForecast
import com.example.weatherapplication.data.TodayData.WeatherResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ForecastFragment : Fragment() {

    private val baseUrlToday = "https://api.openweathermap.org/data/2.5/weather/"
    //private val baseUrl7days = "https://api.openweathermap.org/data/2.5/onecall/?lat="+ lat +"&lon="+ lon +"&exclude=minutely,hourly,current&appid=41afd91e8508faf248e58bef14ffea2d&units=metric"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_forecast, container, false)


        fun getWeather(city : String){

            //Retrofit instance
            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrlToday)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val weatherApi = retrofit.create(WeatherApi::class.java)

            //OpenWeatherApi call
            val result = weatherApi.getWeatherByCity(city)
            result.enqueue(object : Callback<WeatherResult> {
                override fun onResponse(call: Call<WeatherResult>, response: Response<WeatherResult>) {
                    if (response.isSuccessful) {

                        val resp = response.body()

                        val cityname = "${resp?.name}"
                        loadDailyForecast(cityname)
                    }
                }
                override fun onFailure(call: Call<WeatherResult>, t: Throwable) {
                }
            })

        }
        return view
    }


    fun loadDailyForecast(cityname: String) {
        val baseUrl7days = "https://api.openweathermap.org/data/2.5/forecast/daily/"
        //https://api.openweathermap.org/data/2.5/onecall?lat=33.88&lon=35.49&exclude=minutely,hourly,current&appid=41afd91e8508faf248e58bef14ffea2d&units=metric
        val textview = view?.findViewById<TextView>(R.id.textInfo)

        //Retrofit instance
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl7days)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val DailyForecastApi = retrofit.create(DailyForecastApi::class.java)

        //OpenWeatherApi call
        val result = DailyForecastApi.getDailyWeather(cityname)
        result.enqueue(object : Callback<DailyForecast> {
            @SuppressLint("SetTextI18n", "SimpleDateFormat")
            override fun onResponse(call: Call<DailyForecast>, response: Response<DailyForecast>)
            {
                if(response.isSuccessful)
                {
                    val respn = response.body()
                    textview?.text= "${respn?.city?.coord?.lat}, ${respn?.city?.coord?.lon}"

                }
            }

            override fun onFailure(call: Call<DailyForecast>, t: Throwable) {

            }




        })
    }

}