package com.michaldrabik.classicmaterialtimepicker

import android.content.Context
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.michaldrabik.classicmaterialtimepicker.model.CmtpDate
import com.michaldrabik.classicmaterialtimepicker.recycler.CmtpValuesAdapter
import com.michaldrabik.classicmaterialtimepicker.utilities.OnSnapPositionChangeListener
import com.michaldrabik.classicmaterialtimepicker.utilities.SnapOnScrollListener
import com.michaldrabik.classicmaterialtimepicker.utilities.attachSnapHelperWithListener
import com.michaldrabik.classicmaterialtimepicker.utilities.getNumberOfDays
import kotlinx.android.synthetic.main.cmtp_datepicker_view.view.*

class CmtpDatePickerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), OnSnapPositionChangeListener {

    private val recyclerYearsAdapter by lazy { CmtpValuesAdapter() }
    private val recyclerMonthsAdapter by lazy { CmtpValuesAdapter() }
    private val recyclerDaysAdapter by lazy { CmtpValuesAdapter() }

    private val recyclerYearsLayoutManager by lazy { LinearLayoutManager(context, VERTICAL, false) }
    private val recyclerMonthsLayoutManager by lazy { LinearLayoutManager(context, VERTICAL, false) }
    private val recyclerDaysLayoutManager by lazy { LinearLayoutManager(context, VERTICAL, false) }

    private val recyclerYearsSnapHelper by lazy { LinearSnapHelper() }
    private val recyclerMonthsSnapHelper by lazy { LinearSnapHelper() }
    private val recyclerDaysSnapHelper by lazy { LinearSnapHelper() }

    private var date: CmtpDate = CmtpDate.DEFAULT

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
        cmtpRecyclerYears.apply {
            val years = CmtpDateData.YEARS
            recyclerYearsAdapter.setItems(years.map { String.format("%04d", it) })
            recyclerYearsLayoutManager.scrollToPosition(years.indexOf(date.year))
            smoothScrollBy(0, 1)

            attachSnapHelperWithListener(
                recyclerYearsSnapHelper,
                SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL_STATE_IDLE,
                this@CmtpDatePickerView
            )
        }

        cmtpRecyclerMonths.apply {
            val months = CmtpDateData.MONTHS
            recyclerMonthsAdapter.setItems(months.map { String.format("%02d", it) })
            recyclerMonthsLayoutManager.scrollToPosition(months.indexOf(date.month))
            smoothScrollBy(0, 1)

            attachSnapHelperWithListener(
                recyclerMonthsSnapHelper,
                SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL_STATE_IDLE,
                this@CmtpDatePickerView
            )
        }

        // Days RV is setup after month and year because it's necessary to check how many days that specific month has.
        setUpDaysRecyclerBasedOnDate(date)

        cmtpRecyclerDays.attachSnapHelperWithListener(
            recyclerDaysSnapHelper,
            SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL_STATE_IDLE,
            this@CmtpDatePickerView
            )
    }

    private fun setUpDaysRecyclerBasedOnDate(cmtpDate: CmtpDate) {
        cmtpRecyclerDays.apply {
            var dayChanged = false
            val maxNumberOfDays = getNumberOfDays(cmtpDate)

            if (cmtpDate.day > maxNumberOfDays) {
                date = CmtpDate(maxNumberOfDays, cmtpDate.month, cmtpDate.year)
                dayChanged = true
            }

            val days = (1..maxNumberOfDays)
            recyclerDaysAdapter.setItems(days.map { String.format("%02d", it) })
            recyclerDaysAdapter.notifyDataSetChanged()

            if (dayChanged) {
                recyclerDaysLayoutManager.scrollToPosition(days.indexOf(date.day))
                smoothScrollBy(0, 1)
            }
        }
    }

    override fun onSnapPositionChange(position: Int, rv: RecyclerView) {
        date = getDate()

        if (rv != cmtpRecyclerDays) {
            setUpDaysRecyclerBasedOnDate(date)
        }
    }

    /**
     * Set time with 24-Hour or 12-Hour format.
     */
    fun setDate(initialDate: CmtpDate) {
        date = initialDate
        setupRecyclersData()
    }

    fun getDate(): CmtpDate {
        val dayView = recyclerDaysSnapHelper.findSnapView(recyclerDaysLayoutManager)
        val monthView = recyclerMonthsSnapHelper.findSnapView(recyclerMonthsLayoutManager)
        val yearView = recyclerYearsSnapHelper.findSnapView(recyclerYearsLayoutManager)

        if (dayView == null || monthView == null || yearView == null) {
            throw java.lang.IllegalStateException("DatePicker view has not been initialized yet.")
        }

        // Force update of index when new month has fewer days than previous month
        var dayIndex = recyclerDaysLayoutManager.getPosition(dayView)
        if (dayIndex >= recyclerDaysAdapter.getItems().size) {
            dayIndex = recyclerDaysAdapter.getItems().size - 1
        }

        val monthIndex = recyclerMonthsLayoutManager.getPosition(monthView)
        val yearIndex = recyclerYearsLayoutManager.getPosition(yearView)

        return CmtpDate(
            recyclerDaysAdapter.getItems()[dayIndex].toInt(),
            recyclerMonthsAdapter.getItems()[monthIndex].toInt(),
            recyclerYearsAdapter.getItems()[yearIndex].toInt()
        )
    }
}