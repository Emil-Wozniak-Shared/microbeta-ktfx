package pl.ejdev.medic.components

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.ComboBoxTableCell
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import ktfx.layouts.KtfxLayoutDslMarker
import ktfx.layouts.tableView
import org.koin.java.KoinJavaComponent.inject
import pl.ejdev.medic.controller.SamplesController
import pl.ejdev.medic.model.Sample
import pl.ejdev.medic.utils.tableColumn

private const val ID = "ID"
private const val POSITION = "Position"
private const val CPM = "CCPM1"
private const val CPM_PERCENTAGE = "CCPM1 %"
private const val TYPE = "Type"

fun samplesTable(): TableView<Sample> {
    val samplesController: SamplesController by inject(SamplesController::class.java)

    return tableView(samplesController.samples) {
        VBox.setVgrow(this, Priority.ALWAYS)
        HBox.setHgrow(this, Priority.ALWAYS)
        prefHeight = 500.0
        prefWidth = 800.0
        isEditable = true // ✅ Allow editing

        val typeOptions = FXCollections.observableArrayList(*Sample.Type.entries.toTypedArray())

        items = samplesController.samples
        columns.addAll(
            tableColumn<Sample, String>(ID, Sample.ID),
            tableColumn<Sample, String>(POSITION, Sample.POSITION),
            tableColumn<Sample, Int>(CPM, Sample.CPM),
            tableColumn<Sample, Double>(CPM_PERCENTAGE, Sample.CPM_PERCENTAGE),
            tableColumn<Sample, Sample.Type>(TYPE, Sample.TYPE) { sampleConfiguration(this@tableColumn, typeOptions, this@tableView) }
        )
    }
}

private fun sampleConfiguration(
    column: TableColumn<Sample, Sample.Type>,
    typeOptions: ObservableList<Sample.Type>,
    view: TableView<Sample>
) {
    column.cellValueFactory = PropertyValueFactory(Sample.TYPE)
    column.cellFactory = ComboBoxTableCell.forTableColumn(typeOptions)

    column.setCellFactory {
        object : ComboBoxTableCell<Sample, Sample.Type>(typeOptions) {
            override fun updateItem(item: Sample.Type?, empty: Boolean) {
                super.updateItem(item, empty)
                if (empty || item == null) {
                    text = ""
                    style = ""
                    return
                }

                text = item.value
                style = when (item) {
                    Sample.Type.T -> "-fx-background-color: lightgreen; -fx-text-fill: black;"
                    Sample.Type.N -> "-fx-background-color: lightblue; -fx-text-fill: black;"
                    Sample.Type.O -> "-fx-background-color: lightyellow; -fx-text-fill: black;"
                    Sample.Type.S -> "-fx-background-color: darkgray; -fx-text-fill: white;"
                    Sample.Type.X -> "-fx-background-color: red; -fx-text-fill: white;"
                }
            }
        }
    }
    column.setOnEditCommit { event ->
        val sample = event.rowValue
        val newType = event.newValue
        if (newType != null) {
            val updated = sample.copy(type = newType)
            val index = view.items.indexOf(sample)
            if (index >= 0) view.items[index] = updated
        }
    }
}
