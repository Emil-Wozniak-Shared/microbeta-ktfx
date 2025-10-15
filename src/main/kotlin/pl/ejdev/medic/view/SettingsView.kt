package pl.ejdev.medic.view

import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.control.Spinner
import javafx.scene.layout.VBox
import ktfx.coroutines.onAction
import ktfx.layouts.*
import org.koin.java.KoinJavaComponent.inject
import pl.ejdev.medic.controller.SettingsController
import pl.ejdev.medic.model.Settings

fun settingsView(): VBox = vbox {
    val controller: SettingsController by inject(SettingsController::class.java)
    padding = Insets(20.0)
    spacing = 15.0

    val settingsProperty = SimpleObjectProperty(Settings())
    val currentSettings = settingsProperty.get()

    label("Settings") {
        style = "-fx-font-size: 18px; -fx-font-weight: bold;"
    }

    separator {}

    // --- Curve Section ---
    label("Curve Settings") {
        style = "-fx-font-size: 14px; -fx-font-weight: bold;"
    }

    // ✅ Declare fields outside gridPane so we can access them later
    val totalCountField = Spinner<Int>(1, 100, currentSettings.curve.totalCount)
    val nsbCountField = Spinner<Int>(1, 100, currentSettings.curve.nsbCount)
    val zeroCountField = Spinner<Int>(1, 100, currentSettings.curve.zeroCount)

    gridPane {
        hgap = 10.0
        vgap = 10.0
        add(Label("Total Count:"), 0, 0)
        add(totalCountField, 1, 0)

        add(Label("NSB Count:"), 0, 1)
        add(nsbCountField, 1, 1)

        add(Label("Zero Count:"), 0, 2)
        add(zeroCountField, 1, 2)
    }

    separator {}

    // --- Export Section ---
    label("Export Settings") {
        style = "-fx-font-size: 14px; -fx-font-weight: bold;"
    }

    val exportTypeBox = ComboBox<Settings.ExportType>().apply {
        items = FXCollections.observableArrayList(Settings.ExportType.entries)
        value = currentSettings.exportType
    }

    hbox(10.0) {
        alignment = Pos.CENTER_LEFT
        label("Export Type:")
        addChild(exportTypeBox)
    }

    separator {}

    // --- Save Button ---
    hbox(10.0) {
        alignment = Pos.CENTER_RIGHT
        button("Save") {
            onAction {
                val newSettings = Settings(
                    curve = Settings.Curve(
                        totalCount = totalCountField.value,
                        nsbCount = nsbCountField.value,
                        zeroCount = zeroCountField.value
                    ),
                    exportType = exportTypeBox.value
                )

                controller.save(newSettings) // ✅ raw .properties storage
            }
        }
    }
}
