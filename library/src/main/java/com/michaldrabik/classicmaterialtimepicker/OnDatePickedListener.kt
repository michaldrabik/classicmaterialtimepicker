package com.michaldrabik.classicmaterialtimepicker

import androidx.annotation.NonNull
import com.michaldrabik.classicmaterialtimepicker.model.CmtpDate

interface OnDatePickedListener {

  fun onDatePicked(@NonNull date: CmtpDate)
}