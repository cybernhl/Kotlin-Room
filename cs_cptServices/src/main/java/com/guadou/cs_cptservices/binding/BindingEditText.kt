package com.guadou.cs_cptservices.binding

import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.KeyEvent
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.guadou.lib_baselib.utils.ETMoneyValueFilter
import com.guadou.lib_baselib.utils.KeyboardUtils

/**
 * EditText的简单监听事件
 */
@BindingAdapter("onTextChanged")
fun EditText.onTextChanged(action: (String) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            action(s.toString())
        }
    })
}

var _viewClickFlag = false
var _clickRunnable = Runnable { _viewClickFlag = false }

/**
 * Edit的确认按键事件
 */
@BindingAdapter("onKeyEnter")
fun EditText.onKeyEnter(action: () -> Unit) {
    setOnKeyListener { _, keyCode, _ ->
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            KeyboardUtils.closeSoftKeyboard(this)

            if (!_viewClickFlag) {
                _viewClickFlag = true
                action()
            }
            removeCallbacks(_clickRunnable)
            postDelayed(_clickRunnable, 1000)
        }
        return@setOnKeyListener false
    }
}

/**
 * Edit的失去焦点监听
 */
@BindingAdapter("onFocusLose")
fun EditText.onFocusLose(action: (textView: TextView) -> Unit) {
    setOnFocusChangeListener { _, hasFocus ->
        if (!hasFocus) {
            action(this)
        }
    }
}

/**
 * Edit的得到焦点监听
 */
@BindingAdapter("onFocusGet")
fun EditText.onFocusGet(action: (textView: TextView) -> Unit) {
    setOnFocusChangeListener { _, hasFocus ->
        if (hasFocus) {
            action(this)
        }
    }
}

/**
 * 设置ET智能小数点2位
 */
@BindingAdapter("setDecimalPoints")
fun setDecimalPoints(editText: EditText, num: Int) {
    editText.filters = arrayOf<InputFilter>(ETMoneyValueFilter(num))
}
