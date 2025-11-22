package pl.ejdev.medic.service

import javafx.stage.Stage
import org.apache.commons.lang3.StringUtils.EMPTY
import org.dhatim.fastexcel.Workbook
import pl.ejdev.medic.BuildInfo
import pl.ejdev.medic.data.DATA_POMIARU
import pl.ejdev.medic.data.HORMONE
import pl.ejdev.medic.data.XSLX_DATA
import pl.ejdev.medic.model.Sample
import pl.ejdev.medic.model.xlsx.CreateXlsxCommand
import java.io.File

private const val TYPE = "xlsx"
private const val SHEET_FIRST_NAME = "Wydruk"
private const val INT_FORMAT = "#,##0"
private const val FORMULA_SIGN = "="

class ReportFastXslxService() : XslxService {
    override fun generate(stage: Stage, command: CreateXlsxCommand) {
        val (hormone, date, _, samples, _) = command
        val file = file(hormone, date)
        val workbook = Workbook(file.outputStream(), BuildInfo.name, null)
        val sheet = workbook.newWorksheet(SHEET_FIRST_NAME)
        XSLX_DATA.forEachIndexed { rowIndex, rowData ->
            rowData.forEachIndexed { colIndex, cellValue ->
                when (cellValue) {
                    is String -> when {
                        cellValue.startsWith(FORMULA_SIGN) -> {
                            val expression = cellValue.replace(FORMULA_SIGN, EMPTY)
                            sheet.formula(rowIndex, colIndex, expression)
                            if (!cellValue.uppercase().startsWith("=LOG")) {
                                sheet.style(rowIndex, colIndex).format(INT_FORMAT).set()
                            }
                        }

                        cellValue.toDoubleOrNull() != null -> {
                            sheet.value(rowIndex, colIndex, cellValue.toDouble())
                            sheet.style(rowIndex, colIndex).format(INT_FORMAT).set()
                        }

                        else -> when (cellValue) {
                            HORMONE -> sheet.value(rowIndex, colIndex, hormone)
                            DATA_POMIARU -> sheet.value(rowIndex, colIndex, date)
                            else -> sheet.value(rowIndex, colIndex, cellValue)
                        }
                    }

                    is Sample.Type -> {
                        val index = rowData[0].toString().toInt()
                        val sample = samples[index - 1].ccpm1
                        sheet.value(rowIndex, colIndex, sample.toDouble())
                        sheet.style(rowIndex, colIndex).format(INT_FORMAT).set()
                    }
                }
            }
            sheet.flush()
        }
        workbook.finish()
    }

    private fun file(hormone: String, date: String): File {
        val fileName = "${hormone}-${date}.$TYPE"
        val userHome = System.getProperty("user.home")
        return File(userHome, fileName)
    }

}