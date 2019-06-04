package com.michaldrabik.classicmaterialtimepicker.model

interface CmtpTime {
  val hour: Int
  val minute: Int

  fun is12Hour(): Boolean
}