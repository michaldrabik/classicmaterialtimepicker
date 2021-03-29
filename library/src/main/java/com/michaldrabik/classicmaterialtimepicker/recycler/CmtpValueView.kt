package com.michaldrabik.classicmaterialtimepicker.recycler

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import com.michaldrabik.classicmaterialtimepicker.R
import kotlinx.android.synthetic.main.cmtp_value_view.view.*

internal class CmtpValueView @JvmOverloads constructor(
  context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

  init {
    inflate(context, R.layout.cmtp_value_view, this)
    layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
  }

  fun bind(value: String) {
    cmtpTimeNumberValue.text = value
  }
}