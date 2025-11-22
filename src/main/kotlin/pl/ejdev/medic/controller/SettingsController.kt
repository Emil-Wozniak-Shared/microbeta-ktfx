package pl.ejdev.medic.controller

import javafx.beans.property.SimpleObjectProperty
import pl.ejdev.medic.model.Settings
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

private const val CALIBRATION_VALUE_SEPARATOR = ";"

class SettingsController {
    private val configDir: File = File(System.getProperty("user.home"), ".medic")
    private val file: File = File(configDir, "app.properties")
    val settingsProperty = SimpleObjectProperty(load())

    init {
        ensureConfig()
        settingsProperty.set(load())
    }

    /**
     * Ensures that ~/.medic directory and app.properties file exist.
     * If not, they are created with default settings.
     */
    private fun ensureConfig() {
        if (!configDir.exists()) {
            configDir.mkdirs()
            println("Created config directory: ${configDir.absolutePath}")
        }

        if (!file.exists()) {
            println("Creating default config file at: ${file.absolutePath}")
            save(Settings()) // creates default file
        }
    }

    /**
     * Loads settings from ~/.medic/app.properties.
     * Returns defaults if file is missing or corrupted.
     */
    fun load(): Settings {
        if (!file.exists()) {
            return Settings()
        }

        val props = Properties()
        FileInputStream(file).use { props.load(it) }

        return Settings(
            curve = Settings.Curve(
                totalCount = props.getProperty("curve.totalCount")?.toIntOrNull() ?: 2,
                nsbCount = props.getProperty("curve.nsbCount")?.toIntOrNull() ?: 3,
                zeroCount = props.getProperty("curve.zeroCount")?.toIntOrNull() ?: 3,
                calibration = Settings.Calibration(
                    values = props.getProperty("curve.calibration.values")
                        ?.split(CALIBRATION_VALUE_SEPARATOR)
                        ?.toList()
                        ?: Settings.CALIBRATION_DEFAULT_VALUES,
                    repeats = props.getProperty("curve.calibration.repeats")?.toIntOrNull() ?: 2,
                )
            ),
            exportType = props.getProperty("exportType")
                ?.let { runCatching { Settings.ExportType.valueOf(it) }.getOrNull() }
                ?: Settings.ExportType.XLSX
        )
    }

    /**
     * Saves settings to ~/.medic/app.properties and updates the property.
     */
    fun save(settings: Settings) {
        val props = Properties().apply {
            setProperty("curve.totalCount", settings.curve.totalCount.toString())
            setProperty("curve.nsbCount", settings.curve.nsbCount.toString())
            setProperty("curve.zeroCount", settings.curve.zeroCount.toString())
            setProperty("curve.calibration.repeats", settings.curve.calibration.repeats.toString())
            setProperty(
                "curve.calibration.values", settings.curve.calibration.values.joinToString(
                    CALIBRATION_VALUE_SEPARATOR
                )
            )
            setProperty("exportType", settings.exportType.name)
        }

        FileOutputStream(file).use { props.store(it, "App Settings") }
        settingsProperty.set(settings)

        println("✅ Saved settings: $settings")
    }
}