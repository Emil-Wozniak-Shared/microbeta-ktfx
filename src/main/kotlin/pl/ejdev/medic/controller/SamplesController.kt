package pl.ejdev.medic.controller

import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.collections.FXCollections.observableArrayList
import javafx.collections.ObservableList
import org.koin.java.KoinJavaComponent.inject
import pl.ejdev.medic.model.Plate
import pl.ejdev.medic.model.RunInformation
import pl.ejdev.medic.model.Sample
import pl.ejdev.medic.service.ControlCurveHandler

private const val SAMPLE_START_PATTERN = "\tUnk"
private const val TABULATOR = "\t"

class SamplesController() {
    private val settingsController: SettingsController by inject(SettingsController::class.java)
    private val controlCurveHandler: ControlCurveHandler by inject(ControlCurveHandler::class.java)

    private val _samples = observableArrayList<Sample>()
    private val _runInformation: ObjectProperty<RunInformation?> = SimpleObjectProperty(null)
    private val _plates: ObservableList<Plate> = FXCollections.observableArrayList()

    val samples: ObservableList<Sample> get() = _samples
    val runInformation: ObjectProperty<RunInformation?> get() = _runInformation
    val plates: ObservableList<Plate> get() = _plates

    var currentRunInformation: RunInformation?
        get() = _runInformation.get()
        set(value) {
            _runInformation.set(value)
        }

    fun bindData(data: List<String>) {
        extractRunInformation(data)
        extractPlates(data)
        val parsed = data
            .asSequence()
            .filter { it.contains(SAMPLE_START_PATTERN) }
            .map { it.split(TABULATOR).filter(String::isNotBlank) }
            .mapIndexedNotNull { id, chunks -> tryCreateSample(chunks, id) }
            .toList()

        println("Parsed ${parsed.size} samples")
        _samples.setAll(parsed)
    }

    fun isNotEmpty() = _samples.isNotEmpty()

    private fun extractRunInformation(data: List<String>) {
        val countingProtocol = data.firstNotNullOfOrNull { line ->
            countingProtocolRegex.find(line)?.groupValues?.get(1)?.toIntOrNull()
        }

        val normalizationProtocol = data.firstNotNullOfOrNull { line ->
            normalizationProtocolRegex.find(line)?.groupValues?.get(1)?.toIntOrNull()
        }

        val name = data.firstNotNullOfOrNull { line ->
            nameRegex.find(line)?.groupValues?.get(1)
        }

        if (countingProtocol != null && normalizationProtocol != null && name != null) {
            currentRunInformation = RunInformation(
                countingProtocolNo = countingProtocol,
                normalizationProtocolNo = normalizationProtocol,
                name = name
            )
        }
    }


    fun extractPlates(data: List<String>) {
        // --- Parse Plates ---
        val parsedPlates = mutableListOf<Plate>()
        var currentPlateNumber: Int? = null
        var cassetteInfo = ""
        val rows = mutableMapOf<Char, MutableList<Int>>()
        var readingRows = false

        data.forEach { line ->
            // Start of a plate
            PLATE_PATTERN.find(line)?.let {
                currentPlateNumber = it.groupValues[1].toInt()
                cassetteInfo = ""
                rows.clear()
                readingRows = false
            }

            // Cassette information
            if (line.startsWith("Assay:") || line.startsWith("Assay:-")) {
                cassetteInfo = line.trim()
            }

            // Start reading CCPM1 rows
            if (line.trim() == "CCPM1") {
                readingRows = true
                return@forEach
            }

            // Read rows A-D
            if (readingRows && line.trim().matches(PLATE_ROW_REGEXP)) {

                val parts = line.trim().split(WHITE_CHAR_REGEX)
                val rowLetter = parts[0].first()
                val values = parts.drop(1).map { it.toInt() }
                rows[rowLetter] = values.toMutableList()
            }

            // End of plate
            if (line.startsWith("End of plate") && currentPlateNumber != null) {
                parsedPlates.add(
                    Plate(
                        number = currentPlateNumber,
                        cassetteInfo = cassetteInfo,
                        rows = rows.mapValues { it.value.toList() }
                    )
                )
                currentPlateNumber = null
                cassetteInfo = ""
                rows.clear()
                readingRows = false
            }
        }

        println("Parsed ${parsedPlates.size} plates")
        _plates.setAll(parsedPlates)
    }

    private fun tryCreateSample(chunks: List<String>, id: Int): Sample? = try {
        Sample(
            id = chunks[0],
            position = chunks[1],
            ccpm1 = chunks[2].toInt(),
            ccpm1Percentage = chunks[3].toDouble(),
            type = controlCurveHandler.resolveType(id)
        )
    } catch (e: Exception) {
        println("Parse error: $chunks")
        e.printStackTrace()
        null
    }

    companion object {
        val PLATE_PATTERN = "Plate (\\d+)".toRegex()
        val PLATE_ROW_REGEXP = Regex("[A-D]\\s+\\d.*")
        val countingProtocolRegex = """Counting protocol no:\s*(\d+)""".toRegex()
        val normalizationProtocolRegex = """CPM normalization protocol no:\s*(\d+)""".toRegex()
        val nameRegex = """Name:\s*(.+)""".toRegex()
        val WHITE_CHAR_REGEX = Regex("\\s+")
    }
}