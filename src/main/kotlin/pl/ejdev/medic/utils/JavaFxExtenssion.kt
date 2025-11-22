@file:Suppress("FunctionName")

package pl.ejdev.medic.utils

import javafx.scene.Node
import javafx.scene.control.TableColumn
import javafx.scene.control.TextField
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.paint.Color
import kotlin.math.roundToInt

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

class StyleComposer {
    private var style = ""
    fun `background-color`(value: Color) {
        style += "-fx-background-color: ${value.toHexString()};"
    }

    fun `text-fill`(value: Color) {
        style += "-fx-text-fill: ${value.toHexString()};"
    }

    private fun format(`val`: Double): String {
        val `in` = Integer.toHexString((`val` * 255).roundToInt())
        return if (`in`.length == 1) "0$`in`" else `in`
    }

    fun Color.toHexString(): String = buildString {
        append("#")
        append(format(red))
        append(format(green))
        append(format(blue))
        append(format(opacity))
    }.uppercase()

    fun build() = style
}

fun fxStyle(compose: StyleComposer.() -> Unit): String {

    return StyleComposer().apply(compose).build()
}