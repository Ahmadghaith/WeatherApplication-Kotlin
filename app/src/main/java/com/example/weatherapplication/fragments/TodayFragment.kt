package com.example.weatherapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import com.example.weatherapplication.API.WeatherApi
import com.example.weatherapplication.R
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

        val search = view.findViewById<SearchView>(R.id.search)
        val temp = view.findViewById<TextView>(R.id.temperature)
        val location = view.findViewById<TextView>(R.id.location)
        val description = view.findViewById<TextView>(R.id.description)
        val imageView = view.findViewById<ImageView>(R.id.Icon)


        GlobalScope.launch(Dispatchers.Main){
        //Retrofit instance
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val weatherApi = retrofit.create(WeatherApi::class.java)

        //OpenWeatherApi call
        val result = weatherApi.getWeatherByCity()
        result.enqueue(object : Callback<JsonObject>
        {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if(response.isSuccessful) {
                    val result = response.body()
                    val main = result?.get("main")?.asJsonObject
                    val sys = result?.get("sys")?.asJsonObject
                    val weather = result?.get("weather")?.asJsonArray
                    val icon = weather?.get(0)?.asJsonObject?.get("icon")?.asString
                    val city = result?.get("name")?.asString
                    val country = sys?.get("country")?.asString
                    val tmp = main?.get("temp")?.asDouble
                    temp.text = "$tmp °C"
                    location.text = "$city, $country"
                    Picasso.get().load("https://openweathermap.org/img/w/$icon.png").into(imageView)


                }
            }
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

            }
        })}

        return view
    }
}