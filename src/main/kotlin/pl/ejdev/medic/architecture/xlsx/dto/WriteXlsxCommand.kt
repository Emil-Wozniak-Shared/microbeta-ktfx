package pl.ejdev.medic.architecture.xlsx.dto

import pl.ejdev.medic.model.RunInformation
import pl.ejdev.medic.model.Sample

class WriteXlsxCommand(
    val hormone: String,
    val date: String,
    val subject: String,
    val samples: List<Sample>
) {
    companion object {
        fun from(runInformation: RunInformation, samples: List<Sample>) = WriteXlsxCommand(
            hormone = runInformation.name,
            date = runInformation.date,
            subject = "Owce",
            samples = samples
        )
    }
}