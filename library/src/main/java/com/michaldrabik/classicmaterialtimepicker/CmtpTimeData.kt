package com.michaldrabik.classicmaterialtimepicker

import com.michaldrabik.classicmaterialtimepicker.model.CmtpTime12

internal object CmtpTimeData {
  val HOURS_24 = (0..23)

  val HOURS_12 = (1..12)

  val MINUTES = (0..59)

  val PM_AM = CmtpTime12.PmAm.values().map { it.name }
}