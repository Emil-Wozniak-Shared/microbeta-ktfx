package pl.ejdev.medic.components

import javafx.stage.FileChooser
import ktfx.layouts.button
import org.koin.java.KoinJavaComponent.inject
import pl.ejdev.medic.controller.FileReadController
import pl.ejdev.medic.controller.MainController

private const val FILE_CHOOSER_TITLE = "Select a File to Upload"

enum class Extension(
    val description: String,
    val pattern: String
) {
    TXT("Text Files", "*.txt")
}

fun uploadFileButton(
    label: String,
    vararg extensions: Extension,
    successAction: (List<String> ) -> Unit
) = button(label) {
    val mainController: MainController by inject(MainController::class.java)
    val readController: FileReadController by inject(FileReadController::class.java)

    val fileChooser = FileChooser().apply {
        title = FILE_CHOOSER_TITLE
        extensionFilters.addAll(
            extensions.map {
                FileChooser.ExtensionFilter(it.description, it.pattern)
            }
        )
    }

    this.setOnAction {
        fileChooser
            .showOpenDialog(mainController.primaryStage())
            ?.let(readController::onUploadFile)
            ?.let { successAction(it) }
    }
}