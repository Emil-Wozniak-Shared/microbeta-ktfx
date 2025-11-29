package pl.ejdev.medic.architecture.ria.usecase

import pl.ejdev.medic.architecture.ria.dto.RiaResultReadCommand
import pl.ejdev.medic.architecture.ria.dto.RiaResultReadEvent
import pl.ejdev.medic.architecture.ria.port.read.RiaResultReadPort

class RiaResultReadUseCase(
    private val riaResultReadPort: RiaResultReadPort
) {
    fun handle(command: RiaResultReadCommand) =
        command
            .run { RiaResultReadEvent(text) }
            .let { riaResultReadPort.handle(it) }
}