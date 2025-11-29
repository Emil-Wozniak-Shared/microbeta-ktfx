package pl.ejdev.medic.architecture.xlsx.usecase.read

import pl.ejdev.medic.architecture.xlsx.dto.ReadXlsxCommand
import pl.ejdev.medic.architecture.xlsx.dto.ReadXlsxEvent
import pl.ejdev.medic.architecture.xlsx.port.read.ReadXlsxPort

class ReadXlsxUseCase(
    private val readXlsxPort: ReadXlsxPort
) {
    fun handle(command: ReadXlsxCommand) = readXlsxPort.handle(ReadXlsxEvent)
}