package com.example.weatherapplication.fragments

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContextCompat
import com.example.weatherapplication.API.WeatherApi
import com.example.weatherapplication.R
import com.example.weatherapplication.data.WeatherResult
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class TodayFragment : Fragment() {

    private val baseUrl = "https://api.openweathermap.org/data/2.5/weather/"
    val apiKey = "41afd91e8508faf248e58bef14ffea2d"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_today, container, false)


        fun getWeather(city : String){
            val date = view.findViewById<TextView>(R.id.date)
            val temperature = view.findViewById<TextView>(R.id.temperature)
            val location = view.findViewById<TextView>(R.id.location)
            val description = view.findViewById<TextView>(R.id.description)
            val imageView = view.findViewById<ImageView>(R.id.Icon)
            val humidity = view.findViewById<TextView>(R.id.humidity)
            val visibility = view.findViewById<TextView>(R.id.visibility)
            val wind = view.findViewById<TextView>(R.id.wind)
            val pressure = view.findViewById<TextView>(R.id.pressure)
            val sunrise = view.findViewById<TextView>(R.id.sunrise)
            val sunset = view.findViewById<TextView>(R.id.sunset)





            //Retrofit instance
            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val weatherApi = retrofit.create(WeatherApi::class.java)

            //OpenWeatherApi call
            val result = weatherApi.getWeatherByCity(city)
            result.enqueue(object : Callback<WeatherResult> {
                override fun onResponse(call: Call<WeatherResult>, response: Response<WeatherResult>) {
                    if (response.isSuccessful) {

                        val resp = response.body()

                        Picasso.get()
                            .load("https://openweathermap.org/img/w/${resp?.weather?.get(0)?.icon}.png")
                            .into(imageView)
                        temperature.text = "${resp?.main?.temp}°C"
                        location.text = "${resp?.name}, ${resp?.sys?.country}"
                        description.text = "${resp?.weather?.get(0)?.description}"
                        wind.text = "${resp?.wind?.speed}Km/h"
                        humidity.text = "${resp?.main?.humidity}%"
                        visibility.text = "${resp?.visibility?.div(1000)}Km"
                        pressure.text = "${resp?.main?.pressure}hPa"

                        //Unix to normal time

                            val sdf = SimpleDateFormat("dd-MM-yyyy")
                            val dt = Date("${resp?.dt}".toLong()* 1000)
                            val newdate = sdf.format(dt)
                        date.text = newdate

                        val sdfn = SimpleDateFormat("HH:mm")
                        val dtn = Date("${resp?.sys?.sunrise}".toLong()* 1000)
                        val newdaten = sdfn.format(dtn)
                        sunrise.text = "${newdaten} AM"

                        val sdfr = SimpleDateFormat("HH:mm")
                        val dtr = Date("${resp?.sys?.sunset}".toLong()* 1000)
                        val newdater = sdfr.format(dtr)
                        sunset.text = "${newdater} PM"





                    }
                }

                override fun onFailure(call: Call<WeatherResult>, t: Throwable) {

                }
            })

        }

        val searchfield = view.findViewById<EditText>(R.id.editCity)
        val btnsearch = view.findViewById<Button>(R.id.btnSearch)

        btnsearch.setOnClickListener{
            val city = searchfield.text.toString()
            if(city.isEmpty()) {
                Toast.makeText(activity, "Search field can't be empty!", Toast.LENGTH_SHORT).show()
            }
            else {
                getWeather(city)
                searchfield.isEnabled = false;

            }
            searchfield.isEnabled = true;

        }

        return view
    }



    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
    }



}
