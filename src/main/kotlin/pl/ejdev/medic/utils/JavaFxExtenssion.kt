package pl.ejdev.medic.utils

import javafx.scene.Node
import javafx.scene.control.TableColumn
import javafx.scene.control.TextField
import javafx.scene.control.cell.PropertyValueFactory

fun Node.classes(vararg names: String) = apply {
    styleClass.addAll(names)
}

fun TextField.listenText(action: (text: String) -> Unit) {
    this.textProperty().addListener { _, _, newValue ->
        action(newValue)
    }
}

inline fun <reified SOURCE, reified PROP> tableColumn(
    name: String,
    propName: String,
    factory: TableColumn<SOURCE, PROP>.(propName: String) -> Unit = {
        cellValueFactory = PropertyValueFactory(propName)
    }
) = TableColumn<SOURCE, PROP>(name).apply {
    factory(propName)
}