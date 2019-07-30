package com.michaldrabik.classicmaterialtimepicker.model

import com.michaldrabik.classicmaterialtimepicker.CmtpDateData
import com.michaldrabik.classicmaterialtimepicker.utilities.getNumberOfDays

data class CmtpDate(
  val day: Int,
  val month: Int,
  val year: Int
) {

  companion object {
    @JvmStatic
    val DEFAULT = CmtpDate(1, 1, 2019)
  }

  init {
    check(month in CmtpDateData.MONTHS) { "Invalid month. Must be between 1 and 12" }
    val daysInMonth = getNumberOfDays(this)
    check(day in 1..daysInMonth) {
      String.format("%s %02d %s", "Invalid day. Must be between 1 and", daysInMonth, "for this month and year")
    }
  }

  override fun toString() = String.format("%02d/%02d/%04d", day, month, year)
}
