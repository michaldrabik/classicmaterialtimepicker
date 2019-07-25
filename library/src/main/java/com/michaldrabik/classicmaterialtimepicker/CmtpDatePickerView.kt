package com.michaldrabik.classicmaterialtimepicker

import android.content.Context
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.michaldrabik.classicmaterialtimepicker.model.CmtpDate
import com.michaldrabik.classicmaterialtimepicker.model.CmtpTime
import com.michaldrabik.classicmaterialtimepicker.model.CmtpTime12
import com.michaldrabik.classicmaterialtimepicker.model.CmtpTime24
import com.michaldrabik.classicmaterialtimepicker.model.CmtpTimeType.HOUR_12
import com.michaldrabik.classicmaterialtimepicker.model.CmtpTimeType.HOUR_24
import com.michaldrabik.classicmaterialtimepicker.recycler.CmtpValuesAdapter
import kotlinx.android.synthetic.main.cmtp_datepicker_view.view.*
import java.util.*

class CmtpDatePickerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val calendar = Calendar.getInstance()

    private val recyclerYearsAdapter by lazy { CmtpValuesAdapter() }
    private val recyclerMonthsAdapter by lazy { CmtpValuesAdapter() }
    private val recyclerDaysAdapter by lazy { CmtpValuesAdapter() }

    private val recyclerYearsLayoutManager by lazy { LinearLayoutManager(context, VERTICAL, false) }
    private val recyclerMonthsLayoutManager by lazy { LinearLayoutManager(context, VERTICAL, false) }
    private val recyclerDaysLayoutManager by lazy { LinearLayoutManager(context, VERTICAL, false) }

    private val recyclerYearsSnapHelper by lazy { LinearSnapHelper() }
    private val recyclerMonthsSnapHelper by lazy { LinearSnapHelper() }
    private val recyclerDaysSnapHelper by lazy { LinearSnapHelper() }


    // TODO Erase all those time-related items
    private val recyclerHoursAdapter by lazy { CmtpValuesAdapter() }
    private val recyclerMinutesAdapter by lazy { CmtpValuesAdapter() }
    private val recyclerPmAmAdapter by lazy { CmtpValuesAdapter() }

    private val recyclerHoursLayoutManager by lazy { LinearLayoutManager(context, VERTICAL, false) }
    private val recyclerMinutesLayoutManager by lazy { LinearLayoutManager(context, VERTICAL, false) }
    private val recyclerPmAmLayoutManager by lazy { LinearLayoutManager(context, VERTICAL, false) }

    private val recyclerHoursSnapHelper by lazy { LinearSnapHelper() }
    private val recyclerMinutesSnapHelper by lazy { LinearSnapHelper() }
    private val recyclerPmAmSnapHelper by lazy { LinearSnapHelper() }

    private var time: CmtpTime = CmtpTime24.DEFAULT

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
        cmtpRecyclerDays.apply {
            val days = CmtpDateData.DAYS
            recyclerDaysAdapter.setItems(days.map { String.format("%02d", it) })
            recyclerDaysLayoutManager.scrollToPosition(days.indexOf(calendar.get(Calendar.DAY_OF_MONTH)))
            smoothScrollBy(0, 1)
        }
        cmtpRecyclerMonths.apply {
            val months = CmtpDateData.MONTHS
            recyclerMonthsAdapter.setItems(months.map { String.format("%02d", it) })
            recyclerMonthsLayoutManager.scrollToPosition(months.indexOf(calendar.get(Calendar.MONTH)))
            smoothScrollBy(0, 1)
        }
        cmtpRecyclerYears.apply {
            val years = CmtpDateData.YEARS
            recyclerYearsAdapter.setItems(years.map { String.format("%04d", it) })
            recyclerMonthsLayoutManager.scrollToPosition(years.indexOf(calendar.get(Calendar.YEAR)))
        }
    }

    fun getDate(): CmtpDate {
        val dayView = recyclerDaysSnapHelper.findSnapView(recyclerDaysLayoutManager)
        val monthView = recyclerMonthsSnapHelper.findSnapView(recyclerMonthsLayoutManager)
        val yearView = recyclerYearsSnapHelper.findSnapView(recyclerYearsLayoutManager)

        if (dayView == null || monthView == null || yearView == null) {
            throw java.lang.IllegalStateException("DatePicker view has not been initialized yet.")
        }

        val dayIndex = recyclerDaysLayoutManager.getPosition(dayView)
        val monthIndex = recyclerMonthsLayoutManager.getPosition(monthView)
        val yearIndex = recyclerYearsLayoutManager.getPosition(yearView)

        return CmtpDate(
            recyclerDaysAdapter.getItems()[dayIndex].toInt(),
            recyclerMonthsAdapter.getItems()[monthIndex].toInt(),
            recyclerYearsAdapter.getItems()[yearIndex].toInt()
        )
    }
}