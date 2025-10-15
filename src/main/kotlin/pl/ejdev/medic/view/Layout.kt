package pl.ejdev.medic.view

import javafx.application.Platform
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.layout.*
import ktfx.coroutines.onAction
import ktfx.layouts.*
import pl.ejdev.medic.utils.classes

private const val HOME = "Home"
private const val SETTINGS = "Settings"
private const val ABOUT = "About"
private const val MODERN_JAVA_FX_APP = "Modern JavaFX App"

fun layout(): BorderPane {
    val contentArea = stackPane {
        addChild(dashboardView())
    }
    return borderPane {
        top = topBar()
        left = sideNav(contentArea)
        center = contentArea
    }
}

private fun sideNav(contentArea: StackPane): VBox = vbox {
    padding = Insets(10.0)
    spacing = 10.0
    classes(Style.SIDE_NAV)

    button(HOME) {
        classes(Style.NAV_BUTTON_CLASS)
        maxWidth = Double.MAX_VALUE
        onAction { contentArea.children.setAll(dashboardView()) }
    }

    button(SETTINGS) {
        classes(Style.NAV_BUTTON_CLASS)
        maxWidth = Double.MAX_VALUE
        onAction { contentArea.children.setAll(settingsView()) }
    }

    button(ABOUT) {
        classes(Style.NAV_BUTTON_CLASS)
        maxWidth = Double.MAX_VALUE
        onAction {
        }
    }
}

private fun topBar(): HBox = hbox {
    classes(Style.TOP_BAR)
    vbox {
        label(MODERN_JAVA_FX_APP) {
            classes(Style.TOP_BAR_TITLE)
        }
    }
    region { HBox.setHgrow(this, Priority.ALWAYS) }
    vbox(spacing = 10.0) {
        alignment = Pos.TOP_RIGHT
        button("X") {
            classes("button", "close-btn")
            onAction { Platform.exit() }
        }
    }
}