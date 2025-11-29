package pl.ejdev.medic.architecture.xlsx.usecase.write

import pl.ejdev.medic.architecture.shared.dto.NoResult
import pl.ejdev.medic.architecture.xlsx.dto.WriteXlsxCommand
import pl.ejdev.medic.architecture.xlsx.dto.WriteXlsxEvent
import pl.ejdev.medic.architecture.xlsx.port.write.WriteXlsxPort
import pl.ejdev.medic.service.ControlCurveProvider

class WriteXlsxUseCase(
    private val writeXlsxPort: WriteXlsxPort,
    private val controlCurveProvider: ControlCurveProvider
) {
    fun handle(command: WriteXlsxCommand): NoResult {
        val (_, values) = controlCurveProvider.calibrations()
        val event = WriteXlsxEvent.from(command, values)
        return writeXlsxPort.handle(event)
    }

}