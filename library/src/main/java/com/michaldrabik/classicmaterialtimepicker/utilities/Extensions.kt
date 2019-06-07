package com.michaldrabik.classicmaterialtimepicker.utilities

import com.michaldrabik.classicmaterialtimepicker.CmtpDialogFragment
import com.michaldrabik.classicmaterialtimepicker.OnTime12PickedListener
import com.michaldrabik.classicmaterialtimepicker.OnTime24PickedListener
import com.michaldrabik.classicmaterialtimepicker.model.CmtpTime12
import com.michaldrabik.classicmaterialtimepicker.model.CmtpTime24

fun CmtpDialogFragment.setOnTime12PickedListener(listener: (CmtpTime12) -> Unit) {
  this.setOnTime12PickedListener(object : OnTime12PickedListener {
    override fun onTimePicked(time: CmtpTime12) = listener(time)
  })
}

fun CmtpDialogFragment.setOnTime24PickedListener(listener: (CmtpTime24) -> Unit) {
  this.setOnTime24PickedListener(object : OnTime24PickedListener {
    override fun onTimePicked(time: CmtpTime24) = listener(time)
  })
}