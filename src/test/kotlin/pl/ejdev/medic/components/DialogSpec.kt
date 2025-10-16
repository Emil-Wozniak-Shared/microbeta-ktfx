package pl.ejdev.medic.components

import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.StackPane
import javafx.scene.text.Text
import javafx.stage.Stage
import ktfx.coroutines.onAction
import ktfx.layouts.button
import ktfx.layouts.label
import ktfx.layouts.scene
import ktfx.layouts.stackPane
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.testfx.api.FxRobot
import org.testfx.framework.junit5.ApplicationExtension
import org.testfx.framework.junit5.ApplicationTest
import org.testfx.util.WaitForAsyncUtils.waitFor
import org.testfx.util.WaitForAsyncUtils.waitForFxEvents
import pl.ejdev.medic.helpers.click
import pl.ejdev.medic.helpers.verify
import java.util.concurrent.TimeUnit.MILLISECONDS

private const val TITLE = "TITLE"
private const val CONTENT = "content"

@ExtendWith(ApplicationExtension::class)
class DialogSpec : ApplicationTest() {
    private lateinit var button: Button
    private lateinit var closeButton: Button
    private var dialog: Stage? = null

    override fun start(stage: Stage) = stage.run {
        setScene(scene(100.0, 100.0) {
            stackPane {
                button = button("click") btn@{
                    this@btn.id = "show-button"
                    onAction {
                        dialog = dialog(stage, TITLE) {
                            this@dialog.id = "dialog"
                            label(CONTENT)
                            closeButton = button("OK") closeBtn@{
                                this@closeBtn.id = "close"
                                onAction = EventHandler { close() }
                            }
                        }
                    }
                }
            }
        })
        show()
    }

    @Test
    fun `should contain button with text`(robot: FxRobot) {
        // setup
        // show a dialog
        waitFor(300, MILLISECONDS) {
            robot click button
            button.isVisible
        }
        // expect
        // dialog has a label
        "#dialog".verify<StackPane> { this.children.any { it is Label } }
        // dialog has a button
        "#dialog".verify<StackPane> { this.children.any { it is Button } }
        // cleanup
        // close dialog after test
        waitFor(300, MILLISECONDS) {
            robot click closeButton
            button.isVisible
        }
    }
}