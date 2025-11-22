package pl.ejdev.medic.model

data class Sample(
    val id: String,
    val position: String,
    val ccpm1: Int,
    val ccpm1Percentage: Double,
    val type: Type
) {

    enum class Type(val value: String) {
        T("Total"),
        N("NSB"),
        O("Zero"),
        C("Calibration"),
        S("Sample"),
        X("-");

        fun isTotal() = this == T
        fun isNsb() = this == N
        fun isZero() = this == N
        fun isControlPoint() = this == N
    }


    companion object {
        const val ID = "id"
        const val POSITION = "position"
        const val CPM = "ccpm1"
        const val CPM_PERCENTAGE = "ccpm1Percentage"
        const val TYPE = "type"
    }
}
