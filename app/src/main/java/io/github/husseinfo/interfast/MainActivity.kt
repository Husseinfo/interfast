package io.github.husseinfo.interfast

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSeekBar
import nl.joery.timerangepicker.TimeRangePicker


class MainActivity : AppCompatActivity() {
    private val methods = arrayOf("16/8", "18/6", "20/4", "22/2")

    private lateinit var mPrefs: SharedPreferences

    private lateinit var startTime: TextView
    private lateinit var endTime: TextView
    private lateinit var startAt: TextView
    private lateinit var endAt: TextView
    private lateinit var duration: TextView
    private lateinit var method: TextView
    private lateinit var picker: TimeRangePicker
    private lateinit var seekbar: AppCompatSeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mPrefs = getSharedPreferences(localClassName, MODE_PRIVATE);

        picker = findViewById(R.id.trp_sleeping)
        picker.startTimeMinutes = mPrefs.getInt("start", 22 * 60 + 30)
        picker.endTimeMinutes = mPrefs.getInt("end", 8 * 60 + 30)

        startTime = findViewById(R.id.start_time)
        endTime = findViewById(R.id.end_time)
        startAt = findViewById(R.id.tv_breakfast_time)
        endAt = findViewById(R.id.tv_fast_time)
        duration = findViewById(R.id.tv_sleeping_duration)
        method = findViewById(R.id.tv_method_selected)
        seekbar = findViewById(R.id.sb_method)
        seekbar.progress = mPrefs.getInt("method", 1)

        updateTimes()
        updateDuration()
        updateMethod()

        picker.setOnTimeChangeListener(object : TimeRangePicker.OnTimeChangeListener {
            override fun onStartTimeChange(startTime: TimeRangePicker.Time) {
                updateTimes()
            }

            override fun onEndTimeChange(endTime: TimeRangePicker.Time) {
                updateTimes()
            }

            override fun onDurationChange(duration: TimeRangePicker.TimeDuration) {
                updateDuration()
            }
        })

        seekbar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, progress: Int, p2: Boolean) {
                updateMethod()
            }

            override fun onStartTrackingTouch(sb: SeekBar?) {}

            override fun onStopTrackingTouch(sb: SeekBar?) {}
        })
    }


    override fun onPause() {
        super.onPause()
        val ed: SharedPreferences.Editor = mPrefs.edit()
        ed.putInt("start", picker.startTime.totalMinutes)
        ed.putInt("end", picker.endTime.totalMinutes)
        ed.putInt("method", seekbar.progress)
        ed.apply()
    }


    private fun updateDuration() {
        duration.text = getString(R.string.duration, picker.duration)
    }

    private fun updateTimes() {
        endTime.text = picker.endTime.toString()
        startTime.text = picker.startTime.toString()
        calculatePeriod()
    }

    private fun updateMethod() {
        method.text = methods[seekbar.progress]
        calculatePeriod()
    }

    private fun calculatePeriod() {
        val eatingHours = 8L - (seekbar.progress * 2)

        val awakeDuration = TimeRangePicker.TimeDuration(picker.endTime, picker.startTime)
        val midEatingTime = picker.endTime.localTime.plusHours(awakeDuration.hour.toLong() / 2)

        startAt.text = midEatingTime.minusHours(eatingHours / 2).toString()
        endAt.text = midEatingTime.plusHours(eatingHours / 2).toString()
    }
}
