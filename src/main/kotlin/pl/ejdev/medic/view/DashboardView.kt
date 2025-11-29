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
import pl.ejdev.medic.architecture.xlsx.dto.WriteXlsxCommand
import pl.ejdev.medic.architecture.xlsx.usecase.write.WriteXlsxUseCase
import pl.ejdev.medic.components.Extension
import pl.ejdev.medic.components.samplesTable
import pl.ejdev.medic.components.uploadFileButton
import pl.ejdev.medic.controller.SamplesController
import pl.ejdev.medic.model.RunInformation
import pl.ejdev.medic.utils.classes
import java.io.File

private const val UPLOAD = "Upload"
private const val SAVE_XSLX = "Save XSLX"

private val savedLabel = Label("")

fun dashboardView() = stackPane {
    val samplesController: SamplesController by inject(SamplesController::class.java)
    val writeXlsxUseCase: WriteXlsxUseCase by inject(WriteXlsxUseCase::class.java)

    classes(Style.MAIN_CONTENT)
    vbox {
        hbox {
            button(SAVE_XSLX) {
                isDisable = samplesController.isNotEmpty()
                onAction {
                    WriteXlsxCommand
                        .from(
                            runInformation = samplesController.runInformation.get()!!,
                            samples = samplesController.samples.map { it }.toList()
                        )
                        .let { writeXlsxUseCase.handle(it) }
                    savedLabel.text = "Saved!"
                }
            }
            addChild(savedLabel)
        }
        addChild(
            uploadFileButton(
                label = UPLOAD,
                extensions = arrayOf(Extension.TXT),
                successAction = {
                    savedLabel.text = ""
                    samplesController.bindData(it)
                }
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