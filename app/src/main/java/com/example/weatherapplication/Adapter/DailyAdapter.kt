package com.example.weatherapplication.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapplication.R

class DailyAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private var itemList: MutableList<DailyItems> = mutableListOf()

    override fun onCreateViewHolder(parent:ViewGroup, viewType: Int): RecyclerView.ViewHolder
    {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.daily_item, parent, false))
    }

    override fun onBindViewHolder(holder:RecyclerView.ViewHolder, position:Int)
    {
        when (holder)
        {
            is ViewHolder ->
            {
                holder.bind(itemList[position])
            }
        }
    }

    override fun getItemCount(): Int
    {
        return itemList.size
    }

    fun add(item: DailyItems)
    {
        itemList.add(0, item)
        notifyItemInserted(0)
    }


    class ViewHolder constructor(itemView:View) : RecyclerView.ViewHolder(itemView)
    {
        private val date = itemView.findViewById<TextView>(R.id.txtDate)
        private val image = itemView.findViewById<ImageView>(R.id.imgIcon)
        private val highTemp = itemView.findViewById<TextView>(R.id.txtHighTemperature)
        private val lowTemp = itemView.findViewById<TextView>(R.id.txtLowTemperature)

        @SuppressLint("SetTextI18n")
        fun bind(dailyitem: DailyItems)
        {
            date.text = dailyitem.date.toString()
            image.setImageDrawable()
            highTemp.text = dailyitem.highestTemp
            lowTemp.text = dailyitem.lowestTemp
        }
    }

}