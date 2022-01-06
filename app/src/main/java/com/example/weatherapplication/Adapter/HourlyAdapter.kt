package com.example.weatherapplication.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapplication.R

class HourlyAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>()  {
    private var itemList: MutableList<HourlyItems> = mutableListOf()


    override fun onCreateViewHolder(parent:ViewGroup, viewType: Int): RecyclerView.ViewHolder
    {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.hourly_item, parent, false))
    }

    override fun onBindViewHolder(holder:RecyclerView.ViewHolder, position:Int)
    {
        when (holder)
        {
            is ViewHolder ->
            {
                holder.bind(itemList.asReversed()[position])
            }
        }
    }

    override fun getItemCount(): Int
    {
        return itemList.size
    }

    fun add(item: HourlyItems)
    {
        itemList.add(0, item)
        notifyItemInserted(0)
    }

    class ViewHolder constructor(itemView:View) : RecyclerView.ViewHolder(itemView)
    {
        private val hour = itemView.findViewById<TextView>(R.id.txtHour)
        private val image = itemView.findViewById<ImageView>(R.id.imageIcon)
        private val temp = itemView.findViewById<TextView>(R.id.txtTemperature)





        @SuppressLint("SetTextI18n")
        fun bind(hourlyitem: HourlyItems)
        {
            hour.text = hourlyitem.hour
            image.setImageBitmap(hourlyitem.image)
            temp.text = hourlyitem.temp
        }


    }

}