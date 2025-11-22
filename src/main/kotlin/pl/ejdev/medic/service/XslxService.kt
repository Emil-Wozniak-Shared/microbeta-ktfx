package pl.ejdev.medic.service

import javafx.stage.Stage
import pl.ejdev.medic.model.xlsx.CreateXlsxCommand

interface XslxService {
    fun generate(stage: Stage, command: CreateXlsxCommand)
}
