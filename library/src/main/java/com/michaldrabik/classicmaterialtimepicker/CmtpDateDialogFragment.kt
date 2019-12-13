package com.michaldrabik.classicmaterialtimepicker

import android.app.Dialog
import android.os.Bundle
import android.view.WindowManager.LayoutParams.WRAP_CONTENT
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.michaldrabik.classicmaterialtimepicker.model.CmtpDate
import com.michaldrabik.classicmaterialtimepicker.utilities.getNumberOfDays
import java.util.*
import java.util.Calendar.DAY_OF_MONTH
import java.util.Calendar.MONTH
import java.util.Calendar.YEAR

class CmtpDateDialogFragment : DialogFragment() {

  companion object {
    private const val ARG_POSITIVE_BUTTON_TEXT = "ARG_POSITIVE_BUTTON_TEXT"
    private const val ARG_NEGATIVE_BUTTON_TEXT = "ARG_NEGATIVE_BUTTON_TEXT"
    private const val ARG_DAY = "ARG_DAY"
    private const val ARG_MONTH = "ARG_MONTH"
    private const val ARG_YEAR = "ARG_YEAR"
    private const val ARG_SEPARATOR = "ARG_SEPARATOR"

    /**
     * Create new instance of CmtpDateDialogFragment with CmtpDatePickerView embedded.
     * @param positiveButtonText Custom positive button text. "OK" by default.
     * @param negativeButtonText Custom negative button text. "CANCEL" by default.
     */
    @JvmOverloads
    @JvmStatic
    fun newInstance(
      positiveButtonText: String = "OK",
      negativeButtonText: String = "Cancel"
    ) = CmtpDateDialogFragment().apply {
      arguments = Bundle().apply {
        putString(ARG_POSITIVE_BUTTON_TEXT, positiveButtonText)
        putString(ARG_NEGATIVE_BUTTON_TEXT, negativeButtonText)
      }
    }
  }

  private lateinit var date: CmtpDate
  private lateinit var customYearRange: IntRange
  private lateinit var customDateSeparator: String
  private lateinit var customMinDate: Calendar
  private lateinit var customMaxDate: Calendar
  private lateinit var datePicker: CmtpDatePickerView

  private lateinit var onDatePickedListener: OnDatePickedListener

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val context = requireContext()

    datePicker = CmtpDatePickerView(context)
    savedInstanceState?.let { restoreState(it) }
    if (this::customMinDate.isInitialized) datePicker.setMinimumDate(customMinDate)
    if (this::customMaxDate.isInitialized) datePicker.setMaximumDate(customMaxDate)
    if (this::customYearRange.isInitialized) datePicker.setCustomYearRange(customYearRange)
    if (this::date.isInitialized) datePicker.setDate(date)
    if (this::customDateSeparator.isInitialized) datePicker.setCustomSeparator(customDateSeparator)

    val dialogBuilder = AlertDialog.Builder(context, R.style.CmtpDialogFrameStyle)
    dialogBuilder.setView(datePicker)

    dialogBuilder.setPositiveButton(
      arguments?.getString(ARG_POSITIVE_BUTTON_TEXT)
    ) { _, _ -> onDatePicked() }

    dialogBuilder.setNegativeButton(
      arguments?.getString(ARG_NEGATIVE_BUTTON_TEXT)
    ) { _, _ -> }

    return dialogBuilder.create()
  }

  private fun saveState(outState: Bundle) {
    if (!this::datePicker.isInitialized) return
    outState.putInt(ARG_DAY, datePicker.getDate().day)
    outState.putInt(ARG_MONTH, datePicker.getDate().month)
    outState.putInt(ARG_YEAR, datePicker.getDate().year)

    outState.putString(ARG_SEPARATOR, customDateSeparator)
  }

  private fun restoreState(stateBundle: Bundle) {
    val day = stateBundle.getInt(ARG_DAY, CmtpDate.DEFAULT.day)
    val month = stateBundle.getInt(ARG_MONTH, CmtpDate.DEFAULT.month)
    val year = stateBundle.getInt(ARG_YEAR, CmtpDate.DEFAULT.year)

    date = CmtpDate(day, month, year)

    customDateSeparator = stateBundle.getString(ARG_SEPARATOR, getString(R.string.cmtp_default_separator))
  }

  override fun onResume() {
    super.onResume()
    dialog.window?.let {
      val width = resources.getDimensionPixelSize(R.dimen.cmtp_timepicker_width)
      it.setLayout(width, WRAP_CONTENT)
    }
  }

  private fun onDatePicked() {
    if (this::onDatePickedListener.isInitialized) {
      onDatePickedListener.onDatePicked(datePicker.getDate())
    }
  }

  /**
   * Set initial date and initialize picker.
   * @param day from 1 to 31 (depending on month).
   * @param month from 1 to 12.
   * @param year from currentYear-100 to currentYear+100 (default).
   * @throws IllegalStateException when given day, month or year is out of valid range.
   */
  fun setInitialDate(day: Int, month: Int, year: Int) {
    date = CmtpDate(day, month, year)
  }

  /**
   * Extract initial date out of Calendar instance and initialize picker.
   * @param calendar Java Calendar instance.
   * @throws IllegalStateException when given day, month or year is out of valid range.
   */
  fun setInitialDate(calendar: Calendar) {
    date = CmtpDate(
      calendar.get(DAY_OF_MONTH),
      calendar.get(MONTH),
      calendar.get(YEAR)
    )
  }

  /**
   * Set custom range for years - the custom year range will be disregarded if minDate or maxDate are set.
   * @param startingYear year defining the beginning of the custom range.
   * @param endingYear year defining the end of the custom range.
   * @throws IllegalStateException when given an endingYear smaller than startingYear.
   */
  fun setCustomYearRange(startingYear: Int, endingYear: Int) {
    check(startingYear <= endingYear) { "The starting year must be equal to/smaller than endingYear" }
    customYearRange = (startingYear..endingYear)
  }

  /**
   * Set minimum date for date picker. Datepicker will ignore CustomYearRange if this is used
   * @param date Date containing minimum day, month and year for the date picker.
   * @throws IllegalStateException when minimum date is bigger than maximum date.
   */
  fun setMinimumDate(date: Date) {
    if (this::customMaxDate.isInitialized) {
      check(customMaxDate.time >= date) {"Minimum date must be smaller than minimum date"}
    }

    val calendar = Calendar.getInstance()
    calendar.time = date

    customMinDate = calendar

    if (!this::date.isInitialized) {
      setInitialDate(calendar)
    }
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

    setMinimumDate(calendar.time)
  }

  /**
   * Set maximum date for date picker. Datepicker will ignore CustomYearRange if this is used
   * @param date Date containing maximum day, month and year for the date picker.
   * @throws IllegalStateException when maximum date is smaller than maximum date.
   */
  fun setMaximumDate(date: Date) {
    if (this::customMinDate.isInitialized) {
      check(date >= customMinDate.time) {"Maximum date must be bigger than minimum date"}
    }

    val calendar = Calendar.getInstance()
    calendar.time = date

    customMaxDate = calendar
  }

  /**
   * Set maximum date for date picker. Datepicker will ignore CustomYearRange if this is used
   * @param day Int containing maximum day for the date picker.
   * @param month Int containing maximum month for the date picker.
   * @param year Int containing maximum year for the date picker.
   * @throws IllegalStateException when given day or month are out of valid range.
   */
  fun setMaximumDate(day: Int, month: Int, year: Int) {
    check(month in 1..12) {"Month must be between 1 and 12"}
    check(day in 1..getNumberOfDays(month, year)) {"Day is invalid for this month and year"}
    val calendar = Calendar.getInstance()
    calendar.set(year, month, day)

    setMaximumDate(calendar.time)
  }

  /**
   * Set custom separator character for dates.
   * @param separator separator to be used on date picker.
   */
  fun setCustomSeparator(separator: String) {
    customDateSeparator = separator
  }

  /**
   * Set date picked listener.
   */
  fun setOnDatePickedListener(listener: OnDatePickedListener) {
    this.onDatePickedListener = listener
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    saveState(outState)
  }
}