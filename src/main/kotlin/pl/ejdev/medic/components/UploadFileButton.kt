package pl.ejdev.medic.components

import javafx.stage.FileChooser
import ktfx.layouts.button
import org.koin.java.KoinJavaComponent.inject
import pl.ejdev.medic.architecture.ria.dto.RiaResultReadCommand
import pl.ejdev.medic.architecture.ria.usecase.RiaResultReadUseCase
import pl.ejdev.medic.controller.MainController

private const val FILE_CHOOSER_TITLE = "Select a File to Upload"
private const val WITH_TXT = "*.txt"
private const val TEXT_FILES = "Text Files"

enum class Extension(
    val description: String,
    val pattern: String
) {
    TXT(TEXT_FILES, WITH_TXT)
}

fun uploadFileButton(
    label: String,
    vararg extensions: Extension,
    successAction: (List<String>) -> Unit
) = button(label) {
    val mainController: MainController by inject(MainController::class.java)
    val riaResultReadUseCase: RiaResultReadUseCase by inject(RiaResultReadUseCase::class.java)

    val fileChooser = FileChooser().apply {
        title = FILE_CHOOSER_TITLE
        extensionFilters += extensions.map { FileChooser.ExtensionFilter(it.description, it.pattern) }
    }

    this.setOnAction {
        fileChooser.showOpenDialog(mainController.primaryStage())
            ?.readText()
            ?.let { RiaResultReadCommand(it) }
            ?.let(riaResultReadUseCase::handle)
            ?.let { successAction(it.lines) }
    }
}