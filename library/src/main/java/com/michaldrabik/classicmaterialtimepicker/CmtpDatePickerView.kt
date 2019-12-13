package com.michaldrabik.classicmaterialtimepicker

import android.content.Context
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.michaldrabik.classicmaterialtimepicker.model.CmtpDate
import com.michaldrabik.classicmaterialtimepicker.recycler.CmtpValuesAdapter
import com.michaldrabik.classicmaterialtimepicker.utilities.*
import kotlinx.android.synthetic.main.cmtp_datepicker_view.view.*
import java.util.*

open class CmtpDatePickerView @JvmOverloads constructor(
  context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), OnSnapPositionChangeListener {

  private val recyclerYearsAdapter by lazy { CmtpValuesAdapter() }
  private val recyclerMonthsAdapter by lazy { CmtpValuesAdapter() }
  private val recyclerDaysAdapter by lazy { CmtpValuesAdapter() }

  private val recyclerYearsLayoutManager by lazy { LinearLayoutManager(context, VERTICAL, false) }
  private val recyclerMonthsLayoutManager by lazy { LinearLayoutManager(context, VERTICAL, false) }
  private val recyclerDaysLayoutManager by lazy { LinearLayoutManager(context, VERTICAL, false) }

  private val recyclerYearsSnapHelper by lazy { LinearSnapHelper() }
  private val recyclerMonthsSnapHelper by lazy { LinearSnapHelper() }
  private val recyclerDaysSnapHelper by lazy { LinearSnapHelper() }

  private var date: CmtpDate = CmtpDate.DEFAULT
  private var maxDate: Calendar? = null
  private var minDate: Calendar? = null

  init {
    inflate(
      ContextThemeWrapper(context, R.style.CmtpViewTheme),
      R.layout.cmtp_datepicker_view,
      this
    )
    layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
    setupRecyclers()
  }

  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    super.onSizeChanged(w, h, oldw, oldh)
    val padding = measuredHeight / 2
    cmtpRecyclerDays.setPadding(0, padding, 0, padding)
    cmtpRecyclerMonths.setPadding(0, padding, 0, padding)
    cmtpRecyclerYears.setPadding(0, padding, 0, padding)
  }

  private fun setupRecyclers() {
    cmtpRecyclerDays.apply {
      setHasFixedSize(true)
      adapter = recyclerDaysAdapter
      layoutManager = recyclerDaysLayoutManager
      recyclerDaysSnapHelper.attachToRecyclerView(this)
    }
    cmtpRecyclerMonths.apply {
      setHasFixedSize(true)
      adapter = recyclerMonthsAdapter
      layoutManager = recyclerMonthsLayoutManager
      recyclerMonthsSnapHelper.attachToRecyclerView(this)
    }
    cmtpRecyclerYears.apply {
      setHasFixedSize(true)
      adapter = recyclerYearsAdapter
      layoutManager = recyclerYearsLayoutManager
      recyclerYearsSnapHelper.attachToRecyclerView(this)
    }

    setupRecyclersData()
  }

  private fun setupRecyclersData() {
    cmtpRecyclerYears.apply {
      val years = CmtpDateData.YEARS
      recyclerYearsAdapter.setItems(years.map { String.format("%04d", it) })
      recyclerYearsLayoutManager.scrollToPosition(years.indexOf(date.year))
      smoothScrollBy(0, 1)

      attachSnapHelperWithListener(
        recyclerYearsSnapHelper,
        SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL_STATE_IDLE,
        this@CmtpDatePickerView
      )
    }

    setUpYearsRecyclerBasedOnMinimumAndMaximumDate(date)


    cmtpRecyclerMonths.apply {
      val months = CmtpDateData.MONTHS
      recyclerMonthsAdapter.setItems(months.map { String.format("%02d", it) })
      recyclerMonthsLayoutManager.scrollToPosition(months.indexOf(date.month))
      smoothScrollBy(0, 1)

      attachSnapHelperWithListener(
        recyclerMonthsSnapHelper,
        SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL_STATE_IDLE,
        this@CmtpDatePickerView
      )
    }

    setUpMonthsRecyclerBasedOnMinimumAndMaximumDate(date)


    // Days RV is setup after month and year because it's necessary to check how many days that specific month has.
    // Also, force scroll is true because it's necessary to adjust the initial position.
    setUpDaysRecyclerBasedOnMinimumAndMaximumDate(date)

    cmtpRecyclerDays.attachSnapHelperWithListener(
      recyclerDaysSnapHelper,
      SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL_STATE_IDLE,
      this@CmtpDatePickerView
    )
  }

  private fun setUpDaysRecyclerBasedOnMinimumAndMaximumDate(cmtpDate: CmtpDate) {
    cmtpRecyclerDays.apply {
      val minDay = getMinDay(cmtpDate, minDate)
      val maxDay = getMaxDay(cmtpDate, maxDate)

      if (maxDay-minDay+1 != recyclerDaysAdapter.itemCount) {
        if (cmtpDate.day < minDay) {
          date = CmtpDate(minDay, cmtpDate.month, cmtpDate.year)
        }
        if (cmtpDate.day > maxDay) {
          date = CmtpDate(maxDay, cmtpDate.month, cmtpDate.year)
        }

        val days = (minDay..maxDay)
        recyclerDaysAdapter.setItems(days.map { String.format("%02d", it) })
        recyclerDaysAdapter.notifyDataSetChanged()
        recyclerDaysLayoutManager.scrollToPosition(days.indexOf(date.day))
        smoothScrollBy(0, 1)
      }
    }
  }

  private fun setUpMonthsRecyclerBasedOnMinimumAndMaximumDate(cmtpDate: CmtpDate) {
    cmtpRecyclerMonths.apply {
      val minMonth = getMinMonth(cmtpDate, minDate)
      val maxMonth = getMaxMonth(cmtpDate, maxDate)

      if (maxMonth-minMonth+1 != recyclerMonthsAdapter.itemCount) {
        if (cmtpDate.month < minMonth) {
          date = CmtpDate(cmtpDate.day, minMonth, cmtpDate.year)
        }
        if (cmtpDate.month > maxMonth) {
          date = CmtpDate(cmtpDate.day, maxMonth, cmtpDate.year)
        }

        val months = (minMonth..maxMonth)
        recyclerMonthsAdapter.setItems(months.map { String.format("%02d", it) })
        recyclerMonthsAdapter.notifyDataSetChanged()
        recyclerMonthsLayoutManager.scrollToPosition(months.indexOf(date.month))
        smoothScrollBy(0, 1)
      }
    }
  }

  private fun setUpYearsRecyclerBasedOnMinimumAndMaximumDate(cmtpDate: CmtpDate) {
    cmtpRecyclerYears.apply {
      val minYear = getMinYear(minDate)
      val maxYear = getMaxYear(maxDate)

      if (minYear != CmtpDateData.YEARS.first || maxYear != CmtpDateData.YEARS.last) {
        if (cmtpDate.year < minYear) {
          date = CmtpDate(cmtpDate.day, cmtpDate.month, minYear)
        }
        if (cmtpDate.year > maxYear) {
          date = CmtpDate(cmtpDate.day, cmtpDate.month, maxYear)
        }

        val years = (minYear..maxYear)
        recyclerYearsAdapter.setItems(years.map { String.format("%04d", it) })
        recyclerYearsAdapter.notifyDataSetChanged()
        recyclerYearsLayoutManager.scrollToPosition(years.indexOf(date.year))
        smoothScrollBy(0, 1)
      }
    }
  }

  override fun onSnapPositionChange(position: Int, rv: RecyclerView) {
    date = getDate()

    if (rv == cmtpRecyclerYears) {
      setUpMonthsRecyclerBasedOnMinimumAndMaximumDate(date)
    }
    if (rv != cmtpRecyclerDays) {
      setUpDaysRecyclerBasedOnMinimumAndMaximumDate(date)
    }
  }

  /**
   * Set initial date on Pickerview
   */
  fun setDate(initialDate: CmtpDate) {
    date = initialDate
    setupRecyclersData()
  }

  /**
   * Set custom separator on Pickerview
   */
  internal fun setCustomSeparator(separator: String) {
    day_month_separator.text = separator
    month_year_separator.text = separator
  }

  /**
   * Set custom year range for PickerView (public method) - the custom year range will be disregarded if minDate or maxDate are set
   * @param startingYear year defining the beginning of the custom range.
   * @param endingYear year defining the end of the custom range.
   * @throws IllegalStateException when given an endingYear smaller than startingYear.
   */
  fun setCustomYearRange(startingYear: Int, endingYear: Int) {
    check(startingYear <= endingYear) { "The starting year must be equal to/smaller than endingYear" }
    val yearRange = startingYear..endingYear
    setCustomYearRange(yearRange)
  }

  /**
   * Set custom year range for PickerView
   */
  internal fun setCustomYearRange(customYearRange: IntRange) {
    if (this.minDate == null && this.maxDate == null) {
      cmtpRecyclerYears.apply {
        recyclerYearsAdapter.setItems(customYearRange.map { String.format("%04d", it) })
        recyclerYearsLayoutManager.scrollToPosition(customYearRange.indexOf(date.year))
        smoothScrollBy(0, 1)

        attachSnapHelperWithListener(
          recyclerYearsSnapHelper,
          SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL_STATE_IDLE,
          this@CmtpDatePickerView
        )
      }
    }
  }

  /**
   * Set minimum date for PickerView. Datepicker will ignore CustomYearRange if this is used
   * @param minDate Date containing minimum day, month and year for the date picker.
   */
  fun setMinimumDate(minDate: Calendar) {
    this.minDate = minDate
  }

  /**
   * Set minimum date for date picker. Datepicker will ignore CustomYearRange if this is used
   * @param day Int containing minimum day for the date picker.
   * @param month Int containing minimum month for the date picker.
   * @param year Int containing minimum year for the date picker.
   * @throws IllegalStateException when given day or month are out of valid range.
   */
  fun setMinimumDate(day: Int, month: Int, year: Int) {
    check(month in 1..12) {"Month must be between 1 and 12"}
    check(day in 1..getNumberOfDays(month, year)) {"Day is invalid for this month and year"}
    val calendar = Calendar.getInstance()
    calendar.set(year, month, day)

    setMinimumDate(calendar)
  }

  /**
   * Set maximum date for PickerView. Datepicker will ignore CustomYearRange if this is used
   * @param maxDate Date containing maximum day, month and year for the date picker.
   * */
  fun setMaximumDate(maxDate: Calendar) {
    this.maxDate = maxDate
  }

  /**
   * Set maximum date for date picker. Datepicker will ignore CustomYearRange if this is used
   * @param day Int containing minimum day for the date picker.
   * @param month Int containing minimum month for the date picker.
   * @param year Int containing minimum year for the date picker.
   * @throws IllegalStateException when given day or month are out of valid range.
   */
  fun setMaximumDate(day: Int, month: Int, year: Int) {
    check(month in 1..12) {"Month must be between 1 and 12"}
    check(day in 1..getNumberOfDays(month, year)) {"Day is invalid for this month and year"}
    val calendar = Calendar.getInstance()
    calendar.set(year, month, day)

    setMaximumDate(calendar)
  }

  /**
   * Get selected date containing day, month and year.
   * @throws IllegalStateException will be thrown if CmtpTimePickerView has been initialised with 12-Hour format.
   */
  fun getDate(): CmtpDate {
    val dayView = recyclerDaysSnapHelper.findSnapView(recyclerDaysLayoutManager)
    val monthView = recyclerMonthsSnapHelper.findSnapView(recyclerMonthsLayoutManager)
    val yearView = recyclerYearsSnapHelper.findSnapView(recyclerYearsLayoutManager)

    if (dayView == null || monthView == null || yearView == null) {
      throw java.lang.IllegalStateException("DatePicker view has not been initialized yet.")
    }

    var dayIndex = recyclerDaysLayoutManager.getPosition(dayView)
    var monthIndex = recyclerMonthsLayoutManager.getPosition(monthView)
    val yearIndex = recyclerYearsLayoutManager.getPosition(yearView)

    val year = recyclerYearsAdapter.getItems()[yearIndex].toInt()

    val maxMonthIndex = recyclerMonthsAdapter.itemCount-1
    if (monthIndex > maxMonthIndex) {
      monthIndex = maxMonthIndex
    }
    val month = recyclerMonthsAdapter.getItems()[monthIndex].toInt()


    val maxDayIndex = recyclerDaysAdapter.itemCount-1
    if (dayIndex > maxDayIndex) {
      dayIndex = maxDayIndex
    }
    if (dayIndex >= getNumberOfDays(month, year)) {
      dayIndex = getNumberOfDays(month, year) -1
    }
    val day = recyclerDaysAdapter.getItems()[dayIndex].toInt()

    return CmtpDate(
      day,
      month,
      year
    )
  }
}