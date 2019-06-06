package com.michaldrabik.cmtpsample

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.michaldrabik.classicmaterialtimepicker.CmtpDialogFragment
import com.michaldrabik.classicmaterialtimepicker.OnTime12PickedListener
import com.michaldrabik.classicmaterialtimepicker.OnTime24PickedListener
import com.michaldrabik.classicmaterialtimepicker.model.CmtpTime12
import com.michaldrabik.classicmaterialtimepicker.model.CmtpTime12.PmAm.PM
import com.michaldrabik.classicmaterialtimepicker.model.CmtpTime24
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    dialog12Button.setOnClickListener { showTime12PickerDialog() }
    dialog24Button.setOnClickListener { showTime24PickerDialog() }
  }

  private fun showTime12PickerDialog() {
    val dialog = CmtpDialogFragment.newInstance()
    dialog.setInitialTime(CmtpTime12(5, 15, PM))
    dialog.setOnTime12PickedListener(object : OnTime12PickedListener {
      override fun onTimePicked(time: CmtpTime12) {
        Toast.makeText(this@MainActivity, time.toString(), Toast.LENGTH_SHORT).show()
      }
    })
    dialog.show(supportFragmentManager, "TimePicker")
  }

  private fun showTime24PickerDialog() {
    val dialog = CmtpDialogFragment.newInstance()
    dialog.setInitialTime(CmtpTime24(12, 45))
    dialog.setOnTime24PickedListener(object : OnTime24PickedListener {
      override fun onTimePicked(time: CmtpTime24) {
        Toast.makeText(this@MainActivity, time.toString(), Toast.LENGTH_SHORT).show()
      }
    })
    dialog.show(supportFragmentManager, "TimePicker")
  }
}
