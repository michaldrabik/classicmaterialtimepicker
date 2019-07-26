package com.michaldrabik.classicmaterialtimepicker.model

import java.util.*

data class CmtpDate(
  val day: Int,
  val month: Int,
  val year: Int
) {

  companion object {
    @JvmStatic
    val calendar: Calendar = Calendar.getInstance()
    val DEFAULT = CmtpDate(
      calendar.get(Calendar.DAY_OF_MONTH),
      calendar.get(Calendar.MONTH),
      calendar.get(Calendar.YEAR)
    )
  }

  override fun toString() = String.format("%02d/%02d/%04d", day, month, year)
}
