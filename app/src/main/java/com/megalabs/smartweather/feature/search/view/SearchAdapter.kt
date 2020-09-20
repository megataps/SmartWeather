package com.megalabs.smartweather.feature.search.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.megalabs.smartweather.R
import com.megalabs.smartweather.model.DailyItem

interface SearchAdapterListener {
    fun onItemSelected(data: DailyItem)
}

class SearchAdapter(
    private val context: Context,
    private val listener: SearchAdapterListener?
): RecyclerView.Adapter<SearchViewHolder>()  {

    var items: List<DailyItem> = emptyList()

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            LayoutInflater.from(context).inflate(R.layout.search_item, parent, false),
            listener)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(items[position])
    }
}