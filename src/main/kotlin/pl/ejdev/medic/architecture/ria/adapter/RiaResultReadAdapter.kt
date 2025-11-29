package pl.ejdev.medic.architecture.ria.adapter

import pl.ejdev.medic.architecture.ria.dto.RiaResultReadEvent
import pl.ejdev.medic.architecture.ria.port.read.RiaResultReadPort
import pl.ejdev.medic.architecture.xlsx.dto.RiaResultReadResult

class RiaResultReadAdapter: RiaResultReadPort {
    override fun handle(event: RiaResultReadEvent): RiaResultReadResult =
        event.text
            .split("\n")
            .onEach { println(it) }
            .let { RiaResultReadResult(it) }
}