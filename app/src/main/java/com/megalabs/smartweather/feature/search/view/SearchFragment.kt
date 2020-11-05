package com.megalabs.smartweather.feature.search.view

import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import arrow.core.Either
import com.jakewharton.rxbinding4.view.clicks
import com.megalabs.smartweather.R
import com.megalabs.smartweather.feature.base.BaseFragment
import com.megalabs.smartweather.feature.search.viewmodel.SearchViewModel
import com.megalabs.smartweather.model.DailyItem
import io.reactivex.rxjava3.kotlin.plusAssign
import io.reactivex.rxjava3.kotlin.subscribeBy
import kotlinx.android.synthetic.main.fragment_search.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment: BaseFragment(R.layout.fragment_search), SearchAdapterListener {

    companion object {
        fun newInstance() =
            SearchFragment()
    }

    private lateinit var searchAdapter: SearchAdapter
    private val viewModel : SearchViewModel by viewModel()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        searchView.isIconified = true
        searchView.isIconified = false

        searchAdapter = SearchAdapter(requireContext(), this)
        val linearLayoutManager = LinearLayoutManager(this.context)
        searchRecyclerView.apply {
            layoutManager = linearLayoutManager
            setHasFixedSize(true)
            adapter = searchAdapter
            addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        }

        viewModel.dailyForecast?.list?.let {
            searchAdapter.items = it
            searchAdapter.notifyDataSetChanged()
        }
    }

    override fun bindEvents(rootView: View?, savedInstanceState: Bundle?) {
        initViewModel(viewModel)

        subscriptions += getWeatherButton.clicks()
            .subscribeBy(
                onError = {},
                onNext = {
                    searchView.clearFocus()
                    viewModel.dailyForecast(searchView.query.toString())
                }
            )

        viewModel.dailyForecastMutableLiveData
            .observe(this) {
                when (it) {
                    is Either.Right -> {
                        if (it.b.list.isNotEmpty()) {
                            emptyContainerView.visibility = View.GONE
                            searchRecyclerView.visibility = View.VISIBLE
                            searchAdapter.items = it.b.list
                            searchAdapter.notifyDataSetChanged()
                        } else {
                            displayItemNotFound()
                        }
                    }
                    is Either.Left -> {
                        displayItemNotFound()
                    }
                }
            }

        searchView.apply {

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
//                    // This code to prevent SearchView call twice when press enter key
//                    searchView.clearFocus()
//                    query?.let { text ->
//                        if (text.isEmpty()) {
//                            clearSearch()
//                        } else {
//                            viewModel.dailyForecast(text)
//                        }
//                    }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    newText?.let { text ->
                        if (text.isEmpty()) {
                            getWeatherButton.isEnabled = false
                            clearSearch()
                        } else {
                            getWeatherButton.isEnabled = text.length >= 3
                        }
                    }

                    return false
                }
            })
        }
    }

    private fun displayItemNotFound() {
        val errorMsg = String.format(getString(R.string.search_empty_message),
            searchView.query.toString())
        emptyDescriptionTextView.text = Html.fromHtml(errorMsg)
        emptyContainerView.visibility = View.VISIBLE
        searchRecyclerView.visibility = View.GONE

        searchView?.let {
            hideSoftKeyboard(it)
        }
    }

    fun clearSearch() {
        emptyContainerView.visibility = View.GONE
        searchAdapter.items = emptyList()
        searchAdapter.notifyDataSetChanged()
    }

    override fun onItemSelected(data: DailyItem) {

    }

    override fun onStop() {
        // hide the keyboard if has focus
        searchView?.let {
            hideSoftKeyboard(it)
        }

        super.onStop()
    }
}