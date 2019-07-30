package com.michaldrabik.classicmaterialtimepicker.model

import com.michaldrabik.classicmaterialtimepicker.CmtpDateData
import com.michaldrabik.classicmaterialtimepicker.utilities.getNumberOfDays
import java.util.*

data class CmtpDate(
  val day: Int,
  val month: Int,
  val year: Int
) {

  companion object {
    @JvmStatic
    val DEFAULT = CmtpDate(1,1, 2019
    )
  }

  /**
   * Alternative constructor using a calendar
   * @param calendar object with the desired date to be assigned
   */
  constructor(calendar: Calendar) : this(
    calendar.get(Calendar.DAY_OF_MONTH),
    calendar.get(Calendar.MONTH),
    calendar.get(Calendar.YEAR))

  init {
    check(month in CmtpDateData.MONTHS) { "Invalid month. Must be between 1 and 12" }
    val numberOfDaysInMonth = getNumberOfDays(this)
    check(day in 1..numberOfDaysInMonth) { String.format("%s %02d %s", "Invalid day. Must be between 1 and", numberOfDaysInMonth, "for this month and year") }
  }

  override fun toString() = String.format("%02d/%02d/%04d", day, month, year)
}
