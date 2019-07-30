package com.michaldrabik.classicmaterialtimepicker.utilities

import androidx.recyclerview.widget.RecyclerView

interface OnSnapPositionChangeListener {

  fun onSnapPositionChange(position: Int, rv: RecyclerView)
}