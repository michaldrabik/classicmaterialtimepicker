package com.michaldrabik.classicmaterialtimepicker.model

import com.michaldrabik.classicmaterialtimepicker.CmtpTimeData

data class CmtpTime24(
  override val hour: Int,
  override val minute: Int
) : CmtpTime {

  init {
    check(hour in CmtpTimeData.HOURS_24) { "Invalid hour. Must be between 0 and 23" }
    check(minute in CmtpTimeData.MINUTES) { "Invalid minute. Must be between 0 and 59" }
  }

  override fun is12Hour() = false

  override fun toString() = String.format("%02d:%02d", hour, minute)
}