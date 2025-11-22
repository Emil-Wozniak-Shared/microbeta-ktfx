package pl.ejdev.medic.model.xlsx

import pl.ejdev.medic.model.Sample

data class CreateXlsxCommand(
    val hormone: String,
    val date: String,
    val subject: String,
    val samples: List<Sample>,
    val standardPoints: List<List<String>>
)
