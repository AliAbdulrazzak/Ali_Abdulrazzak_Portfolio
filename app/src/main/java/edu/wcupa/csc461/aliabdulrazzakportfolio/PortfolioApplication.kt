package edu.wcupa.csc461.aliabdulrazzakportfolio

import android.app.Application
import edu.wcupa.csc461.aliabdulrazzakportfolio.juicetracker.data.AppContainer
import edu.wcupa.csc461.aliabdulrazzakportfolio.juicetracker.data.AppDataContainer
import edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.data.HamburgerAppContainer
import edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.data.HamburgerAppDataContainer
import edu.wcupa.csc461.aliabdulrazzakportfolio.waterme.data.DefaultWaterMeAppContainer
import edu.wcupa.csc461.aliabdulrazzakportfolio.waterme.data.WaterMeAppContainer

class PortfolioApplication : Application() {
    lateinit var container: AppContainer
    lateinit var hamburgerContainer: HamburgerAppContainer
    lateinit var waterMeContainer: WaterMeAppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
        hamburgerContainer = HamburgerAppDataContainer(this)
        waterMeContainer = DefaultWaterMeAppContainer(this)
    }
}
