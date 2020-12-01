package com.michaldrabik.classicmaterialtimepicker

import android.app.Dialog
import android.os.Bundle
import android.view.WindowManager.LayoutParams.WRAP_CONTENT
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.michaldrabik.classicmaterialtimepicker.model.CmtpTime
import com.michaldrabik.classicmaterialtimepicker.model.CmtpTime12
import com.michaldrabik.classicmaterialtimepicker.model.CmtpTime24
import com.michaldrabik.classicmaterialtimepicker.model.CmtpTimeType
import com.michaldrabik.classicmaterialtimepicker.model.CmtpTimeType.HOUR_12
import com.michaldrabik.classicmaterialtimepicker.model.CmtpTimeType.HOUR_24

class CmtpTimeDialogFragment : DialogFragment() {

  companion object {
    private const val ARG_POSITIVE_BUTTON_TEXT = "ARG_POSITIVE_BUTTON_TEXT"
    private const val ARG_NEGATIVE_BUTTON_TEXT = "ARG_NEGATIVE_BUTTON_TEXT"
    private const val ARG_HOUR = "ARG_HOUR"
    private const val ARG_MINUTE = "ARG_MINUTE"
    private const val ARG_PM_AM = "ARG_PM_AM"
    private const val ARG_TYPE = "ARG_TYPE"

    /**
     * Create new instance of CmtpTimeDialogFragment with CmtpTimePickerView embedded.
     * @param positiveButtonText Custom positive button text. "OK" by default.
     * @param negativeButtonText Custom negative button text. "CANCEL" by default.
     */
    @JvmOverloads
    @JvmStatic
    fun newInstance(
      positiveButtonText: String = "OK",
      negativeButtonText: String = "Cancel"
    ) = CmtpTimeDialogFragment().apply {
      arguments = Bundle().apply {
        putString(ARG_POSITIVE_BUTTON_TEXT, positiveButtonText)
        putString(ARG_NEGATIVE_BUTTON_TEXT, negativeButtonText)
      }
    }
  }

  private lateinit var time: CmtpTime
  private lateinit var timePicker: CmtpTimePickerView

  private lateinit var onTime12PickedListener: OnTime12PickedListener
  private lateinit var onTime24PickedListener: OnTime24PickedListener

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val context = requireContext()

    timePicker = CmtpTimePickerView(context)
    savedInstanceState?.let { restoreState(it) }
    if (this::time.isInitialized) timePicker.setTime(time)

    val dialogBuilder = AlertDialog.Builder(context, R.style.CmtpDialogFrameStyle)
    dialogBuilder.setView(timePicker)

    dialogBuilder.setPositiveButton(
      arguments?.getString(ARG_POSITIVE_BUTTON_TEXT)
    ) { _, _ -> onTimePicked() }

    dialogBuilder.setNegativeButton(
      arguments?.getString(ARG_NEGATIVE_BUTTON_TEXT)
    ) { _, _ -> }

    return dialogBuilder.create()
  }

  private fun saveState(outState: Bundle) {
    if (!this::timePicker.isInitialized) return
    when (timePicker.getType()) {
      HOUR_24 -> {
        outState.putInt(ARG_HOUR, timePicker.getTime24().hour)
        outState.putInt(ARG_MINUTE, timePicker.getTime24().minute)
      }
      HOUR_12 -> {
        outState.putInt(ARG_HOUR, timePicker.getTime12().hour)
        outState.putInt(ARG_MINUTE, timePicker.getTime12().minute)
        outState.putString(ARG_PM_AM, timePicker.getTime12().pmAm.name)
      }
    }
    outState.putString(ARG_TYPE, timePicker.getType().name)
  }

  private fun restoreState(stateBundle: Bundle) {
    val type = enumValueOf<CmtpTimeType>(stateBundle.getString(ARG_TYPE)!!)
    when (type) {
      HOUR_24 -> {
        val hour = stateBundle.getInt(ARG_HOUR, CmtpTime24.DEFAULT.hour)
        val minute = stateBundle.getInt(ARG_MINUTE, CmtpTime24.DEFAULT.minute)
        time = CmtpTime24(hour, minute)
      }
      HOUR_12 -> {
        val hour = stateBundle.getInt(ARG_HOUR, CmtpTime12.DEFAULT.hour)
        val minute = stateBundle.getInt(ARG_MINUTE, CmtpTime12.DEFAULT.hour)
        val pmAm = stateBundle.getString(ARG_PM_AM, CmtpTime12.DEFAULT.pmAm.name)
        time = CmtpTime12(hour, minute, enumValueOf(pmAm))
      }
    }
  }

  override fun onResume() {
    super.onResume()
    dialog?.window?.let {
      val width = resources.getDimensionPixelSize(R.dimen.cmtp_timepicker_width)
      it.setLayout(width, WRAP_CONTENT)
    }
  }

  private fun onTimePicked() {
    when (timePicker.getType()) {
      HOUR_12 -> {
        if (this::onTime12PickedListener.isInitialized) {
          onTime12PickedListener.onTimePicked(timePicker.getTime12())
        }
      }
      HOUR_24 -> {
        if (this::onTime24PickedListener.isInitialized) {
          onTime24PickedListener.onTimePicked(timePicker.getTime24())
        }
      }
    }
  }

  /**
   * Set initial time and initialize picker with 12-Hour format.
   * @param hour from 1 to 12.
   * @param minute from 0 to 59.
   * @param pmAm PM or AM.
   * @throws IllegalStateException when given hour or minute is out of valid range.
   */
  fun setInitialTime12(hour: Int, minute: Int, pmAm: CmtpTime12.PmAm) {
    time = CmtpTime12(hour, minute, pmAm)
  }

  /**
   * Set initial time and initialize picker with 24-Hour format.
   * @param hour from 0 to 23.
   * @param minute from 0 to 59.
   * @throws IllegalStateException when given hour or minute is out of valid range.
   */
  fun setInitialTime24(hour: Int, minute: Int) {
    time = CmtpTime24(hour, minute)
  }

  /**
   * Set time picked listener for 12-Hour format.
   */
  fun setOnTime12PickedListener(listener: OnTime12PickedListener) {
    check(time.getType() == HOUR_12) { "Invalid listener type. Time picker has been initialised as 24-Hour type" }
    this.onTime12PickedListener = listener
  }

  /**
   * Set time picked listener for 24-Hour format.
   */
  fun setOnTime24PickedListener(listener: OnTime24PickedListener) {
    check(time.getType() == HOUR_24) { "Invalid listener type. Time picker has been initialised as 12-Hour type" }
    this.onTime24PickedListener = listener
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    saveState(outState)
  }
}