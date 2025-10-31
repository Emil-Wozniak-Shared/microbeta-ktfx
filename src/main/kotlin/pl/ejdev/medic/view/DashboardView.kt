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
import pl.ejdev.medic.service.XslxService
import pl.ejdev.medic.utils.classes
import pl.ejdev.medic.utils.listenText
import java.io.File

private const val TITLE = "Medic"
private const val UPLOAD = "Upload"

fun dashboardView() = stackPane {
    val mainController: MainController by inject(MainController::class.java)
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

                    samplesController.runInformation.get()
                    val hormone: String = "Kortyzol"
                    val date: String = "2019-01-08 00:00:00"
                    val subject: String = "Owce"
                    val initialData: List<List<String>> = listOf(
                        listOf("Total", "1976"),
                        listOf("Total", "1982"),
                        listOf("Bg", "32"),
                        listOf("Bg", "36"),
                        listOf("Bo", "458"),
                        listOf("Bo", "459"),
                        listOf("Bo", "447"),
                        listOf("1.25", "412"),
                        listOf("1.25", "390"),
                        listOf("2.5", "378"),
                        listOf("2.5", "352"),
                        listOf("5.0", "322"),
                        listOf("5.0", "316"),
                        listOf("10", "225"),
                        listOf("10", "249"),
                        listOf("20", "165"),
                        listOf("20", "174"),
                        listOf("40", "102"),
                        listOf("40", "116"),
                        listOf("80", "74"),
                        listOf("80", "65")
                    )
                    val cpmData: List<Int> = listOf(
                        219, 224, 164, 191, 226, 230, 293, 279, 325, 347, 386, 388, 372, 379, 293, 330, 252, 237,
                        225, 247, 211, 220, 313, 295, 338, 341, 335, 362, 368, 392, 402, 402, 237, 220, 264, 295,
                        359, 352, 400, 403, 411, 437, 477, 469, 454, 474, 483, 450, 412, 440, 262, 201, 250, 242,
                        227, 270, 298, 299, 341, 300, 392, 410, 442, 413, 453, 449, 440, 477, 465, 469, 417, 373,
                        297, 286, 341, 342, 375, 437, 392, 399, 391, 421, 452, 421, 468, 494, 473, 470, 395, 404,
                        358, 355, 352, 373, 431, 387, 441, 456, 417, 460, 186, 176, 157, 158, 161, 138, 141, 150,
                        194, 177, 17, 20, 235, 243, 309, 290, 335, 341, 396, 371, 400, 407, 412, 423, 375, 403,
                        372, 365, 412, 358, 389, 432, 442, 446, 472, 435, 473, 456, 441, 440, 438, 472, 419, 444,
                        382, 415, 433, 414, 455, 426, 482, 488, 248, 245, 235, 246, 187, 169, 199, 182, 228, 242,
                        266, 293, 341, 358, 330, 296, 245, 259, 236, 262, 287, 284, 252, 265, 237, 252, 291, 296,
                        233, 243, 229, 249, 259, 234, 240, 244, 279, 294, 312, 310, 326, 354, 321, 358, 229, 225,
                        215, 220, 262, 271, 221, 236, 220, 226, 240, 258, 308, 297, 307, 299, 259, 255, 222, 212,
                        192, 196, 199, 166, 196, 182, 305, 307, 341, 340, 361, 370, 246, 246, 204, 211, 194, 167,
                        166, 181, 196, 174, 191
                    )
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


                    xslxService.generate(
                        mainController.primaryStage(), hormone, date, subject, initialData, cpmData, standardPoints
                    )
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