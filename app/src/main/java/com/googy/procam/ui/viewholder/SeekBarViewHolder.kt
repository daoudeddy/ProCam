package com.googy.procam.ui.viewholder

import android.os.Build
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.appcompat.widget.AppCompatTextView
import com.googy.procam.R
import com.googy.procam.di.Preferences
import com.googy.procam.ui.model.SeekBarItem
import org.koin.core.KoinComponent
import org.koin.core.inject

class SeekBarViewHolder(parent: ViewGroup) : BaseViewHolder<SeekBarItem>(R.layout.seekbar_item, parent), KoinComponent {

    private val title: AppCompatTextView by lazy { itemView.findViewById<AppCompatTextView>(R.id.titleView) }
    private val summary: AppCompatTextView by lazy { itemView.findViewById<AppCompatTextView>(R.id.summaryView) }
    private val seekBar: AppCompatSeekBar by lazy { itemView.findViewById<AppCompatSeekBar>(R.id.seekBarView) }
    private val preferences: Preferences by inject()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun bindView(item: SeekBarItem) {
        title.setText(item.title)

        seekBar.max = item.maxValue
        seekBar.min = item.minValue
        seekBar.progress = preferences.get(item.key, item.minValue)
        summary.text = summary.resources.getString(R.string.progress_value).format(seekBar.progress)

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                preferences.putInteger(item.key, seekBar.currentProgress())
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                summary.text = summary.resources.getString(R.string.progress_value).format(seekBar.currentProgress())
            }
        })
    }

    private fun SeekBar.currentProgress(): Int {
        val denominator = when {
            progress < 10000 -> 500
            progress < 30000 -> 1000
            else -> 5000
        }
        return (progress / denominator) * denominator
    }
}
