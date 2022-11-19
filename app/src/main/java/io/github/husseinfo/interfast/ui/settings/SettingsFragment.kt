package io.github.husseinfo.interfast.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.fragment.app.Fragment
import io.github.husseinfo.interfast.R
import io.github.husseinfo.interfast.databinding.FragmentSettingsBinding
import nl.joery.timerangepicker.TimeRangePicker

class SettingsFragment : Fragment() {

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

    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        super.onCreate(savedInstanceState)
        mPrefs = requireActivity().getSharedPreferences(
            requireActivity().localClassName,
            AppCompatActivity.MODE_PRIVATE
        )

        picker = binding.trpSleeping
        picker.startTimeMinutes = mPrefs.getInt("start", 22 * 60 + 30)
        picker.endTimeMinutes = mPrefs.getInt("end", 8 * 60 + 30)

        startTime = binding.startTime
        endTime = binding.endTime
        startAt = binding.tvBreakfastTime
        endAt = binding.tvFastTime
        duration = binding.tvSleepingDuration
        method = binding.tvMethodSelected
        seekbar = binding.sbMethod
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

        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, progress: Int, p2: Boolean) {
                updateMethod()
            }

            override fun onStartTrackingTouch(sb: SeekBar?) {}

            override fun onStopTrackingTouch(sb: SeekBar?) {}
        })
        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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