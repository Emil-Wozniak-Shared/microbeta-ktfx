package pl.ejdev.medic.helpers

import javafx.scene.Node
import javafx.scene.control.Control
import org.testfx.api.FxAssert.verifyThat
import org.testfx.api.FxRobot
import org.testfx.api.FxRobotInterface

typealias Selector = String

inline fun <reified T : Control> Selector.verify(crossinline check: T.() -> Boolean) {
    verifyThat<T>(this) { check(it) }
}

inline fun <reified T : Control> T.verify(crossinline check: T.() -> Boolean) {
    verifyThat<T>(this) { check(it) }
}

infix fun FxRobot.click(node: Node): FxRobotInterface = this.clickOn(node)