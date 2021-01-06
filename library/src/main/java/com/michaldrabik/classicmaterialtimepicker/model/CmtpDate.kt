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
    val numberOfDaysInMonth = getNumberOfDays(month, year)
    check(day in 1..numberOfDaysInMonth) {
      String.format(
        "%s %02d %s",
        "Invalid day. Must be between 1 and",
        numberOfDaysInMonth,
        "for this month and year"
      )
    }
  }

  override fun toString() = String.format("%02d/%02d/%04d", day, month, year)

  fun toString(separator: String) = String.format("%02d$separator%02d$separator%04d", day, month, year)
}
