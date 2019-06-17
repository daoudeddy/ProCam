package com.googy.procam.data

import com.googy.procam.R
import com.googy.procam.data.Data.Keys.PRO_CAMERA
import com.googy.procam.data.Data.Keys.SHUTTER_SCHEDULER
import com.googy.procam.data.Data.Keys.SHUTTER_SCHEDULER_VALUE
import com.googy.procam.ui.model.*

object Data {
    val mainData = listOf(
            TitleDividerItem(R.string.camera),
            SwitchItem(PRO_CAMERA, R.string.pro_camera, R.string.pro_camera_summary,
                    listOf(
                            SwitchItem(
                                    key = SHUTTER_SCHEDULER,
                                    title = R.string.shutter_scheduler,
                                    summary = R.string.shutter_scheduler_summary,
                                    subItems = listOf(
                                            SeekBarItem(
                                                    key = SHUTTER_SCHEDULER_VALUE,
                                                    title = R.string.shutter_scheduler_delay,
                                                    minValue = 1000,
                                                    maxValue = 60000
                                            )
                                    )
                            )
                    )
            ),
            EmptyItem(),
            GridItem(
                    identifier = "about",
                    items = listOf(
                            AboutItem(R.string.twitter, "https://twitter.com/daoudedy", R.drawable.twitter),
                            AboutItem(R.string.github, "https://github.com/Edydaoud", R.drawable.github_circle),
                            AboutItem(R.string.xda, "https://forum.xda-sdevelopers.com/member.php?u=4472524", R.drawable.xda)
                    )
            )
    )

    object Keys {
        const val PRO_CAMERA = "pro_camera"
        const val SHUTTER_SCHEDULER = "shutter_scheduler"
        const val SHUTTER_SCHEDULER_VALUE = "shutter_scheduler_value"
    }
}