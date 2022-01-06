package com.example.weatherapplication.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.weatherapplication.API.DailyForecastApi
import com.example.weatherapplication.API.WeatherApi
import com.example.weatherapplication.R
import com.example.weatherapplication.data.DailyForecast.DailyForecast
import com.example.weatherapplication.data.CityName
import com.example.weatherapplication.data.TodayData.WeatherResult
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class TodayFragment : Fragment() {

    private val baseUrlToday = "https://api.openweathermap.org/data/2.5/weather/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_today, container, false)
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
                        val sdfSunrise = SimpleDateFormat("HH:mm")
                        val dtn = Date("${resp?.sys?.sunrise}".toLong()* 1000)
                        val newSunrise = sdfSunrise.format(dtn)
                        sunrise.text = "$newSunrise AM"

                        //Sunset to normal time
                        val sdfSunset = SimpleDateFormat("HH:mm")
                        val dtr = Date("${resp?.sys?.sunset}".toLong()* 1000)
                        val newSunset = sdfSunset.format(dtr)
                        sunset.text = "$newSunset PM"

                        content.visibility =View.VISIBLE


                        val lat = "${resp?.coord?.lat}".toDouble()
                        val lon = "${resp?.coord?.lon}".toDouble()
                        val cityname = "${resp?.name}"
                        CityName.cityname = cityname
                        loadDailyForecast()




                    }
                }
                override fun onFailure(call: Call<WeatherResult>, t: Throwable) {
                }
            })

        }

        btnsearch.setOnClickListener{
            val city = searchfield.text.toString()
            if(city.isEmpty()) {
                Toast.makeText(activity, "Search field can't be empty!", Toast.LENGTH_SHORT).show()
            }
            else {
                getWeather(city)


                searchfield.isEnabled = false
            }
            searchfield.isEnabled = true
        }
        return view
    }


    fun loadDailyForecast() {
        val cityname = CityName.cityname
        val baseUrl7days = "https://api.openweathermap.org/data/2.5/forecast/daily/"

        val textview = view?.findViewById<TextView>(R.id.textViewNew)

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
