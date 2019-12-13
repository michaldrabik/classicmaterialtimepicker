package com.michaldrabik.cmtpsample

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.michaldrabik.classicmaterialtimepicker.CmtpDateDialogFragment
import com.michaldrabik.classicmaterialtimepicker.CmtpTimeDialogFragment
import com.michaldrabik.classicmaterialtimepicker.model.CmtpTime12.PmAm.PM
import com.michaldrabik.classicmaterialtimepicker.utilities.setOnDatePickedListener
import com.michaldrabik.classicmaterialtimepicker.utilities.setOnTime12PickedListener
import com.michaldrabik.classicmaterialtimepicker.utilities.setOnTime24PickedListener
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    dialog12Button.setOnClickListener { showTime12PickerDialog() }
    dialog24Button.setOnClickListener { showTime24PickerDialog() }
    dialogDate.setOnClickListener { showDateTimePickerDialog() }
  }

  private fun showTime12PickerDialog() {
    val dialog = CmtpTimeDialogFragment.newInstance()
    dialog.setInitialTime12(5, 15, PM)
    dialog.setOnTime12PickedListener {
      Toast.makeText(this@MainActivity, it.toString(), Toast.LENGTH_SHORT).show()
    }
    dialog.show(supportFragmentManager, "TimePicker")
  }

  private fun showTime24PickerDialog() {
    val dialog = CmtpTimeDialogFragment.newInstance()
    dialog.setInitialTime24(23, 30)
    dialog.setOnTime24PickedListener {
      Toast.makeText(this@MainActivity, it.toString(), Toast.LENGTH_SHORT).show()
    }
    dialog.show(supportFragmentManager, "TimePicker")
  }

  private fun showDateTimePickerDialog() {
    val dialog = CmtpDateDialogFragment.newInstance()
//
    val minCal = Calendar.getInstance()
    minCal.set(2017, 9, 21)
    dialog.setMinimumDate(minCal.time)
//
//    val maxCal = Calendar.getInstance()
//    maxCal.set(2020, 3, 1)
//    dialog.setMaximumDate(maxCal.time)

    dialog.setMinimumDate(29, 2, 2000)

    dialog.setCustomSeparator(".")
    dialog.setOnDatePickedListener {
      Toast.makeText(this@MainActivity, it.toString(), Toast.LENGTH_SHORT).show()
    }
    dialog.show(supportFragmentManager, "TimePicker")
  }
}
