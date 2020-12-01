package com.michaldrabik.classicmaterialtimepicker.utilities

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.michaldrabik.classicmaterialtimepicker.*
import com.michaldrabik.classicmaterialtimepicker.CmtpDateData
import com.michaldrabik.classicmaterialtimepicker.model.CmtpDate
import com.michaldrabik.classicmaterialtimepicker.model.CmtpTime12
import com.michaldrabik.classicmaterialtimepicker.model.CmtpTime24
import java.util.Calendar

fun CmtpTimeDialogFragment.setOnTime12PickedListener(listener: (CmtpTime12) -> Unit) {
  this.setOnTime12PickedListener(object : OnTime12PickedListener {
    override fun onTimePicked(time: CmtpTime12) = listener(time)
  })
}

fun CmtpTimeDialogFragment.setOnTime24PickedListener(listener: (CmtpTime24) -> Unit) {
  this.setOnTime24PickedListener(object : OnTime24PickedListener {
    override fun onTimePicked(time: CmtpTime24) = listener(time)
  })
}

fun CmtpDateDialogFragment.setOnDatePickedListener(listener: (CmtpDate) -> Unit) {
  this.setOnDatePickedListener(object : OnDatePickedListener {
    override fun onDatePicked(date: CmtpDate) = listener(date)
  })
}

// Calculates the number of days in a certain month and year.
fun getNumberOfDays(month: Int, year: Int): Int {
  val calendar = Calendar.getInstance()
  calendar.isLenient = false
  calendar.set(year, month - 1, 1)
  calendar.add(Calendar.MONTH, 1)
  calendar.add(Calendar.DAY_OF_MONTH, -1)

  return calendar.get(Calendar.DAY_OF_MONTH)
}

fun getMinDay(cmtpDate: CmtpDate, minDate: Calendar?) : Int {
  if (cmtpDate.year == minDate?.get(Calendar.YEAR)) {
    if (cmtpDate.month == minDate.get(Calendar.MONTH)) {
      return minDate.get(Calendar.DAY_OF_MONTH)
    }
  }
  return 1
}

fun getMaxDay(cmtpDate: CmtpDate, maxDate: Calendar?) : Int {
  val maxNumberOfDays = getNumberOfDays(cmtpDate.month, cmtpDate.year)

  if (cmtpDate.year == maxDate?.get(Calendar.YEAR)) {
    if (cmtpDate.month == maxDate.get(Calendar.MONTH)) {
      if (maxDate.get(Calendar.DAY_OF_MONTH) <= maxNumberOfDays) {
        return maxDate.get(Calendar.DAY_OF_MONTH)
      }
    }
  }
  return maxNumberOfDays
}

fun getMinMonth(cmtpDate: CmtpDate, minDate: Calendar?) : Int {
  if (cmtpDate.year == minDate?.get(Calendar.YEAR)) {
    return minDate.get(Calendar.MONTH)
  }
  return CmtpDateData.MONTHS.first
}

fun getMaxMonth(cmtpDate: CmtpDate, maxDate: Calendar?) : Int {
  if (cmtpDate.year == maxDate?.get(Calendar.YEAR)) {
    return maxDate.get(Calendar.MONTH)
  }
  return CmtpDateData.MONTHS.last
}

fun getMinYear(minDate: Calendar?) : Int {
  minDate?.let {
    return it.get(Calendar.YEAR)
  }
  return CmtpDateData.YEARS.first
}

fun getMaxYear(maxDate: Calendar?) : Int {
  maxDate?.let {
    return it.get(Calendar.YEAR)
  }
  return CmtpDateData.YEARS.last
}

fun RecyclerView.attachSnapHelperWithListener(
  snapHelper: SnapHelper,
  behavior: SnapOnScrollListener.Behavior = SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL,
  onSnapPositionChangeListener: OnSnapPositionChangeListener
) {
  snapHelper.attachToRecyclerView(this)
  val snapOnScrollListener = SnapOnScrollListener(snapHelper, behavior, onSnapPositionChangeListener)
  addOnScrollListener(snapOnScrollListener)
}

fun SnapHelper.getSnapPosition(recyclerView: RecyclerView): Int {
  val layoutManager = recyclerView.layoutManager ?: return RecyclerView.NO_POSITION
  val snapView = findSnapView(layoutManager) ?: return RecyclerView.NO_POSITION
  return layoutManager.getPosition(snapView)
}