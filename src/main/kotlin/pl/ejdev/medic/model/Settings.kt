package pl.ejdev.medic.model

data class Settings(
    val curve: Curve = Curve(),
    val exportType: ExportType = ExportType.XLSX
) {

    data class Curve(
        val totalCount: Int = 2,
        val nsbCount: Int = 3,
        val zeroCount: Int = 3
    )

    enum class ExportType {
        XLSX
    }
}

