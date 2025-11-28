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

sealed interface FxSize

data class PixelSize(val value: Int) : FxSize

val Int.px: PixelSize
    get() = PixelSize(this)

enum class FontWeight {
    BOLD
}

private const val FONT_SIZE = "font-size"
private const val HASH = "#"

@Suppress("FunctionName")
class StyleComposer {
    private var style = ""

    fun `background-color`(value: Color) {
        "background-color" eq value.toHexString()
    }

    fun `text-fill`(value: Color) {
        "text-fill" eq value.toHexString()
    }

    fun `font-size`(value: FxSize) {
        style += when (value) {
            is PixelSize -> FONT_SIZE eq "${value.value}$STYLE_SEPARATOR"
        }
    }

    fun `font-weight`(weight: FontWeight) {
        "font-weight" eq weight.name.lowercase()
    }

    private infix fun String.eq(value: String) {
        this@StyleComposer.style += "$FX_PREFIX$this: $value$STYLE_SEPARATOR"
    }

    private fun format(value: Double): String {
        val hexText = Integer.toHexString((value * 255).roundToInt())
        return if (hexText.length == 1) "0$hexText" else hexText
    }

    private fun Color.toHexString(): String =
        "$HASH${format(red)}${format(green)}${format(blue)}${format(opacity)}"
            .uppercase()

    fun build() = style

    private companion object {
        const val FX_PREFIX = "-fx-"
        const val STYLE_SEPARATOR = ";"
    }
}

fun fxStyle(compose: StyleComposer.() -> Unit): String = StyleComposer().apply(compose).build()