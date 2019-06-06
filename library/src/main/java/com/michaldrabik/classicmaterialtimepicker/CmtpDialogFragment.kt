package com.michaldrabik.classicmaterialtimepicker

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.WindowManager.LayoutParams.WRAP_CONTENT
import com.michaldrabik.classicmaterialtimepicker.model.CmtpTime
import com.michaldrabik.classicmaterialtimepicker.model.CmtpTime12
import com.michaldrabik.classicmaterialtimepicker.model.CmtpTime24
import com.michaldrabik.classicmaterialtimepicker.model.CmtpTimeType
import com.michaldrabik.classicmaterialtimepicker.model.CmtpTimeType.HOUR_12
import com.michaldrabik.classicmaterialtimepicker.model.CmtpTimeType.HOUR_24

class CmtpDialogFragment : DialogFragment() {

  companion object {
    private const val ARG_POSITIVE_BUTTON_TEXT = "ARG_POSITIVE_BUTTON_TEXT"
    private const val ARG_NEGATIVE_BUTTON_TEXT = "ARG_NEGATIVE_BUTTON_TEXT"
    private const val ARG_HOUR = "ARG_HOUR"
    private const val ARG_MINUTE = "ARG_MINUTE"
    private const val ARG_PM_AM = "ARG_PM_AM"
    private const val ARG_TYPE = "ARG_TYPE"

    /**
     * Create new instance of CmtpDialogFragment with CmtpTimePickerView embedded.
     * @param positiveButtonText Custom positive button text. "OK" by default.
     * @param negativeButtonText Custom negative button text. "CANCEL" by default.
     */
    @JvmOverloads
    fun newInstance(
      positiveButtonText: String = "OK",
      negativeButtonText: String = "Cancel"
    ) = CmtpDialogFragment().apply {
      arguments = Bundle().apply {
        putString(ARG_POSITIVE_BUTTON_TEXT, positiveButtonText)
        putString(ARG_NEGATIVE_BUTTON_TEXT, negativeButtonText)
      }
    }
  }

  private lateinit var time: CmtpTime
  private lateinit var timePicker: CmtpTimePickerView

  private var onTime12PickedListener: (CmtpTime12) -> Unit = { }
  private var onTime24PickedListener: (CmtpTime24) -> Unit = { }

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val context = requireContext()

    timePicker = CmtpTimePickerView(context)
    savedInstanceState?.let { restoreState(it) }
    if (this::time.isInitialized) timePicker.setTime(time)

    val dialogBuilder = AlertDialog.Builder(context, R.style.CmtpDialogFrameStyle)
    dialogBuilder.setView(timePicker)

    dialogBuilder.setPositiveButton(
      arguments?.getString(ARG_POSITIVE_BUTTON_TEXT)
    ) { _, _ ->
      when (timePicker.getType()) {
        HOUR_12 -> onTime12PickedListener.invoke(timePicker.getTime12())
        HOUR_24 -> onTime24PickedListener.invoke(timePicker.getTime24())
      }
    }

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
    dialog.window?.let {
      val width = resources.getDimensionPixelSize(R.dimen.cmtp_timepicker_width)
      it.setLayout(width, WRAP_CONTENT)
    }
  }

  /**
   * Set initial time with 24-Hour or 12-Hour format.
   */
  fun setInitialTime(initialTime: CmtpTime) {
    time = initialTime
  }

  /**
   * Set time picked listener for 12-Hour format.
   */
  fun setOnTime12PickedListener(listener: (CmtpTime12) -> Unit) {
    check(time.type == HOUR_12) { "Invalid listener type. Time picker has been initialised as 24-Hour type" }
    this.onTime12PickedListener = listener
  }

  /**
   * Set time picked listener for 24-Hour format.
   */
  fun setOnTime24PickedListener(listener: (CmtpTime24) -> Unit) {
    check(time.type == HOUR_24) { "Invalid listener type. Time picker has been initialised as 12-Hour type" }
    this.onTime24PickedListener = listener
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    saveState(outState)
  }
}