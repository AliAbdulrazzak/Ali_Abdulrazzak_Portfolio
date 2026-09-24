package edu.wcupa.csc461.aliabdulrazzakportfolio.waterme.data

import android.content.Context

interface WaterMeAppContainer {
    val waterRepository: WaterRepository
}

class DefaultWaterMeAppContainer(context: Context) : WaterMeAppContainer {
    override val waterRepository: WaterRepository = WorkManagerWaterRepository(context)
}
