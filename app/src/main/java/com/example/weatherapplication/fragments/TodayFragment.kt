package com.example.weatherapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import com.example.weatherapplication.R
import retrofit2.Retrofit

class TodayFragment : Fragment() {

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

        //Retrofit instance
        /*val rertofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/weather")
            .build()
*/
        return view
    }
}