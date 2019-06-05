package com.michaldrabik.cmtpsample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.michaldrabik.classicmaterialtimepicker.CmtpDialogFragment
import com.michaldrabik.classicmaterialtimepicker.model.CmtpTime24
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    cmtpDialogButton.setOnClickListener { showTimePickerDialog() }
  }

  private fun showTimePickerDialog() {
    val dialog = CmtpDialogFragment.newInstance("Hello", "Bye")
    dialog.setInitialTime(CmtpTime24(12, 0))
    dialog.setOnTime24PickedListener {
      Log.d("TIME", it.toString())
    }
    dialog.show(supportFragmentManager, "TimePicker")
  }
}
