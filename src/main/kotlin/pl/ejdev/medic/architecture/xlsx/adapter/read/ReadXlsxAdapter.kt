package pl.ejdev.medic.architecture.xlsx.adapter.read

import org.dhatim.fastexcel.reader.ReadableWorkbook
import pl.ejdev.medic.architecture.xlsx.dto.ReadXlsxEvent
import pl.ejdev.medic.architecture.xlsx.dto.ReadXlsxResult
import pl.ejdev.medic.architecture.xlsx.port.read.ReadXlsxPort
import java.io.File
import java.io.FileInputStream
import kotlin.use

class ReadXlsxAdapter: ReadXlsxPort {
    override fun handle(event: ReadXlsxEvent): ReadXlsxResult {
        val sb = StringBuilder()
        val file = File(this::class.java.getResource("/sample.xlsx")?.toURI() ?: error("Resource not found"))
        FileInputStream(file).use { inputStream ->
            ReadableWorkbook(inputStream).use { workbook ->
                val sheet = workbook.sheets.toList()[1]
                sheet.openStream().use { rows ->
                    rows.toList().forEachIndexed { _, row ->
                        (0..18).forEach { colIndex ->
                            try {
                                val cell = row.getCell(colIndex)
                                val value = cell?.rawValue?.trim() ?: ""
                                val formula = cell?.formula?.trim()
                                if (formula != null) {
                                    sb.append("=$formula")
                                } else {
                                    sb.append(value)
                                }
                                if (colIndex < 18) sb.append(" | ")
                            } catch (_: Exception) {

                            }
                        }
                        sb.append("\n")
                    }
                    rows
                }
            }
        }

        return sb
            .split("\n")
            .map { it.split(" | ") }
            .let { ReadXlsxResult(it) }
    }
}