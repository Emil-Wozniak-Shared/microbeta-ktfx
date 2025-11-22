package pl.ejdev.medic

import java.io.IOException
import java.util.Properties

object BuildInfo {
    private val props = Properties()

    init {
        try {
            BuildInfo::class.java.getResourceAsStream("/build.properties").use { stream ->
                if (stream != null) props.load(stream)
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    val name: String
        get() = props.getProperty("name", "Unknown")

    val version: String
        get() = props.getProperty("version", "Unknown")
}