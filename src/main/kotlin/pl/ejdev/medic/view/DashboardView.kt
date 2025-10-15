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
import pl.ejdev.medic.controller.SamplesController
import pl.ejdev.medic.model.RunInformation
import pl.ejdev.medic.service.XslxService
import pl.ejdev.medic.utils.classes
import pl.ejdev.medic.utils.listenText
import java.io.File

private const val TITLE = "Medic"
private const val UPLOAD = "Upload"

fun dashboardView() = stackPane {
    val samplesController: SamplesController by inject(SamplesController::class.java)
    val xslxService: XslxService by inject(XslxService::class.java)

    classes(Style.MAIN_CONTENT)
    vbox {
        hbox(2.0) {
            text(TITLE) {}
        }
        hbox {
            val label = Label("")
            textField { listenText { label.text = it } }
            addChild(label)
            button("Save XSLX") {
                onAction {
                    val fileName = "file.xlsx"
                    val userHome = System.getProperty("user.home")
                    val outputFile = File(userHome, fileName)

                    // Ensure parent directory exists (optional)
                    outputFile.parentFile?.mkdirs()

                    val runInfo = samplesController.runInformation.get()
                    val sampleData = listOf(
                        listOf("1", "Total", "1976"),
                        listOf("2", "Total", "1982"),
                        listOf("3", "Bg", "32"),
                        listOf("4", "Bg", "36"),
                        listOf("5", "Bo", "458"),
                        listOf("6", "Bo", "459"),
                        listOf("7", "Bo", "447"),
                        listOf("8", "1.25", "412"),
                        listOf("9", "1.25", "390"),
                        listOf("10", "2.5", "378"),
                        listOf("11", "2.5", "352"),
                        listOf("12", "5", "322"),
                        listOf("13", "5", "316"),
                        listOf("14", "10", "225"),
                        listOf("15", "10", "249"),
                        listOf("16", "20", "165"),
                        listOf("17", "20", "174"),
                        listOf("18", "40", "102"),
                        listOf("19", "40", "116"),
                        listOf("20", "80", "74"),
                        listOf("21", "80", "65")
                    )


                    xslxService.generate(
                        sampleData
//                        runInfo = runInfo!!,
//                        samples = samplesController.samples.map { it },
//                        plates = samplesController.plates.map { it },
                    )

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