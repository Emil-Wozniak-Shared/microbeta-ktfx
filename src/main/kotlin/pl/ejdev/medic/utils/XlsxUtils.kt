package pl.ejdev.medic.utils

import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File

// DSL entry point
fun excel(fileName: String, block: ExcelBuilder.() -> Unit) {
    val workbook = XSSFWorkbook()
    val builder = ExcelBuilder(workbook)
    builder.block()
    File(fileName).outputStream().use { workbook.write(it) }
    workbook.close()
}

// Builder for the workbook
class ExcelBuilder(private val workbook: XSSFWorkbook) {
    fun sheet(name: String, block: SheetBuilder.() -> Unit) {
        val sheet = workbook.createSheet(name)
        SheetBuilder(sheet).block()
    }
}

data class Input(val value: Any?, val formula: String?)

enum class Type {
    V, // value
    F // formula
}

// Builder for a sheet
class SheetBuilder(private val sheet: org.apache.poi.ss.usermodel.Sheet) {
    fun row(index: Int? = null, block: RowBuilder.() -> Unit) {
        val row: Row = if (index != null) sheet.createRow(index) else sheet.createRow(sheet.lastRowNum + 1)
        RowBuilder(row).block()
    }


    fun v(value: Any?) = Input(value, null)
    fun f(formula: String?) = Input(null, formula)

    operator fun get(index: Int? = null): RowBuilder {
        var row: Row
        if (index != null) {
            try {
                row = sheet.getRow(index)
            } catch (_: Exception) {
                row = sheet.createRow(index)
            }
        } else {
            val newRowNum = sheet.lastRowNum + 1
            try {
                row = sheet.createRow(newRowNum)
            } catch (_: Exception) {
                row = sheet.createRow(newRowNum)
            }
        }
        return RowBuilder(row)
    }

    fun row(): RowBuilder {
        val row: Row = sheet.createRow(sheet.lastRowNum + 1)
        return RowBuilder(row)
    }

}

// Builder for a row
class RowBuilder(private val row: Row) {
    fun cell(index: Int? = null, value: Any?, formula: String?) {
        val cell: Cell =
            if (index != null) row.createCell(index) else row.createCell(row.lastCellNum.toInt().coerceAtLeast(0))
        if (value != null) {
            when (value) {
                is String -> cell.setCellValue(value)
                is Double -> cell.setCellValue(value)
                is Int -> cell.setCellValue(value.toDouble())
                is Boolean -> cell.setCellValue(value)
                else -> cell.setCellValue(value.toString())
            }
        }
        if (formula != null) {
            cell.cellFormula = formula
        }
    }

    operator fun get(index: Int, input: Any, type: Type) = apply {
        when (type) {
            Type.V -> cell(index, input, null)
            Type.F -> cell(index, null, input as String)
        }
    }

    operator fun get(input: Any, type: Type) = apply {
        when (type) {
            Type.V -> cell(null, input, null)
            Type.F -> cell(null, null, input as String)
        }
    }

    operator fun get(input: Any) = apply {
        cell(null, input, null)
    }
}
