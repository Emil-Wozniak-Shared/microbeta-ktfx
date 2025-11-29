package pl.ejdev.medic.architecture.xlsx.dto

import pl.ejdev.medic.architecture.shared.dto.Result

class ReadXlsxResult(
    val data: List<List<String>>
): Result