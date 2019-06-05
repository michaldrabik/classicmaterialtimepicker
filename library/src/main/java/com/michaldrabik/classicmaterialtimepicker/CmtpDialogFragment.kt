package com.michaldrabik.classicmaterialtimepicker

import android.app.Dialog
import android.os.Bundle
import android.view.WindowManager.LayoutParams.WRAP_CONTENT
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.michaldrabik.classicmaterialtimepicker.model.CmtpTime
import com.michaldrabik.classicmaterialtimepicker.model.CmtpTime12
import com.michaldrabik.classicmaterialtimepicker.model.CmtpTime24
import com.michaldrabik.classicmaterialtimepicker.model.CmtpTimeType.HOUR_12
import com.michaldrabik.classicmaterialtimepicker.model.CmtpTimeType.HOUR_24

class CmtpDialogFragment : DialogFragment() {

  companion object {
    private const val ARG_POSITIVE_BUTTON_TEXT = "ARG_POSITIVE_BUTTON_TEXT"
    private const val ARG_NEGATIVE_BUTTON_TEXT = "ARG_NEGATIVE_BUTTON_TEXT"

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

  private var onTime12PickedListener: (CmtpTime12) -> Unit = { }
  private var onTime24PickedListener: (CmtpTime24) -> Unit = { }

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val context = requireContext()

    val timePicker = CmtpTimePickerView(context)
    if (this::time.isInitialized) timePicker.setInitialTime(time)

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
}