package com.megalabs.smartweather.feature.base

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.megalabs.smartweather.R
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.view_custom_dialog.view.*
import timber.log.Timber

abstract class BaseFragment(@LayoutRes val layoutResId: Int) : Fragment()  {

    protected var subscriptions = CompositeDisposable()
    private val progressDialog: MaterialDialog by lazy { MaterialDialog(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        subscriptions = CompositeDisposable()
        return inflater.inflate(layoutResId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressDialog.setContentView(R.layout.view_progress_dialog)

        bindEvents(view, savedInstanceState)
    }

    override fun onDestroyView() {
        subscriptions.clear()
        super.onDestroyView()
    }

    abstract fun bindEvents(rootView: View?, savedInstanceState: Bundle?)

    fun initViewModel(baseViewModel: BaseViewModel) {

        baseViewModel.bindLoadingConsumableLiveData()
            .observe(this) {
                if (it) {
                    showProgressDialog()
                } else {
                    hideProgressDialog()
                }
            }

        baseViewModel.bindErrorMessageConsumableLiveData()
            .observe(this) {
                showErrorDialog( it)
            }
    }

    protected fun showProgressDialog() {
        try {
            progressDialog.show()
        } catch (e: WindowManager.BadTokenException) {
            Timber.e(e)
        }
    }

    protected fun hideProgressDialog() {
        progressDialog.dismiss()
    }

    protected fun showErrorDialog(message: String) {
        showCustomDialog(message=message, shouldFinish=false)
    }

    protected fun showErrorDialog(@StringRes messageRes: Int) {
        showCustomDialog(message=getString(messageRes), shouldFinish=false)
    }

    protected fun showErrorDialog(@StringRes messageRes: Int, shouldFinish: Boolean) {
        showCustomDialog(message=getString(messageRes), shouldFinish=shouldFinish)
    }

    private fun showCustomDialog(title: String? = null, message: String, shouldFinish: Boolean) {
        val dialog = MaterialDialog(requireContext())
            .customView(
                R.layout.view_custom_dialog,
                scrollable = false,
                noVerticalPadding = true,
                dialogWrapContent = true)

        val customView = dialog.getCustomView()
        if (!title.isNullOrEmpty()) {
            customView.dialogTitleTextView.text = title
        }

        customView.dialogDescriptionTextView.text = message

        customView.dialogCloseButton.setOnClickListener{
            dialog.dismiss()
            if (shouldFinish) requireActivity().finish()
        }

        dialog.show {
            cornerRadius(32f)
        }
    }

    protected fun hideSoftKeyboard(view: View) {
        if (view.requestFocus()) {
            val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?

            // here is one more tricky issue
            // imm.showSoftInputMethod doesn't work well
            // and imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0) doesn't work well for all cases too
            imm?.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
        }
    }
}