package com.megalabs.smartweather.feature.base

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.megalabs.smartweather.R
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

abstract class BaseViewModel(
    private val context: Context
) : ViewModel() {

    private val disposables by lazy { CompositeDisposable() }
    // TODO: Check rotation
    private val loadingConsumableLiveData by lazy { MutableLiveData<Boolean>() }
    private val errorMessageConsumableLiveData by lazy { MutableLiveData<String>() }

    fun bindLoadingConsumableLiveData(): MutableLiveData<Boolean> = loadingConsumableLiveData
    fun bindErrorMessageConsumableLiveData(): MutableLiveData<String> = errorMessageConsumableLiveData

    fun addToDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }

    protected fun showLoading() {
        loadingConsumableLiveData.postValue(true)
    }

    protected fun hideLoading() {
        loadingConsumableLiveData.postValue(false)
    }

    private fun showError(errorMessage: String) {
        errorMessageConsumableLiveData.postValue(errorMessage)
    }

    protected open fun onErrorHandler(error: Throwable) {
        error.message.let { errorMessage ->
            when {
                errorMessage.isNullOrEmpty() -> showError(context.getString(R.string.common_error_default))
                else -> showError(errorMessage)
            }
        }
    }
}