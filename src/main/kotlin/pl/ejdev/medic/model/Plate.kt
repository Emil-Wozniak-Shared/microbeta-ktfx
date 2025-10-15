package pl.ejdev.medic.model

data class Plate(
    val number: Int,
    val cassetteInfo: String,
    val rows: Map<Char, List<Int>> // e.g., 'A' -> [4215, 65, 68, 86, 4419, 1062]
)
