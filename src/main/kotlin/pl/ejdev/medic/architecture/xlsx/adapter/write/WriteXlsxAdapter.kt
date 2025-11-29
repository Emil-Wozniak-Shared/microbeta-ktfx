package pl.ejdev.medic.architecture.xlsx.adapter.write

import org.apache.commons.lang3.StringUtils.EMPTY
import org.dhatim.fastexcel.Workbook
import org.dhatim.fastexcel.Worksheet
import pl.ejdev.medic.BuildInfo
import pl.ejdev.medic.architecture.shared.dto.NoResult
import pl.ejdev.medic.architecture.xlsx.dto.WriteXlsxEvent
import pl.ejdev.medic.architecture.xlsx.port.write.WriteXlsxPort
import pl.ejdev.medic.data.DATA_POMIARU
import pl.ejdev.medic.data.HORMONE
import pl.ejdev.medic.data.XSLX_DATA
import pl.ejdev.medic.model.Sample
import java.io.File

private const val TYPE = "xlsx"
private const val SHEET_FIRST_NAME = "Wydruk"
private const val INT_FORMAT = "#,##0"
private const val FORMULA_SIGN = "="
private const val LOG_FUNCTION = "=LOG"

class WriteXlsxAdapter : WriteXlsxPort {
    override fun handle(event: WriteXlsxEvent): NoResult {
        val (hormone, date, _, samples, controlCurveValues) = event
        val file = file(hormone, date)
        val workbook = Workbook(file.outputStream(), BuildInfo.name, null)
        val sheet = workbook.newWorksheet(SHEET_FIRST_NAME)

        val controlCurvePoints = controlCurvePoints(samples, controlCurveValues)
        XSLX_DATA.forEachIndexed { rowIndex, rowData ->
            rowData.forEachIndexed { colIndex, cellValue ->
                when (cellValue) {
                    is String -> when {
                        cellValue.startsWith(FORMULA_SIGN) -> stringFormula(cellValue, sheet, rowIndex, colIndex)
                        cellValue.toDoubleOrNull() != null -> stringNumberValue(sheet, rowIndex, colIndex, cellValue)
                        else -> otherString(cellValue, sheet, rowIndex, colIndex, hormone, date)
                    }

                    is Sample.Type -> {
                        val index = rowData[0].toString().toInt()
                        if (cellValue == Sample.Type.C) {
                            val values = controlCurvePoints.find { it[2] == index.toString() }
                            if (values != null) {

                            }
                        }
                        val sample = samples[index - 1].ccpm1
                        sheet.value(rowIndex, colIndex, sample.toDouble())
                        sheet.style(rowIndex, colIndex).format(INT_FORMAT).set()
                    }
                }
            }
            sheet.flush()
        }
        workbook.finish()
        return NoResult
    }

    private fun otherString(
        cellValue: String, sheet: Worksheet, rowIndex: Int, colIndex: Int, hormone: String, date: String
    ) {
        when (cellValue) {
            HORMONE -> sheet.value(rowIndex, colIndex, hormone)
            DATA_POMIARU -> sheet.value(rowIndex, colIndex, date)
            else -> sheet.value(rowIndex, colIndex, cellValue)
        }
    }

    private fun stringNumberValue(sheet: Worksheet, rowIndex: Int, colIndex: Int, cellValue: String) {
        sheet.value(rowIndex, colIndex, cellValue.toDouble())
        sheet.style(rowIndex, colIndex).format(INT_FORMAT).set()
    }

    private fun stringFormula(cellValue: String, sheet: Worksheet, rowIndex: Int, colIndex: Int) {
        val expression = cellValue.replace(FORMULA_SIGN, EMPTY)
        sheet.formula(rowIndex, colIndex, expression)
        if (!cellValue.uppercase().startsWith(LOG_FUNCTION)) {
            sheet.style(rowIndex, colIndex).format(INT_FORMAT).set()
        }
    }

    private fun controlCurvePoints(samples: List<Sample>, values: List<String>): List<List<String>> =
        samples.mapIndexedNotNull { id, value ->
            if (value.type == Sample.Type.C) {
                // row, F_col_value, G_col_cpm, point_number
                listOf(values[id], value.ccpm1.toString(), "$id")
            }
            null
        }

    private fun file(hormone: String, date: String): File {
        val fileName = "${hormone}-${date}.$TYPE"
        val userHome = System.getProperty("user.home")
        val outputFile = File(userHome, fileName)
        outputFile.parentFile?.mkdirs()
        return File(userHome, fileName)
    }
}