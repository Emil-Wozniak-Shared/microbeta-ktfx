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
        S("Sample"),
        X("-")
    }

    companion object {
        const val ID = "id"
        const val POSITION = "position"
        const val CPM = "ccpm1"
        const val CPM_PERCENTAGE = "ccpm1Percentage"
        const val TYPE = "type"
    }
}
