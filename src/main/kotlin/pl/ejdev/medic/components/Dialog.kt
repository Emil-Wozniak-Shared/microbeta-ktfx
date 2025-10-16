package pl.ejdev.medic.components

import javafx.scene.paint.Color
import javafx.stage.Modality
import javafx.stage.Stage
import ktfx.layouts.KtfxStackPane
import ktfx.layouts.scene
import ktfx.layouts.stackPane
import ktfx.windows.stage

fun dialog(owner: Stage, title: String, configuration: KtfxStackPane.() -> Unit): Stage = stage {
    this.title = title
    initOwner(owner)
    initModality(Modality.APPLICATION_MODAL)
    scene = scene(400.0, 200.0, Color.RED) {
        stackPane {
            configuration(this)
        }
    }
    showAndWait()
}