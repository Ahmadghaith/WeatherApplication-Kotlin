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
        initRecycleView(view)
        loadDailyForecast()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    fun loadDailyForecast() {
        val cityname = CityInfo.cityname
        val baseUrl7days = "https://api.openweathermap.org/data/2.5/forecast/daily/"

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
                    val resp = response.body()
                    for (i : Days in resp?.list!!)
                    {
                        val sdfDate = SimpleDateFormat("EEE dd, MMMM")
                        val dt = Date(i.dt* 1000)
                        val newDate = sdfDate.format(dt)

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

    private fun initRecycleView(view:View)
    {
        val recycleView = view.findViewById<RecyclerView>(R.id.recyclerviewDays)
        recycleView.layoutManager = LinearLayoutManager(this@ForecastFragment.context)
        itemRecycleView = DailyAdapter()
        recycleView.adapter = itemRecycleView
    }

}