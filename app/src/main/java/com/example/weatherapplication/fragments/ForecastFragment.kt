package com.example.weatherapplication.fragments

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapplication.API.DailyForecastApi
import com.example.weatherapplication.Adapter.DailyAdapter
import com.example.weatherapplication.Adapter.DailyItems
import com.example.weatherapplication.R
import com.example.weatherapplication.data.DailyForecast.DailyForecast
import com.example.weatherapplication.data.DailyForecast.Days
import com.example.weatherapplication.data.CityInfo
import com.example.weatherapplication.data.DailyForecast.Weather
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class ForecastFragment : Fragment() {

    private lateinit var itemRecycleView: DailyAdapter

    // Full url: https://api.openweathermap.org/data/2.5/forecast/daily?q=beirut&appid=41afd91e8508faf248e58bef14ffea2d
    private val baseUrl7Days = "https://api.openweathermap.org/data/2.5/forecast/daily/"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_forecast, container, false)

        initRecycleView(view)
        loadDailyForecast()

        return view
    }


    private fun loadDailyForecast() {

        //Getting the city name from the object CityInfo
        val cityName = CityInfo.cityname

        //Retrofit instance
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl7Days)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val dailyForecastApi = retrofit.create(DailyForecastApi::class.java)

        //OpenWeatherApi call
        val result = dailyForecastApi.getDailyWeather(cityName)
        result.enqueue(object : Callback<DailyForecast> {
            @SuppressLint("SetTextI18n", "SimpleDateFormat")
            override fun onResponse(call: Call<DailyForecast>, response: Response<DailyForecast>)
            {
                if(response.isSuccessful)
                {
                    val resp = response.body()

                    for (i : Days in resp?.list!!)
                    {
                        //Converting date
                        val sdfDate = SimpleDateFormat("EEE dd, MMMM")
                        val dt = Date(i.dt* 1000)
                        val newDate = sdfDate.format(dt)

                        //Another loop to get each icon
                        for(j : Weather in i.weather){
                                Picasso.get()
                                    .load("https://openweathermap.org/img/w/${j.icon}.png")
                                    .resize(250,250)
                                    .into(object : com.squareup.picasso.Target {
                                        override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                                            itemRecycleView.add(DailyItems(newDate, bitmap!!, "↑ ${i.temp.max}°C ", "↓ ${i.temp.min}°C"))
                                        }
                                        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
                                        override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}
                                })
                        }
                    }
                }
            }
            override fun onFailure(call: Call<DailyForecast>, t: Throwable) {
            }
        })
    }

    //Initializing 7 days forecast Recyclerview
    private fun initRecycleView(view:View)
    {
        val recycleView = view.findViewById<RecyclerView>(R.id.recyclerviewDays)
        recycleView.layoutManager = LinearLayoutManager(this@ForecastFragment.context)
        itemRecycleView = DailyAdapter()
        recycleView.adapter = itemRecycleView
    }
}