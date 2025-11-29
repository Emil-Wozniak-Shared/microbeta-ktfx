package pl.ejdev.medic

import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module
import pl.ejdev.medic.architecture.ArchitectureConfiguration
import pl.ejdev.medic.controller.MainController
import pl.ejdev.medic.controller.SamplesController
import pl.ejdev.medic.controller.SettingsController
import pl.ejdev.medic.service.ControlCurveProvider

object KoinConfig {

    fun init() = startKoin {
        modules(
            javafxModule,
            *ArchitectureConfiguration.modules
        )
    }

    private val javafxModule = module {
        single { MainController() }
        single { ControlCurveProvider() }
        single { SettingsController() }
        single { SamplesController(get()) }
    }
}
