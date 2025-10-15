package pl.ejdev.medic

import org.koin.dsl.module
import pl.ejdev.medic.controller.FileReadController
import pl.ejdev.medic.controller.MainController
import pl.ejdev.medic.controller.SamplesController
import pl.ejdev.medic.controller.SettingsController
import pl.ejdev.medic.service.XslxService

val appModule = module {
    single { MainController() }
    single { FileReadController() }
    single { SettingsController() }
    single { SamplesController(get()) }
    single { XslxService() }
}
