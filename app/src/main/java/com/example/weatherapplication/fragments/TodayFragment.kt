package com.example.weatherapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
            val temp = view.findViewById<TextView>(R.id.temperature)
            val location = view.findViewById<TextView>(R.id.location)
            val description = view.findViewById<TextView>(R.id.description)
            val imageView = view.findViewById<ImageView>(R.id.Icon)
            val sunrise = view.findViewById<TextView>(R.id.sunrise)
            val sunset = view.findViewById<TextView>(R.id.sunset)



            //Unix to normal time
            fun dateformater(){

            }

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
                        temp.text = "${resp?.main?.temp} °C"
                        location.text = "${resp?.name}, ${resp?.sys?.country}"
                        description.text = "${resp?.weather?.get(0)?.description}"


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
            }
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
