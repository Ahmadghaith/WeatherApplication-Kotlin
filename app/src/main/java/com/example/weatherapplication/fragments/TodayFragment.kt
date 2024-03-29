package com.example.weatherapplication.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapplication.API.HourlyForecastApi
import com.example.weatherapplication.API.WeatherApi
import com.example.weatherapplication.Adapter.HourlyAdapter
import com.example.weatherapplication.Adapter.HourlyItems
import com.example.weatherapplication.R
import com.example.weatherapplication.data.CityInfo
import com.example.weatherapplication.data.HourlyForecast.HourlyResult
import com.example.weatherapplication.data.HourlyForecast.Hours
import com.example.weatherapplication.data.HourlyForecast.WeatherH
import com.example.weatherapplication.data.TodayForecast.WeatherResult
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class TodayFragment : Fragment() {

    //Firebase RT DB
    private lateinit var itemRef: DatabaseReference

    // Full url: https://api.openweathermap.org/data/2.5/weather?q=beirut&appid=41afd91e8508faf248e58bef14ffea2d
    private val baseUrlToday = "https://api.openweathermap.org/data/2.5/weather/"

    // Full url: https://api.openweathermap.org/data/2.5/forecast?q=beirut&appid=41afd91e8508faf248e58bef14ffea2d
    private val baseUrlHourly = "https://api.openweathermap.org/data/2.5/forecast/"

    private lateinit var itemRecycleView: HourlyAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_today, container, false)


        initRecycleView(view)

        //Saving City names to DB
        val database = Firebase.database
        itemRef = database.getReference("City name")

        val date = view.findViewById<TextView>(R.id.date)
        val temperature = view.findViewById<TextView>(R.id.temperature)
        val feelsLike = view.findViewById<TextView>(R.id.feelsLike)
        val location = view.findViewById<TextView>(R.id.location)
        val description = view.findViewById<TextView>(R.id.description)
        val imageView = view.findViewById<ImageView>(R.id.Icon)
        val minimum = view.findViewById<TextView>(R.id.minimum)
        val maximum = view.findViewById<TextView>(R.id.maximum)
        val humidity = view.findViewById<TextView>(R.id.humidity)
        val visibility = view.findViewById<TextView>(R.id.visibility)
        val wind = view.findViewById<TextView>(R.id.wind)
        val pressure = view.findViewById<TextView>(R.id.pressure)
        val sunrise = view.findViewById<TextView>(R.id.sunrise)
        val sunset = view.findViewById<TextView>(R.id.sunset)
        val searchfield = view.findViewById<EditText>(R.id.editCity)
        val btnsearch = view.findViewById<Button>(R.id.btnSearch)
        val content = view.findViewById<RelativeLayout>(R.id.content)




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
                @SuppressLint("SetTextI18n", "SimpleDateFormat")
                override fun onResponse(call: Call<WeatherResult>, response: Response<WeatherResult>) {
                    if (response.isSuccessful) {

                        val resp = response.body()

                        Picasso.get()
                            .load("https://openweathermap.org/img/w/${resp?.weather?.get(0)?.icon}.png")
                            .resize(250,250)
                            .into(imageView)

                        temperature.text = "${resp?.main?.temp}°C"
                        feelsLike.text = "Feels like ${resp?.main?.feelsLike}°C"
                        location.text = "${resp?.name}, ${resp?.sys?.country}"
                        description.text = "${resp?.weather?.get(0)?.description}"
                        minimum.text = "${resp?.main?.tempMin}°C"
                        maximum.text = "${resp?.main?.tempMax}°C"
                        wind.text = "${resp?.wind?.speed}Km/h"
                        humidity.text = "${resp?.main?.humidity}%"
                        visibility.text = "${resp?.visibility?.div(1000)}Km"
                        pressure.text = "${resp?.main?.pressure}hPa"


                        //Date to normal date
                        val sdfDate = SimpleDateFormat("EEE dd, MMMM yyyy")
                        val dt = Date("${resp?.dt}".toLong()* 1000)
                        val newDate = sdfDate.format(dt)
                        date.text = newDate

                        //Sunrise to normal time
                        val sdfSunrise = SimpleDateFormat("HH:mm a")
                        val dtn = Date("${resp?.sys?.sunrise}".toLong()* 1000)
                        val newSunrise = sdfSunrise.format(dtn)
                        sunrise.text = newSunrise

                        //Sunset to normal time
                        val sdfSunset = SimpleDateFormat("HH:mm a")
                        val dtr = Date("${resp?.sys?.sunset}".toLong()* 1000)
                        val newSunset = sdfSunset.format(dtr)
                        sunset.text = newSunset

                        content.visibility =View.VISIBLE

                        //Saving the data into the Object CityInfo
                        val cityname = "${resp?.name}"
                        val lat = resp?.coord?.lat
                        val lon = resp?.coord?.lon
                        CityInfo.cityname = cityname
                        CityInfo.lat = lat!!
                        CityInfo.lon = lon!!

                        loadHourlyForecast()

                        //Saves the city name in DB
                        itemRef.push().setValue(cityname)

                    }
                }
                override fun onFailure(call: Call<WeatherResult>, t: Throwable) {
                }
            })
        }

        //To stay on screen when coming back to the fragment
        getWeather(CityInfo.cityname)

        btnsearch.setOnClickListener{
            val city = searchfield.text.toString()
            if(city.isEmpty()) {

                //Alert Dialog
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Message")
                builder.setMessage("Search field can't be empty!")
                builder.setPositiveButton(android.R.string.yes)
                { dialog, which ->
                    Toast.makeText(activity?.applicationContext,
                        android.R.string.yes, Toast.LENGTH_SHORT).show()
                }
                builder.show()
            }
            else {
                //Show me the result
                getWeather(city)
                searchfield.isEnabled = false
            }
            searchfield.isEnabled = true
        }
        return view
    }


    fun loadHourlyForecast() {

        //Loading the city name from the CityInfo Object
        val cityname = CityInfo.cityname


        //Retrofit instance
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrlHourly)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val hourlyForecastApi = retrofit.create(HourlyForecastApi::class.java)


        //OpenWeatherApi call
        val result = hourlyForecastApi.getHourlyWeather(cityname)
        result.enqueue(object : Callback<HourlyResult> {
            @SuppressLint("SetTextI18n", "SimpleDateFormat")
            override fun onResponse(call: Call<HourlyResult>, response: Response<HourlyResult>)
            {
                if(response.isSuccessful)
                {
                    val resp = response.body()

                    for (i : Hours in resp?.list!!)
                    {
                        //Converting date
                        val sdfDate = SimpleDateFormat("HH:mm a")
                        val dt = Date(i.dt* 1000)
                        val newDate = sdfDate.format(dt)

                        //Another loop to get each icon
                        for(j : WeatherH in i.weather){
                            Picasso.get()
                                .load("https://openweathermap.org/img/w/${j.icon}.png")
                                .resize(150,150)
                                .into(object : com.squareup.picasso.Target {
                                    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                                        itemRecycleView.add(HourlyItems(newDate, bitmap!!, "${i.main.temp}°C"))
                                    }
                                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
                                    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}
                                })
                        }
                    }
                }
            }
            override fun onFailure(call: Call<HourlyResult>, t: Throwable) {
            }
        })
    }

    //Initializing Hourly forecast Recyclerview
    private fun initRecycleView(view:View)
    {
        val recycleView = view.findViewById<RecyclerView>(R.id.recyclerviewHours)
        recycleView.layoutManager = LinearLayoutManager(this@TodayFragment.context, LinearLayoutManager.HORIZONTAL, false)
        itemRecycleView = HourlyAdapter()
        recycleView.adapter = itemRecycleView
    }
}
