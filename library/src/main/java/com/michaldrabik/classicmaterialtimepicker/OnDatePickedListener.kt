package com.michaldrabik.classicmaterialtimepicker

import androidx.annotation.NonNull
import com.michaldrabik.classicmaterialtimepicker.model.CmtpDate
import com.michaldrabik.classicmaterialtimepicker.model.CmtpTime24

interface OnDatePickedListener {

  fun onDatePicked(@NonNull date: CmtpDate)
}