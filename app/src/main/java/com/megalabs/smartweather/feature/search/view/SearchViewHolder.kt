package com.megalabs.smartweather.feature.search.view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.megalabs.smartweather.R
import com.megalabs.smartweather.model.DailyItem
import com.megalabs.smartweather.model.Temp
import kotlinx.android.synthetic.main.search_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class SearchViewHolder(
    private val view: View,
    private val listener: SearchAdapterListener?
) : RecyclerView.ViewHolder(view), View.OnClickListener {

    init {
        view.setOnClickListener(this)
    }

    // TODO: Should display based on timezone and locale of the searched city
    fun bind(data: DailyItem) {

        view.tag = data
        view.dateTextView.text = String.format(view.context.getString(R.string.search_date), getDisplayDate(data.dt))
        view.avgTemperatureTextView.text = String.format(view.context.getString(R.string.search_average_temperature), getAverageTemperature(data.temp))
        view.pressureTextView.text = String.format(view.context.getString(R.string.search_pressure), data.pressure)
        view.humidityTextView.text =
            String.format(view.context.getString(R.string.search_humidity), data.humidity) + "%"
        view.descriptionTextView.text = String.format(view.context.getString(R.string.search_description), data.weather.firstOrNull()?.description ?: "")
    }

    private fun getDisplayDate(timestamp: Long): String {
        val calendar = Calendar.getInstance(Locale.getDefault())
        calendar.timeInMillis = timestamp*1000
        val sdf = SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault())
        return sdf.format(calendar.time)
    }

    private fun getAverageTemperature(temp: Temp): String {
        return String.format("%.2f", ((temp.max + temp.min)/2)-273.15)
    }

    override fun onClick(v: View) {
        val data: DailyItem = v.tag as DailyItem
        listener?.onItemSelected(data)
    }
}