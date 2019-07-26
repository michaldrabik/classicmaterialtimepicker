package com.michaldrabik.classicmaterialtimepicker

import androidx.annotation.NonNull
import com.michaldrabik.classicmaterialtimepicker.model.CmtpTime12

interface OnTime12PickedListener {

  fun onTimePicked(@NonNull time: CmtpTime12)
}