package pl.ejdev.medic.view

import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections.observableArrayList
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
import pl.ejdev.medic.utils.FontWeight
import pl.ejdev.medic.utils.fxStyle
import pl.ejdev.medic.utils.px

private const val TOTAL_COUNT_ = "Total Count:"
private const val SETTINGS = "Settings"
private const val CURVE_SETTINGS = "Curve Settings"
private const val NSB_COUNT_ = "NSB Count:"
private const val ZERO_COUNT_ = "Zero Count:"
private const val EXPORT_SETTINGS = "Export Settings"
private const val EXPORT_TYPE = "Export Type:"
private const val SAVE = "Save"

fun settingsView(): VBox = vbox {
    val controller: SettingsController by inject(SettingsController::class.java)
    padding = Insets(20.0)
    spacing = 15.0

    val settingsProperty = SimpleObjectProperty(Settings())
    val currentSettings = settingsProperty.get()
    label(SETTINGS) {
        style = fxStyle {
            `font-size`(18.px)
            `font-weight`(FontWeight.BOLD)
        }
    }
    separator {}
    label(CURVE_SETTINGS) {
        style = fxStyle {
            `font-size`(14.px)
            `font-weight`(FontWeight.BOLD)
        }
    }
    val totalCountField = Spinner<Int>(1, 100, currentSettings.curve.totalCount)
    val nsbCountField = Spinner<Int>(1, 100, currentSettings.curve.nsbCount)
    val zeroCountField = Spinner<Int>(1, 100, currentSettings.curve.zeroCount)

    gridPane {
        hgap = 10.0
        vgap = 10.0
        add(Label(TOTAL_COUNT_), 0, 0)
        add(totalCountField, 1, 0)

        add(Label(NSB_COUNT_), 0, 1)
        add(nsbCountField, 1, 1)

        add(Label(ZERO_COUNT_), 0, 2)
        add(zeroCountField, 1, 2)
    }
    separator {}
    label(EXPORT_SETTINGS) {
        style = fxStyle {
            `font-size`(14.px)
            `font-weight`(FontWeight.BOLD)
        }
    }

    val exportTypeBox = ComboBox<Settings.ExportType>().apply {
        items = observableArrayList(Settings.ExportType.entries)
        value = currentSettings.exportType
    }

    hbox(10.0) {
        alignment = Pos.CENTER_LEFT
        label(EXPORT_TYPE)
        addChild(exportTypeBox)
    }
    separator {}
    hbox(10.0) {
        alignment = Pos.CENTER_RIGHT
        button(SAVE) {
            onAction {
                val newSettings = Settings(
                    curve = Settings.Curve(
                        totalCount = totalCountField.value,
                        nsbCount = nsbCountField.value,
                        zeroCount = zeroCountField.value
                    ),
                    exportType = exportTypeBox.value
                )
                controller.save(newSettings)
            }
        }
    }
}
