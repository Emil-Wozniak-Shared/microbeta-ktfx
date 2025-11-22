package pl.ejdev.medic.model

data class Settings(
    val curve: Curve = Curve(),
    val exportType: ExportType = ExportType.XLSX
) {

    data class Curve(
        val totalCount: Int = 2,
        val nsbCount: Int = 3,
        val zeroCount: Int = 3,
        val calibration: Calibration = Calibration()
    )

    data class Calibration(
        val values: List<String> = CALIBRATION_DEFAULT_VALUES,
        val repeats: Int = 2,
    )

    enum class ExportType {
        XLSX
    }

    companion object {
        val CALIBRATION_DEFAULT_VALUES = listOf("1.25", "2.5", "5", "10", "40", "80")
    }
}

