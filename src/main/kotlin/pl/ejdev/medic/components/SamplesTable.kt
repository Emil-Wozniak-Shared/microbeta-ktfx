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
import javafx.scene.paint.Color
import ktfx.layouts.tableView
import org.koin.java.KoinJavaComponent.inject
import pl.ejdev.medic.controller.SamplesController
import pl.ejdev.medic.model.Sample
import pl.ejdev.medic.model.Sample.Type
import pl.ejdev.medic.service.ControlCurveProvider
import pl.ejdev.medic.utils.fxStyle
import pl.ejdev.medic.utils.tableColumn

private const val ID = "ID"
private const val POSITION = "Position"
private const val CPM = "CCPM1"
private const val CPM_PERCENTAGE = "CCPM1 %"
private const val TYPE = "Type"

fun samplesTable(): TableView<Sample> {
    val samplesController: SamplesController by inject(SamplesController::class.java)
    val controlCurveProvider: ControlCurveProvider by inject(ControlCurveProvider::class.java)

    return tableView(samplesController.samples) {
        VBox.setVgrow(this, Priority.ALWAYS)
        HBox.setHgrow(this, Priority.ALWAYS)
        prefHeight = 500.0
        prefWidth = 800.0
        isEditable = true // ✅ Allow editing

        val typeOptions = FXCollections.observableArrayList(*Type.entries.toTypedArray())

        items = samplesController.samples
        columns.addAll(
            tableColumn<Sample, String>(ID, Sample.ID),
            tableColumn<Sample, String>(POSITION, Sample.POSITION),
            tableColumn<Sample, Int>(CPM, Sample.CPM),
            tableColumn<Sample, Double>(CPM_PERCENTAGE, Sample.CPM_PERCENTAGE),
            tableColumn<Sample, Type>(TYPE, Sample.TYPE) {
                sampleConfiguration(
                    this@tableColumn,
                    typeOptions,
                    this@tableView,
                    controlCurveProvider
                )
            }
        )
    }
}

private fun sampleConfiguration(
    column: TableColumn<Sample, Type>,
    typeOptions: ObservableList<Type>,
    view: TableView<Sample>,
    controlCurveProvider: ControlCurveProvider,
) {
    column.cellValueFactory = PropertyValueFactory(Sample.TYPE)
    column.cellFactory = ComboBoxTableCell.forTableColumn(typeOptions)

    column.setCellFactory {
        object : ComboBoxTableCell<Sample, Type>(typeOptions) {
            override fun updateItem(item: Type?, empty: Boolean) {
                super.updateItem(item, empty)
                if (empty || item == null) {
                    text = ""
                    style = ""
                    return
                }
                val rowIndex = tableRow.index
                text = controlCurveProvider.resolveType(rowIndex, item)
                style = when (item) {
                    Type.T -> fxStyle {
                        `background-color`(Color.LIGHTGREEN)
                        `text-fill`(Color.BLACK)
                    }

                    Type.N -> fxStyle {
                        `background-color`(Color.LIGHTBLUE)
                        `text-fill`(Color.BLACK)
                    }

                    Type.O -> fxStyle {
                        `background-color`(Color.LIGHTYELLOW)
                        `text-fill`(Color.BLACK)
                    }

                    Type.C -> fxStyle {
                        `background-color`(Color.VIOLET)
                        `text-fill`(Color.BLACK)
                    }

                    Type.S -> fxStyle {
                        `background-color`(Color.DARKGRAY)
                        `text-fill`(Color.BLACK)
                    }

                    Type.X -> fxStyle {
                        `background-color`(Color.RED)
                        `text-fill`(Color.BLACK)
                    }
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
