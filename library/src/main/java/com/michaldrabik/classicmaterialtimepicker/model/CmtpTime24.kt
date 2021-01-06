package com.michaldrabik.classicmaterialtimepicker.model

import com.michaldrabik.classicmaterialtimepicker.CmtpTimeData

data class CmtpTime24(
  override val hour: Int,
  override val minute: Int
) : CmtpTime {

  companion object {
    @JvmStatic
    val DEFAULT = CmtpTime24(12, 30)
  }

  init {
    check(hour in CmtpTimeData.HOURS_24) { "Invalid hour. Must be between 0 and 23" }
    check(minute in CmtpTimeData.MINUTES) { "Invalid minute. Must be between 0 and 59" }
  }

  override fun getType() = CmtpTimeType.HOUR_24

  override fun toString() = String.format("%02d:%02d", hour, minute)

  fun toString(separator: String) = String.format("%02d$separator%02d", hour, minute)
}