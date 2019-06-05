package com.michaldrabik.cmtpsample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.michaldrabik.classicmaterialtimepicker.CmtpDialogFragment
import com.michaldrabik.classicmaterialtimepicker.model.CmtpTime12
import com.michaldrabik.classicmaterialtimepicker.model.CmtpTime12.PmAm.PM
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    cmtpDialogButton.setOnClickListener { showTimePickerDialog() }
  }

  private fun showTimePickerDialog() {
    val dialog = CmtpDialogFragment.newInstance("Pos", "Neg")
    dialog.setInitialTime(CmtpTime12(5, 15, PM))
    dialog.setOnTime12PickedListener {
      Log.d("TIME", it.toString())
    }
    dialog.show(supportFragmentManager, "TimePicker")
  }
}
