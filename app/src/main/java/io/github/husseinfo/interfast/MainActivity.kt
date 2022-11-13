package io.github.husseinfo.interfast

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import nl.joery.timerangepicker.TimeRangePicker

class MainActivity : AppCompatActivity() {
    private lateinit var picker: TimeRangePicker
    private lateinit var endTime: TextView
    private lateinit var startTime: TextView
    private lateinit var duration: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        picker = findViewById(R.id.trp_sleeping)
        startTime = findViewById(R.id.start_time)
        endTime = findViewById(R.id.end_time)
        duration = findViewById(R.id.tv_sleeping_duration)

        updateTimes()
        updateDuration()

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
    }

    private fun updateDuration() {
        duration.text = getString(R.string.duration, picker.duration)
    }

    private fun updateTimes() {
        endTime.text = picker.endTime.toString()
        startTime.text = picker.startTime.toString()
    }
}