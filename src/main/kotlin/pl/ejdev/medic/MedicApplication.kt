package pl.ejdev.medic

import javafx.application.Application
import javafx.stage.Stage
import ktfx.layouts.scene
import org.koin.core.context.GlobalContext.startKoin
import org.koin.java.KoinJavaComponent.inject
import pl.ejdev.medic.controller.MainController
import pl.ejdev.medic.view.Style
import pl.ejdev.medic.view.layout

private const val TITLE = "Medic App"

class MedicApplication() : Application() {
    override fun init() {
        startKoin { modules(appModule) }
    }

    private val controller: MainController by inject(MainController::class.java)

    override fun start(stage: Stage) {
        controller.bindStage(stage)
        stage.title = TITLE
        stage.isFullScreen = true
        stage.scene {
            stylesheets.add(Style.SOURCE)
            addChild(layout())
        }
        stage.show()
    }
}
