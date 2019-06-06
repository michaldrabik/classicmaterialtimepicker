package com.michaldrabik.classicmaterialtimepicker

import androidx.annotation.NonNull
import com.michaldrabik.classicmaterialtimepicker.model.CmtpTime24

interface OnTime24PickedListener {

  fun onTimePicked(@NonNull time: CmtpTime24)
}