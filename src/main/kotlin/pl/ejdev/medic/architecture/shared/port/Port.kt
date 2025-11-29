package pl.ejdev.medic.architecture.shared.port

import pl.ejdev.medic.architecture.shared.dto.Event
import pl.ejdev.medic.architecture.shared.dto.Result

interface Port<E: Event, R: Result> {
    fun handle(event: E): R
}