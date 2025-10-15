package pl.ejdev.medic

import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Labeled
import javafx.scene.layout.StackPane
import javafx.stage.Stage
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.testfx.api.FxRobot
import org.testfx.framework.junit5.ApplicationExtension
import org.testfx.framework.junit5.ApplicationTest
import org.testfx.util.WaitForAsyncUtils.waitFor
import pl.ejdev.medic.helpers.click
import pl.ejdev.medic.helpers.verify
import java.util.concurrent.TimeUnit.MILLISECONDS

@ExtendWith(ApplicationExtension::class)
class ClickableButtonSpec : ApplicationTest() {
    private lateinit var button: Button

    override fun start(stage: Stage) {
        button = Button("click me!")
        button.id = "myButton"
        button.onAction = EventHandler { _ -> button.text = "clicked!" }
        stage.setScene(Scene(StackPane(button), 100.0, 100.0))
        stage.show()
    }

    @Test
    fun `should contain button with text`(robot: FxRobot) {
        // given:
        button.verify { text == "click me!" }
        "#myButton".verify<Labeled> { text == "click me!" }
        // when:
        waitFor(300, MILLISECONDS) {
            robot click button
            button.isVisible
        }
        // then:
        ".button".verify<Labeled> { text == "clicked!" }
    }
}