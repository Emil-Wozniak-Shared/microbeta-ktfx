package pl.ejdev.medic.view

import javafx.beans.value.ObservableValue
import javafx.collections.ListChangeListener
import javafx.scene.control.Label
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import ktfx.coroutines.onAction
import ktfx.layouts.*
import org.koin.java.KoinJavaComponent.inject
import pl.ejdev.medic.components.Extension
import pl.ejdev.medic.components.samplesTable
import pl.ejdev.medic.components.uploadFileButton
import pl.ejdev.medic.controller.MainController
import pl.ejdev.medic.controller.SamplesController
import pl.ejdev.medic.model.RunInformation
import pl.ejdev.medic.model.Sample
import pl.ejdev.medic.model.xlsx.CreateXlsxCommand
import pl.ejdev.medic.service.XslxService
import pl.ejdev.medic.utils.classes
import pl.ejdev.medic.utils.listenText
import java.io.File

private const val UPLOAD = "Upload"

fun dashboardView() = stackPane {
    val mainController: MainController by inject(MainController::class.java)
    val samplesController: SamplesController by inject(SamplesController::class.java)
    val xslxService: XslxService by inject(XslxService::class.java)

    classes(Style.MAIN_CONTENT)
    vbox {
        hbox {
//            val label = Label("")
//            textField { listenText { label.text = it } }
//            addChild(label)
            button("Save XSLX") {
                onAction {
                    val fileName = "file.xlsx"
                    val userHome = System.getProperty("user.home")
                    val outputFile = File(userHome, fileName)
                    outputFile.parentFile?.mkdirs()
                    samplesController.runInformation.get()
                    val samples = samplesController.samples
                        .filter { it.type != Sample.Type.S }
                        .toList()
                    val hormone = "Kortyzol"
                    val date = "2019-01-08"
                    val subject = "Owce"
                    val initialData: List<Sample> = samplesController.samples
                        .map { it }
                        .toList()

                    val standardPoints: List<List<String>> = listOf(
                        // row, F_col_value, G_col_cpm, point_number
                        listOf("1.25", "412", "4"),
                        listOf("1.25", "390", "4"),
                        listOf("2.5", "378", "5"),
                        listOf("2.5", "352", "5"),
                        listOf("5.0", "322", "6"),
                        listOf("5.0", "316", "6"),
                        listOf("10", "225", "7"),
                        listOf("10", "249", "7"),
                        listOf("20", "174", "1"),
                        listOf("40", "102", "2"),
                        listOf("40", "116", "2"),
                        listOf("80", "74", "3"),
                        listOf("80", "65", "3"),
                    )

                    val command = CreateXlsxCommand(hormone, date, subject, initialData, standardPoints)
                    xslxService.generate(mainController.primaryStage(), command)
                    this@hbox.addChild(Label("Saved!"))
                    println("Workbook saved")
                }
            }
        }
        addChild(
            uploadFileButton(
                label = UPLOAD,
                extensions = arrayOf(Extension.TXT),
                successAction = samplesController::bindData
            )
        )
        hbox(20.0) {
            vbox {
                addChild(samplesTable())
                HBox.setHgrow(this, Priority.ALWAYS)
            }
            vbox(5.0) {
                HBox.setHgrow(this, Priority.ALWAYS)
                runInformation()
                platesSection()
            }
        }
    }
}

private fun @KtfxLayoutDslMarker KtfxVBox.platesSection() {
    val samplesController: SamplesController by inject(SamplesController::class.java)

    vbox(5.0) {
        VBox.setVgrow(this, Priority.NEVER) // fixed height section
        label("Plates") { style = "-fx-font-weight: bold;" }
        scrollPane {
            isFitToWidth = true
            prefHeight = 400.0
            content = vbox(5.0) {
                samplesController.plates.addListener(ListChangeListener { _ ->
                    children.clear()
                    samplesController.plates.forEach { plate ->
                        vbox(2.0) {
                            children.add(Label("Plate ${plate.number}: ${plate.cassetteInfo}").apply {
                                style = "-fx-font-weight: bold;"
                                classes("plate-label")
                            })

                            gridPane {
                                // Column headers 1..6
                                (1..6).forEachIndexed { colIndex, col ->
                                    add(Label(col.toString()).apply {
                                        classes("plate-label")
                                    }, colIndex + 1, 0)
                                }

                                // Rows A..D
                                plate.rows.forEach { (rowChar, values) ->
                                    val rowIndex = "ABCD".indexOf(rowChar) + 1
                                    add(Label(rowChar.toString()).apply {
                                        classes("plate-label")
                                    }, 0, rowIndex)
                                    values.forEachIndexed { colIndex, value ->
                                        add(Label(value.toString()).apply {
                                            classes("plate-cell")
                                        }, colIndex + 1, rowIndex)
                                    }
                                }
                                hgap = 5.0
                                vgap = 2.0
                            }
                        }
                    }
                })
            }
        }
    }
}

private fun @KtfxLayoutDslMarker KtfxVBox.runInformation() {
    val samplesController: SamplesController by inject(SamplesController::class.java)

    vbox(5.0) {
        VBox.setVgrow(this, Priority.NEVER) // fixed height section
        classes("run-info-panel")
        label("Run Information") {
            style = "-fx-font-weight: bold;"
            classes("run-info-title")
        }
        hbox {
            label("Counting protocol no: ") {
                classes("run-info-label")
            }
            label("") {
                classes("run-info-value")
                textProperty().bind(samplesController.mapRunInfo { it?.countingProtocolNo?.toString() })
            }
        }
        hbox {
            label("Name: ") {
                classes("run-info-label")
            }
            label("") {
                classes("run-info-value")
                textProperty().bind(samplesController.mapRunInfo { it?.name })
            }
        }
        hbox {
            label("Normalization protocol no: ") {
                classes("run-info-label")
            }
            label("-") {
                classes("run-info-value")
                textProperty().bind(samplesController.mapRunInfo { it?.normalizationProtocolNo?.toString() })
            }
        }
    }
}

private fun SamplesController.mapRunInfo(action: (RunInformation?) -> String?): ObservableValue<String> =
    this.runInformation.map { action(it) ?: "-" }