package pl.ejdev.medic.controller

import javafx.stage.Stage

class MainController {
    private lateinit var stage: Stage

    fun primaryStage() = stage

    fun bindStage(stage: Stage) {
        this.stage = stage
    }
}