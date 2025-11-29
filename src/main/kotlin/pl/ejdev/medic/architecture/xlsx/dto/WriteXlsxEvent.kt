package pl.ejdev.medic.architecture.xlsx.dto

import pl.ejdev.medic.architecture.shared.dto.Event
import pl.ejdev.medic.model.Sample

data class WriteXlsxEvent(
    val hormone: String,
    val date: String,
    val subject: String,
    val samples: List<Sample>,
    val controlCurveValues: List<String>
) : Event {
    companion object {
        fun from(command: WriteXlsxCommand, controlCurveValues: List<String>) = WriteXlsxEvent(
            command.hormone,
            command.date,
            command.subject,
            command.samples,
            controlCurveValues
        )
    }
}
