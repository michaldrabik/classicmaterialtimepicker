package com.michaldrabik.classicmaterialtimepicker

import android.app.Dialog
import android.os.Bundle
import android.view.WindowManager.LayoutParams.WRAP_CONTENT
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.michaldrabik.classicmaterialtimepicker.model.CmtpTime

class CmtpDialogFragment : DialogFragment() {

  companion object {
    fun newInstance(): CmtpDialogFragment {
      return CmtpDialogFragment()
    }
  }

  private lateinit var time: CmtpTime
  private var onTimePickedListener: (CmtpTime) -> Unit = { }

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val context = requireContext()

    val timePicker = CmtpTimePickerView(context)
    if (this::time.isInitialized) {
      timePicker.setInitialTime(time)
    }

    val dialogBuilder = AlertDialog.Builder(context, R.style.CmtpDialogFrameStyle)
    dialogBuilder.setView(timePicker)
    dialogBuilder.setPositiveButton(R.string.cmtp_ok) { _, _ ->
      val time = when {
        timePicker.is12Hour() -> timePicker.getTime12()
        else -> timePicker.getTime24()
      }
      onTimePickedListener.invoke(time)
    }
    dialogBuilder.setNegativeButton(R.string.cmtp_cancel) { _, _ -> }

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

  fun setOnTimePickedListener(listener: (CmtpTime) -> Unit) {
    this.onTimePickedListener = listener
  }
}