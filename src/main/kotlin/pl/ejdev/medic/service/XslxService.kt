package pl.ejdev.medic.service

import javafx.stage.Stage
import ktfx.layouts.label
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import pl.ejdev.medic.components.dialog
import pl.ejdev.medic.utils.SheetBuilder
import pl.ejdev.medic.utils.Type
import pl.ejdev.medic.utils.Type.FORM
import pl.ejdev.medic.utils.Type.VAL
import pl.ejdev.medic.utils.excel
import java.io.File

private const val WYDRUK = "Wydruk"
private const val VALUES_SHEET = "Mel8V09"

@Suppress("LocalVariableName")
class XslxService {
    fun generate(
        owner: Stage,
        hormone: String = "Kortyzol",
        date: String = "2019-01-08 00:00:00",
        subject: String = "Owce",
        initialData: List<List<String>> = listOf(
            listOf("Total", "1976"),
            listOf("Total", "1982"),
            listOf("Bg", "32"),
            listOf("Bg", "36"),
            listOf("Bo", "458"),
            listOf("Bo", "459"),
            listOf("Bo", "447"),
            listOf("1.25", "412"),
            listOf("1.25", "390"),
            listOf("2.5", "378"),
            listOf("2.5", "352"),
            listOf("5.0", "322"),
            listOf("5.0", "316"),
            listOf("10", "225"),
            listOf("10", "249"),
            listOf("20", "165"),
            listOf("20", "174"),
            listOf("40", "102"),
            listOf("40", "116"),
            listOf("80", "74"),
            listOf("80", "65")
        ),
        cpmData: List<Int> = listOf(
            219, 224, 164, 191, 226, 230, 293, 279, 325, 347, 386, 388, 372, 379, 293, 330, 252, 237,
            225, 247, 211, 220, 313, 295, 338, 341, 335, 362, 368, 392, 402, 402, 237, 220, 264, 295,
            359, 352, 400, 403, 411, 437, 477, 469, 454, 474, 483, 450, 412, 440, 262, 201, 250, 242,
            227, 270, 298, 299, 341, 300, 392, 410, 442, 413, 453, 449, 440, 477, 465, 469, 417, 373,
            297, 286, 341, 342, 375, 437, 392, 399, 391, 421, 452, 421, 468, 494, 473, 470, 395, 404,
            358, 355, 352, 373, 431, 387, 441, 456, 417, 460, 186, 176, 157, 158, 161, 138, 141, 150,
            194, 177, 17, 20, 235, 243, 309, 290, 335, 341, 396, 371, 400, 407, 412, 423, 375, 403,
            372, 365, 412, 358, 389, 432, 442, 446, 472, 435, 473, 456, 441, 440, 438, 472, 419, 444,
            382, 415, 433, 414, 455, 426, 482, 488, 248, 245, 235, 246, 187, 169, 199, 182, 228, 242,
            266, 293, 341, 358, 330, 296, 245, 259, 236, 262, 287, 284, 252, 265, 237, 252, 291, 296,
            233, 243, 229, 249, 259, 234, 240, 244, 279, 294, 312, 310, 326, 354, 321, 358, 229, 225,
            215, 220, 262, 271, 221, 236, 220, 226, 240, 258, 308, 297, 307, 299, 259, 255, 222, 212,
            192, 196, 199, 166, 196, 182, 305, 307, 341, 340, 361, 370, 246, 246, 204, 211, 194, 167,
            166, 181, 196, 174, 191
        ),
        standardPoints: List<List<String>> = listOf(
            // row, F_col_value, G_col_cpm, point_number
            listOf("1.25", "412", "4"),
            listOf("1.25", "390", "4"),
            listOf("2.5", "378", "5"),
            listOf("2.5", "352", "5"),
            listOf("5.0", "322", "6"),
            listOf("5.0", "316", "6"),
            listOf("10", "225", "7"),
            listOf("10", "249", "7"),
            listOf("20", "174", "1"),
            listOf("40", "102", "2"),
            listOf("40", "116", "2"),
            listOf("80", "74", "3"),
            listOf("80", "65", "3"),
        )
    ) {
        val fileName = "file.xlsx"
        val userHome = System.getProperty("user.home")
        val outputFile = File(userHome, fileName)
        runCatching {
            excel(outputFile.path) {
                sheet(WYDRUK) { wydrukRows() }
                sheet(VALUES_SHEET) { valuesRows(hormone, date, subject, initialData, standardPoints, cpmData) }
            }
        }.onFailure { e ->
            dialog(owner, "Create XLSX failed!") {
                label("${e.message}") {}
            }
            throw e
        }
    }

    private fun SheetBuilder.valuesRows(
        hormone: String,
        date: String,
        subject: String,
        initialData: List<List<String>>,
        standardPoints: List<List<String>>,
        cpmData: List<Int>
    ) {
        this[0]["RIA"]["Data"]
        this[1][hormone][date][subject]
        this[2][4, "% Bo", VAL][5, "G5*100/\$J$18", FORM][6, "(H5-I16)*100/\$J$18", FORM][7, "(I5-I16)*100/\$J$18", FORM][8, "(J5-I16)*100/\$J$18", FORM][9, "(K5-I16)*100/\$J$18", FORM][10, "(L5-I16)*100/\$J\$18", FORM][11, "(M5-I16)*100/\$J\$18", FORM][12, "(N5-I16)*100/\$J\$18", FORM][13, "(O5-I16)*100/\$J\$18", FORM][15, "J18*100/I14", FORM]
        this[3][4, "% T", VAL][5, "F5*100/\$F\$5", FORM][6, "G5*100/\$F\$5", FORM][7, "(H5-\$I$16)*100/\$F$5", FORM][8, "(I5-\$I\$16)*100/\$F\$5", FORM][9, "(J5-\$I\$16)*100/\$F\$5", FORM][10, "(K5-\$I\$16)*100/\$F\$5", FORM][11, "(L5-\$I\$16)*100/\$F\$5", FORM][12, "(M5-\$I\$16)*100/\$F\$5", FORM][13, "(N5-\$I\$16)*100/\$F\$5", FORM][14, "(O5-\$I\$16)*100/\$F\$5", FORM][15, "(P5-\$I\$16)*100/\$F\$5", FORM]
        this[4][2, "Wpisz", VAL][4, "Mean [cpm]", VAL][5, "INT(AVERAGE(G14:G15))", FORM][6, "AVERAGE(G16:G17)", FORM][7, "AVERAGE(G18:G20)", FORM][8, "AVERAGE(G23:G24)", FORM][9, "AVERAGE(G25:G26)", FORM][10, "AVERAGE(G27:G28)", FORM][11, "AVERAGE(G29:G30)", FORM][12, "AVERAGE(G31:G32)", FORM][13, "AVERAGE(G33:G34)", FORM][14, "AVERAGE(G35:G36)", FORM][15, "AVERAGE(G37:G39)", FORM]
        this[5]["Nr probówki"]["Zawartość"]["cpm"]["N"][5, "COUNT(G14:G15)", FORM][6, "COUNT(G16:G17)", FORM][7, "COUNT(G18:G21)", FORM][8, "COUNT(G23:G24)", FORM][9, "COUNT(G25:G26)", FORM][10, "COUNT(G27:G28)", FORM][11, "COUNT(G29:G30)", FORM][12, "COUNT(G31:G32)", FORM][13, "COUNT(G33:G34)", FORM][14, "COUNT(G35:G36)", FORM][15, "COUNT(G37:G39)", FORM]

        initialData
            .filter { it[0] in listOf("Total", "Bg", "Bo") }
            .forEachIndexed { index, data ->
                this@valuesRows[6 + index][0, index + 1, VAL][1, data[0], VAL][2, data[1].toInt(), VAL]
            }
        initialData
            .filter { it[0] !in listOf("Total", "Bg", "Bo") }
            .forEachIndexed { index, data ->
                this@valuesRows[15 + index][0, index + 1, VAL][1, data[0], VAL][2, data[1].toInt(), VAL]
            }

        standarCurveHeadersAndInitialCalc()
        controlCurve()
        renderStandardCurve()
        renderStandardPoints(standardPoints)
        sumFormula()
        dataHeaders()
        renderPoints(cpmData)
    }

    private fun SheetBuilder.wydrukRows() {
        this[0]["Mel8V09!A1", FORM]["Mel8V09!B1", FORM]
        // 3 empty
        // 4 empty
        this[1]["Mel8V09!A2", FORM]["Mel8V09!B2", FORM]
        this[4]["Mel8V09!M17", FORM]["Mel8V09!O17", FORM]
        this[5]["Mel8V09!M18", FORM]["Mel8V09!N18", FORM]["±SD"]
        this[6]["Mel8V09!M19", FORM]["Mel8V09!N19", FORM]["Mel8V09!O19", FORM]
        this[7]["Mel8V09!M20", FORM]["Mel8V09!N20", FORM]["Mel8V09!O20", FORM]
        this[8]["Mel8V09!M21", FORM]["Mel8V09!N21", FORM]
        // 9 empty
        this[10]["Mel8V09!L44", FORM]["Mel8V09!N44", FORM]["Mel8V09!Q44", FORM]["Mel8V09!R44", FORM]
        this[11]["Mel8V09!L45", FORM]["Mel8V09!N45", FORM]["Mel8V09!O45", FORM]["Mel8V09!P45", FORM]["Mel8V09!Q45", FORM]["Mel8V09!R45", FORM]
        this[12]["Mel8V09!L46", FORM]["Mel8V09!N46", FORM]["Mel8V09!O46", FORM]["Mel8V09!P46", FORM]["Mel8V09!Q46", FORM]["Mel8V09!R46", FORM]
        this[13]["Mel8V09!L47", FORM]["Mel8V09!N47", FORM]["Mel8V09!O47", FORM]["Mel8V09!P47", FORM]["Mel8V09!Q47", FORM]["Mel8V09!R47", FORM]
//        // 14 empty
//        this[15][2, "Mel8V09!C42", FORM][4, "Wynik", V]
//        this[16]["Mel8V09!A43", FORM]["Uwagi", V]["Mel8V09!C43", FORM]["Opis próby"]["Mel8V09!F43", FORM]["Mel8V09!G43", FORM]
        (17..405).forEach { rowNum ->
            this[rowNum].also { row ->
                row[0, if (rowNum <= 404) "Mel8V09!A${rowNum + 26}" else "", VAL][2, if (rowNum <= 404) "Mel8V09!C${rowNum + 26}" else "", VAL][6, "MOD(ROW(),2)", FORM]
                when (rowNum) {
                    in (17..403 step 2) -> {
                        // Column E - formulas and values
                        row[4, "Mel8V09!F${rowNum + 26}", FORM]
                        // Column F - formulas
                        row[5, "Mel8V09!G${rowNum + 26}", FORM]
                    }
                    // Column H - references to column E
                    in (18..404 step 2) -> row[7, "E${rowNum + 1}", FORM]
                }

                // Column I - sequential numbers and formulas
                when (rowNum) {
                    17 -> row[8, 1.0, VAL]
                    18 -> row[8, 2.0, VAL]
                    19 -> row[8, "I18+1", FORM]
                    20 -> row[8, 4.0, VAL]
                    21 -> row[8, "I20+1", FORM]
                    // Continue this pattern for all rows...
                    else -> when (rowNum) {
                        in (23..403 step 2) -> row[8, "I${rowNum - 1}+1", FORM]
                        in (22..404 step 2) -> row[8, (rowNum - 16).toDouble(), VAL]
                    }
                }
            }
        }
    }

    infix fun Sheet.row(index: Int): Row = this.createRow(index)
    infix fun Row.cell(index: Int): Cell = this.createCell(index)
    infix fun Cell.formula(text: String) {
        this.cellFormula = text
    }

    infix fun Cell.value(input: Any) {
        when (input) {
            is Double -> this.setCellValue(input)
            else -> this.setCellValue(input.toString())
        }
    }


    private fun Sheet.buildWydruk() {
        // Row 1
        var row = this row 0
        row cell 0 formula "Mel8V09!A1"
        row cell 1 formula "Mel8V09!B1"

        // Row 2
        row = this row 1
        row cell 0 formula "Mel8V09!A2"
        row cell 1 formula "#REF!"

        // Rows 3-4 empty

        // Row 5
        row = this row 4
        row cell 0 formula "Mel8V09!M17"
        row cell 1 formula "Mel8V09!O17"

        // Row 6
        row = this row 5
        row cell 0 formula "Mel8V09!M18"
        row cell 1 formula "Mel8V09!N18"
        row cell 2 value "±SD"

        // Row 7
        row = this row 6
        row cell 0 formula "Mel8V09!M19"
        row cell 1 formula "Mel8V09!N19"
        row cell 2 formula "Mel8V09!O19"

        // Row 8
        row = this row 7
        row cell 0 formula "Mel8V09!M20"
        row cell 1 formula "Mel8V09!N20"
        row cell 2 formula "Mel8V09!O20"

        // Row 9
        row = this row 8
        row cell 0 formula "Mel8V09!M21"
        row cell 1 formula "Mel8V09!N21"

        // Row 10 empty

        // Rows 11-13
        row = this row 10
        row cell 0 formula "Mel8V09!L44"
        row cell 1 formula "Mel8V09!N44"
        row cell 5 formula "Mel8V09!Q44"
        row cell 6 formula "Mel8V09!R44"

        row = this row 11
        row cell 0 formula "Mel8V09!L45"
        row cell 1 formula "Mel8V09!N45"
        row cell 2 formula "Mel8V09!O45"
        row cell 4 formula "Mel8V09!P45"
        row cell 5 formula "Mel8V09!Q45"
        row cell 6 formula "Mel8V09!R45"

        row = this row 12
        row cell 0 formula "Mel8V09!L46"
        row cell 1 formula "Mel8V09!N46"
        row cell 2 formula "Mel8V09!O46"
        row cell 4 formula "Mel8V09!P46"
        row cell 5 formula "Mel8V09!Q46"
        row cell 6 formula "Mel8V09!R46"

        row = this row 13
        row cell 0 formula "Mel8V09!L47"
        row cell 1 formula "Mel8V09!N47"
        row cell 2 formula "Mel8V09!O47"
        row cell 4 formula "Mel8V09!P47"
        row cell 6 formula "Mel8V09!R47"

        // Row 14 empty

        // Rows 15-16
        row = this row 15
        row cell 2 formula "Mel8V09!C42"
        row cell 4 value "Wynik"

        row = this row 16
        row cell 0 formula "Mel8V09!A43"
        row cell 1 value "Uwagi"
        row cell 2 formula "Mel8V09!C43"
        row cell 3 value "Opis próby"
        row cell 4 formula "Mel8V09!F43"
        row cell 5 formula "Mel8V09!G43"

        // Create the main data table with formulas
        (17..405).forEach { rowNum ->
            row = this row rowNum

            // Column A
            if (rowNum <= 404) {
                row cell 0 formula "Mel8V09!A${rowNum + 26}"
            }

            // Column B - mostly empty except for some labels

            // Column C
            if (rowNum <= 404) {
                row cell 2 formula "Mel8V09!C${rowNum + 26}"
            }

            // Column D - mostly empty

            // Column E - formulas and values
            when (rowNum) {
                17, 19, 21, 23, 25, 27, 29, 31, 33, 35, 37, 39, 41, 43, 45, 47, 49, 51, 53, 55, 57, 59,
                61, 63, 65, 67, 69, 71, 73, 75, 77, 79, 81, 83, 85, 87, 89, 91, 93, 95, 97, 99, 101, 103,
                105, 107, 109, 111, 113, 115, 117, 119, 121, 123, 125, 127, 129, 131, 133, 135, 137, 139,
                141, 143, 145, 147, 149, 151, 153, 155, 157, 159, 161, 163, 165, 167, 169, 171, 173, 175,
                177, 179, 181, 183, 185, 187, 189, 191, 193, 195, 197, 199, 201, 203, 205, 207, 209, 211,
                213, 215, 217, 219, 221, 223, 225, 227, 229, 231, 233, 235, 237, 239, 241, 243, 245, 247,
                249, 251, 253, 255, 257, 259, 261, 263, 265, 267, 269, 271, 273, 275, 277, 279, 281, 283,
                285, 287, 289, 291, 293, 295, 297, 299, 301, 303, 305, 307, 309, 311, 313, 315, 317, 319,
                321, 323, 325, 327, 329, 331, 333, 335, 337, 339, 341, 343, 345, 347, 349, 351, 353, 355,
                357, 359, 361, 363, 365, 367, 369, 371, 373, 375, 377, 379, 381, 383, 385, 387, 389, 391,
                393, 395, 397, 399, 401, 403, 405 -> {
                    if (rowNum <= 404) {
                        row cell 4 formula "Mel8V09!F${rowNum + 26}"
                    }
                }
            }

            // Column F - formulas
            when (rowNum) {
                17, 19, 21, 23, 25, 27, 29, 31, 33, 35, 37, 39, 41, 43, 45, 47, 49, 51, 53, 55, 57, 59,
                61, 63, 65, 67, 69, 71, 73, 75, 77, 79, 81, 83, 85, 87, 89, 91, 93, 95, 97, 99, 101, 103,
                105, 107, 109, 111, 113, 115, 117, 119, 121, 123, 125, 127, 129, 131, 133, 135, 137, 139,
                141, 143, 145, 147, 149, 151, 153, 155, 157, 159, 161, 163, 165, 167, 169, 171, 173, 175,
                177, 179, 181, 183, 185, 187, 189, 191, 193, 195, 197, 199, 201, 203, 205, 207, 209, 211,
                213, 215, 217, 219, 221, 223, 225, 227, 229, 231, 233, 235, 237, 239, 241, 243, 245, 247,
                249, 251, 253, 255, 257, 259, 261, 263, 265, 267, 269, 271, 273, 275, 277, 279, 281, 283,
                285, 287, 289, 291, 293, 295, 297, 299, 301, 303, 305, 307, 309, 311, 313, 315, 317, 319,
                321, 323, 325, 327, 329, 331, 333, 335, 337, 339, 341, 343, 345, 347, 349, 351, 353, 355,
                357, 359, 361, 363, 365, 367, 369, 371, 373, 375, 377, 379, 381, 383, 385, 387, 389, 391,
                393, 395, 397, 399, 401, 403, 405 -> {
                    if (rowNum <= 404) {
                        row cell 5 formula "Mel8V09!G${rowNum + 26}"
                    }
                }
            }

            // Column G - MOD formulas
            row cell 6 formula "MOD(ROW(),2)"

            // Column H - references to column E
            when (rowNum) {
                18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 42, 44, 46, 48, 50, 52, 54, 56, 58, 60,
                62, 64, 66, 68, 70, 72, 74, 76, 78, 80, 82, 84, 86, 88, 90, 92, 94, 96, 98, 100, 102, 104,
                106, 108, 110, 112, 114, 116, 118, 120, 122, 124, 126, 128, 130, 132, 134, 136, 138, 140,
                142, 144, 146, 148, 150, 152, 154, 156, 158, 160, 162, 164, 166, 168, 170, 172, 174, 176,
                178, 180, 182, 184, 186, 188, 190, 192, 194, 196, 198, 200, 202, 204, 206, 208, 210, 212,
                214, 216, 218, 220, 222, 224, 226, 228, 230, 232, 234, 236, 238, 240, 242, 244, 246, 248,
                250, 252, 254, 256, 258, 260, 262, 264, 266, 268, 270, 272, 274, 276, 278, 280, 282, 284,
                286, 288, 290, 292, 294, 296, 298, 300, 302, 304, 306, 308, 310, 312, 314, 316, 318, 320,
                322, 324, 326, 328, 330, 332, 334, 336, 338, 340, 342, 344, 346, 348, 350, 352, 354, 356,
                358, 360, 362, 364, 366, 368, 370, 372, 374, 376, 378, 380, 382, 384, 386, 388, 390, 392,
                394, 396, 398, 400, 402, 404 -> {
                    row cell 7 formula "E${rowNum + 1}"
                }
            }

            // Column I - sequential numbers and formulas
            when (rowNum) {
                17 -> row cell 8 value 1.0
                18 -> row cell 8 value 2.0
                19 -> row cell 8 formula "I18+1"
                20 -> row cell 8 value 4.0
                21 -> row cell 8 formula "I20+1"
                // Continue this pattern for all rows...
                else -> {
                    when {
                        rowNum in 22..404 && rowNum % 2 == 1 -> row cell 8 formula "I${rowNum - 1}+1"
                        rowNum in 22..404 -> row cell 8 value (rowNum - 16).toDouble()
                    }
                }
            }

            // Column J - labels
            when (rowNum) {
                17 -> row cell 9 value "53F"
                92 -> row cell 9 value "54K"
                142 -> row cell 9 value "56K"
                192 -> row cell 9 value "52F"
                242 -> row cell 9 value "58K"
                292 -> row cell 9 value "54F"
                344 -> row cell 9 value "52K"
            }

            // Column K - values (mostly 1s in specific rows)
            when (rowNum) {
                292, 294, 296, 298, 300, 302, 304, 306, 308, 310, 312, 314, 316, 318, 320, 322, 324, 326,
                328, 330, 332, 334, 336, 338, 340 -> row cell 10 value 1.0
            }
        }
    }

    private fun buildMel8V09Sheet(sheet: Sheet) {
        // Header section (rows 0-5)
        var row = sheet.createRow(0)
        row.createCell(0).setCellValue("RIA")
        row.createCell(1).setCellValue("Data")

        row = sheet.createRow(1)
        row.createCell(0).setCellValue("Kortyzol")
        row.createCell(1).setCellValue("2019-01-08 00:00:00")
        row.createCell(2).setCellValue("Owce")

        row = sheet.createRow(2)
        row.createCell(4).setCellValue("% Bo")
        row.createCell(5).cellFormula = "G5*100/\$J\$18"
        row.createCell(6).cellFormula = "(H5-I16)*100/\$J\$18"
        row.createCell(7).cellFormula = "(I5-I16)*100/\$J\$18"
        row.createCell(8).cellFormula = "(J5-I16)*100/\$J\$18"
        row.createCell(9).cellFormula = "(K5-I16)*100/\$J\$18"
        row.createCell(10).cellFormula = "(L5-I16)*100/\$J\$18"
        row.createCell(11).cellFormula = "(M5-I16)*100/\$J\$18"
        row.createCell(12).cellFormula = "(N5-I16)*100/\$J\$18"
        row.createCell(13).cellFormula = "(O5-I16)*100/\$J\$18"
        row.createCell(15).cellFormula = "J18*100/I14"

        row = sheet.createRow(3)
        row.createCell(4).setCellValue("% T")
        row.createCell(5).cellFormula = "F5*100/\$F\$5"
        row.createCell(6).cellFormula = "G5*100/\$F\$5"
        row.createCell(7).cellFormula = "(H5-\$I\$16)*100/\$F\$5"
        row.createCell(8).cellFormula = "(I5-\$I\$16)*100/\$F\$5"
        row.createCell(9).cellFormula = "(J5-\$I\$16)*100/\$F\$5"
        row.createCell(10).cellFormula = "(K5-\$I\$16)*100/\$F\$5"
        row.createCell(11).cellFormula = "(L5-\$I\$16)*100/\$F\$5"
        row.createCell(12).cellFormula = "(M5-\$I\$16)*100/\$F\$5"
        row.createCell(13).cellFormula = "(N5-\$I\$16)*100/\$F\$5"
        row.createCell(14).cellFormula = "(O5-\$I\$16)*100/\$F\$5"
        row.createCell(15).cellFormula = "(P5-\$I\$16)*100/\$F\$5"

        row = sheet.createRow(4)
        row.createCell(2).setCellValue("Wpisz")
        row.createCell(4).setCellValue("Mean [cpm]")
        row.createCell(5).cellFormula = "AVERAGE(G14:G15)"
        row.createCell(6).cellFormula = "AVERAGE(G16:G17)"
        row.createCell(7).cellFormula = "AVERAGE(G18:G20)"
        row.createCell(8).cellFormula = "AVERAGE(G23:G24)"
        row.createCell(9).cellFormula = "AVERAGE(G25:G26)"
        row.createCell(10).cellFormula = "AVERAGE(G27:G28)"
        row.createCell(11).cellFormula = "AVERAGE(G29:G30)"
        row.createCell(12).cellFormula = "AVERAGE(G31:G32)"
        row.createCell(13).cellFormula = "AVERAGE(G33:G34)"
        row.createCell(14).cellFormula = "AVERAGE(G35:G36)"
        row.createCell(15).cellFormula = "AVERAGE(G37:G39)"

        row = sheet.createRow(5)
        row.createCell(0).setCellValue("Nr probówki")
        row.createCell(1).setCellValue("Zawartość")
        row.createCell(2).setCellValue("cpm")
        row.createCell(4).setCellValue("N")
        row.createCell(5).cellFormula = "COUNT(G14:G15)"
        row.createCell(6).cellFormula = "COUNT(G16:G17)"
        row.createCell(7).cellFormula = "COUNT(G18:G21)"
        row.createCell(8).cellFormula = "COUNT(G23:G24)"
        row.createCell(9).cellFormula = "COUNT(G25:G26)"
        row.createCell(10).cellFormula = "COUNT(G27:G28)"
        row.createCell(11).cellFormula = "COUNT(G29:G30)"
        row.createCell(12).cellFormula = "COUNT(G31:G32)"
        row.createCell(13).cellFormula = "COUNT(G33:G34)"
        row.createCell(14).cellFormula = "COUNT(G35:G36)"
        row.createCell(15).cellFormula = "COUNT(G37:G39)"

        // Sample data rows 6-21
        val initialData = listOf(
            listOf("1", "Total", "1976"),
            listOf("2", "Total", "1982"),
            listOf("3", "Bg", "32"),
            listOf("4", "Bg", "36"),
            listOf("5", "Bo", "458"),
            listOf("6", "Bo", "459"),
            listOf("7", "Bo", "447"),
            listOf("8", "1.25", "412"),
            listOf("9", "1.25", "390"),
            listOf("10", "2.5", "378"),
            listOf("11", "2.5", "352"),
            listOf("12", "5", "322"),
            listOf("13", "5", "316"),
            listOf("14", "10", "225"),
            listOf("15", "10", "249"),
            listOf("16", "20", "165"),
            listOf("17", "20", "174"),
            listOf("18", "40", "102"),
            listOf("19", "40", "116"),
            listOf("20", "80", "74"),
            listOf("21", "80", "65")
        )

        initialData.forEachIndexed { index, data ->
            row = sheet.createRow(6 + index)
            row.createCell(0).setCellValue(data[0])
            row.createCell(1).setCellValue(data[1])
            row.createCell(2).setCellValue(data[2].toDouble())
        }

        // Standard curve and calculations (rows 22-42)
        // This section contains complex formulas for the RIA standard curve

        // MAIN DATA SECTION - Rows 43 onwards with CPM values in column B
        row = sheet.createRow(43)
        row.createCell(0).setCellValue("Nr probówki")
        row.createCell(1).setCellValue("cpm")
        row.createCell(2).setCellValue("ng/ml")
        row.createCell(3).setCellValue("rozc.")
        row.createCell(4).setCellValue("Wynik")
        row.createCell(5).setCellValue("Srednia")
        row.createCell(6).setCellValue("c.v.")

        // CPM data for rows 44-404 in column B
        val cpmData = listOf(
            219, 224, 164, 191, 226, 230, 293, 279, 325, 347, 386, 388, 372, 379, 293, 330, 252, 237,
            225, 247, 211, 220, 313, 295, 338, 341, 335, 362, 368, 392, 402, 402, 237, 220, 264, 295,
            359, 352, 400, 403, 411, 437, 477, 469, 454, 474, 483, 450, 412, 440, 262, 201, 250, 242,
            227, 270, 298, 299, 341, 300, 392, 410, 442, 413, 453, 449, 440, 477, 465, 469, 417, 373,
            297, 286, 341, 342, 375, 437, 392, 399, 391, 421, 452, 421, 468, 494, 473, 470, 395, 404,
            358, 355, 352, 373, 431, 387, 441, 456, 417, 460, 186, 176, 157, 158, 161, 138, 141, 150,
            194, 177, 17, 20, 235, 243, 309, 290, 335, 341, 396, 371, 400, 407, 412, 423, 375, 403,
            372, 365, 412, 358, 389, 432, 442, 446, 472, 435, 473, 456, 441, 440, 438, 472, 419, 444,
            382, 415, 433, 414, 455, 426, 482, 488, 248, 245, 235, 246, 187, 169, 199, 182, 228, 242,
            266, 293, 341, 358, 330, 296, 245, 259, 236, 262, 287, 284, 252, 265, 237, 252, 291, 296,
            233, 243, 229, 249, 259, 234, 240, 244, 279, 294, 312, 310, 326, 354, 321, 358, 229, 225,
            215, 220, 262, 271, 221, 236, 220, 226, 240, 258, 308, 297, 307, 299, 259, 255, 222, 212,
            192, 196, 199, 166, 196, 182, 305, 307, 341, 340, 361, 370, 246, 246, 204, 211, 194, 167,
            166, 181, 196, 174, 191
        )

        // Create rows 44-404 with CPM data and formulas
        (44..404).forEach { i ->
            row = sheet.createRow(i)
            val rowIndex = i + 1 // For 1-based indexing in formulas

            // Column A - Tube numbers (continuing from previous)
            if (i == 44) {
                row.createCell(0).setCellValue("25")
            } else {
                row.createCell(0).cellFormula = "A${i}+1"
            }

            // Column B - CPM values (the critical data that was missing)
            val cpmIndex = i - 44
            if (cpmIndex < cpmData.size) {
                row.createCell(1).setCellValue(cpmData[cpmIndex].toDouble())
            }

            // Column C - Cortisol concentration formula
            val concentrationFormula =
                "10^((LOG((B$rowIndex-\$I\$16)*100/\$J\$18/(100-(B$rowIndex-\$I\$16)*100/\$J\$18))-\$R\$19)/\$R\$20)"
            row.createCell(2).cellFormula = concentrationFormula

            // Column D - Dilution factor
            if (i == 44) {
                row.createCell(3).setCellValue(1.0)
            } else {
                row.createCell(3).cellFormula = "D${i}"
            }

            // Column E - Result (concentration * dilution)
            row.createCell(4).cellFormula = "C$rowIndex*D$rowIndex"

            when (i) {
                in (44..404 step 2) -> {
                    // Column F - Average (for duplicate samples)
                    row.createCell(5).cellFormula = "AVERAGE(E${i}:E${i + 1})"
                    // Column G - Coefficient of variation
                    row.createCell(6).cellFormula = "100*STDEV(E${i}:E${i + 1})/F$rowIndex"
                }
            }

            // Column H - Sample identifiers
            when (i) {
                44 -> row.createCell(7).setCellValue("1")
                46 -> row.createCell(7).setCellValue("2")
                48 -> row.createCell(7).setCellValue("3")
                50 -> row.createCell(7).setCellValue("4")
                52 -> row.createCell(7).setCellValue("5")
                54 -> row.createCell(7).setCellValue("6")
                56 -> row.createCell(7).setCellValue("7")
                58 -> row.createCell(7).setCellValue("8")
                60 -> row.createCell(7).setCellValue("9")
                62 -> row.createCell(7).setCellValue("10")
                // ... continue pattern
                92 -> row.createCell(8).setCellValue("54K")
                142 -> row.createCell(8).setCellValue("56K")
                192 -> row.createCell(8).setCellValue("52F")
                242 -> row.createCell(8).setCellValue("58K")
                292 -> row.createCell(8).setCellValue("54F")
                344 -> row.createCell(8).setCellValue("52K")
            }
        }

        // ... (previous header and initial data setup remains the same)

        // CRITICAL SECTION: Standard Curve Data and Calculations (Rows 13-23, Columns E-J)

        // Row 13 - Standard curve headers and initial calculations
        val row13 = sheet.createRow(12)
        row13.createCell(4).setCellValue("c.v. [%]")
        row13.createCell(5).cellFormula = "STDEV(G14:G15)*100/F5"
        row13.createCell(6).cellFormula = "STDEV(G16:G17)*100/G5"
        row13.createCell(7).cellFormula = "STDEV(G18:G21)*100/H5"
        row13.createCell(8).cellFormula = "STDEV(G23:G24)*100/I5"
        row13.createCell(9).cellFormula = "STDEV(G25:G26)*100/J5"
        row13.createCell(10).cellFormula = "STDEV(G27:G28)*100/K5"
        row13.createCell(11).cellFormula = "STDEV(G29:G30)*100/L5"
        row13.createCell(12).cellFormula = "STDEV(G31:G32)*100/M5"
        row13.createCell(13).cellFormula = "STDEV(G33:G34)*100/N5"
        row13.createCell(14).cellFormula = "STDEV(G35:G36)*100/O5"
        row13.createCell(15).cellFormula = "STDEV(G37:G39)*100/P5"

        // Row 14 - Diff%Mean calculations
        val row14 = sheet.createRow(13)
        row14.createCell(4).setCellValue("Diff%Mean")
        row14.createCell(5).cellFormula = "G14*100/\$F\$5-100"
        row14.createCell(6).cellFormula = "G16*100/\$G\$5-100"
        row14.createCell(7).cellFormula = "G18*100/\$H\$5-100"
        row14.createCell(8).cellFormula = "G23*100/\$I\$5-100"
        row14.createCell(9).cellFormula = "G25*100/\$J\$5-100"
        row14.createCell(10).cellFormula = "G27*100/\$K\$5-100"
        row14.createCell(11).cellFormula = "G29*100/\$L\$5-100"
        row14.createCell(12).cellFormula = "G31*100/\$M\$5-100"
        row14.createCell(13).cellFormula = "G33*100/\$N\$5-100"
        row14.createCell(14).cellFormula = "G35*100/\$N\$5-100"
        row14.createCell(15).cellFormula = "G37*100/\$N\$5-100"

        // Row 15 - Second set of Diff%Mean calculations
        val row15 = sheet.createRow(14)
        row15.createCell(5).cellFormula = "G15*100/\$F\$5-100"
        row15.createCell(6).cellFormula = "G17*100/\$G\$5-100"
        row15.createCell(7).cellFormula = "G19*100/\$H\$5-100"
        row15.createCell(8).cellFormula = "G24*100/\$I\$5-100"
        row15.createCell(9).cellFormula = "G26*100/\$J\$5-100"
        row15.createCell(10).cellFormula = "G28*100/\$K\$5-100"
        row15.createCell(11).cellFormula = "G30*100/\$L\$5-100"
        row15.createCell(12).cellFormula = "G32*100/\$M\$5-100"
        row15.createCell(13).cellFormula = "G34*100/\$N\$5-100"
        row15.createCell(14).cellFormula = "G36*100/\$N\$5-100"
        row15.createCell(15).cellFormula = "G38*100/\$N\$5-100"

        // Row 16 - Empty row for spacing

        // Row 17 - Tube No., Content, [cpm], Mean; c.v., Akceptuj headers
        val row17 = sheet.createRow(16)
        row17.createCell(4).setCellValue("Tube No.")
        row17.createCell(5).setCellValue("Content")
        row17.createCell(6).setCellValue("[cpm]")
        row17.createCell(7).setCellValue("Mean; c.v.")
        row17.createCell(8).setCellValue("Akceptuj")

        // Row 18 - First tube data and calculations
        val row18 = sheet.createRow(17)
        row18.createCell(4).cellFormula = "A7"
        row18.createCell(5).cellFormula = "B7"
        row18.createCell(6).cellFormula = "C7"
        row18.createCell(7).cellFormula = "AVERAGE(G14:G15)"
        row18.createCell(8).cellFormula = "H14"

        // Row 19 - Second tube data and calculations
        val row19 = sheet.createRow(18)
        row19.createCell(4).cellFormula = "A8"
        row19.createCell(5).cellFormula = "B8"
        row19.createCell(6).cellFormula = "C8"
        row19.createCell(7).cellFormula = "STDEV(G14:G15)*100/H14"

        // Row 20 - Third tube data and calculations
        val row20 = sheet.createRow(19)
        row20.createCell(4).cellFormula = "A9"
        row20.createCell(5).setCellValue("O")
        row20.createCell(6).cellFormula = "C9"
        row20.createCell(7).cellFormula = "AVERAGE(G16:G17)"
        row20.createCell(8).cellFormula = "H16"
        row20.createCell(9).setCellValue("Bo-Bg")

        // Row 21 - Fourth tube data and calculations
        val row21 = sheet.createRow(20)
        row21.createCell(4).cellFormula = "A10"
        row21.createCell(5).setCellValue("O")
        row21.createCell(6).cellFormula = "C10"
        row21.createCell(7).cellFormula = "STDEV(G16:G17)*100/H16"
        row21.createCell(9).cellFormula = "I18-I16"

        // Row 22 - Fifth tube data and calculations
        val row22 = sheet.createRow(21)
        row22.createCell(4).cellFormula = "A11"
        row22.createCell(5).setCellValue("N")
        row22.createCell(6).cellFormula = "C11"
        row22.createCell(7).cellFormula = "AVERAGE(G18:G20)"
        row22.createCell(8).cellFormula = "H18"
        row22.createCell(9).cellFormula = "I18-I16"

        // Row 23 - Sixth tube data and calculations
        val row23 = sheet.createRow(22)
        row23.createCell(4).cellFormula = "A12"
        row23.createCell(5).setCellValue("N")
        row23.createCell(6).cellFormula = "C12"
        row23.createCell(7).cellFormula = "STDEV(G18:G20)*100/H18"

        // STANDARD CURVE REGRESSION SECTION (Rows 23-40)

        // Row 23 - Wzorzec headers and binding calculations
        val row23b = sheet.getRow(22) ?: sheet.createRow(22)
        row23b.createCell(10).setCellValue("Parametry regresji")
        row23b.createCell(13).setCellValue("Y=a+bX")

        // Row 24 - N, SD headers
        val row24 = sheet.createRow(23)
        row24.createCell(10).setCellValue("N")
        row24.createCell(12).setCellValue("SD")

        // Row 25 - N count and b calculation
        val row25 = sheet.createRow(24)
        row25.createCell(10).cellFormula = "COUNT(M25:M40)"
        row25.createCell(11).cellFormula =
            "(COUNT(M25:M38)*SUMPRODUCT(M25:M38,N25:N38)-SUM(M25:M38)*SUM(N25:N38))/(COUNT(M25:M38)*SUMSQ(M25:M38)-(SUM(M25:M38))^2)"
        row25.createCell(12).cellFormula =
            "(((COUNT(M25:M40)*SUMSQ(N25:N40)-SUM(N25:N40)^2)/(COUNT(M25:M40)*SUMSQ(M25:M40)-SUM(M25:M40)^2))*(1-N21^2)/(COUNT(M25:M40)-2))^0.5"

        // Row 26 - a calculation
        val row26 = sheet.createRow(25)
        row26.createCell(9).setCellValue("a=")
        row26.createCell(10).cellFormula = "SUM(N25:N38)/COUNT(M25:M38)-N19*SUM(M25:M38)/COUNT(M25:M38)"
        row26.createCell(11).cellFormula = "(O19^2*SUMSQ(M25:M40)/COUNT(M25:M40))^0.5"
        row26.createCell(13).setCellValue("b=")
        row26.createCell(14).cellFormula = "N19"

        // Row 27 - r calculation
        val row27 = sheet.createRow(26)
        row27.createCell(9).setCellValue("r=")
        row27.createCell(10).cellFormula = "CORREL(M25:M40,N25:N40)"
        row27.createCell(14).cellFormula = "N21"

        // Row 28 - Logit transformation headers
        val row28 = sheet.createRow(27)
        row28.createCell(6).setCellValue("bindingPercent")
        row28.createCell(7).setCellValue("logDose")
        row28.createCell(8).setCellValue("logarithmRealZero")
        row28.createCell(11).setCellValue("logDose")
        row28.createCell(12).setCellValue("logarithmRealZero")
        row28.createCell(14).setCellValue("Logit(B-Bg)")

        // Row 29 - Standard curve point 1 calculations
        val row29 = sheet.createRow(28)
        row29.createCell(5).setCellValue("STD")
        row29.createCell(6).cellFormula = "(G23-\$I\$16)*100/\$J\$18"  // %Bo-Bg
        row29.createCell(7).cellFormula = "LOG(F23)"                   // Log(dose)
        row29.createCell(8).cellFormula = "LOG(H23/(100-H23))"        // Logit(B-Bg)
        row29.createCell(10).cellFormula = "I23"                      // X
        row29.createCell(11).cellFormula = "J23"                      // Y
        row29.createCell(12).cellFormula =
            "10^((LOG((G23-\$I\$16)*100/\$J\$18/(100-(G23-\$I\$16)*100/\$J\$18))-\$R\$19)/\$R\$20)" // Odczyt [pg]
        row29.createCell(13).setCellValue("1")                          // Punkt nr
        row29.createCell(14).cellFormula = "100*STDEV(O25:O26)/AVERAGE(O25:O26)" // c.v. %
        row29.createCell(15).cellFormula = "(O25-F23)*100/F23"        // Delta%
        row29.createCell(16).cellFormula = "O25/F23"                  // ng*N

        // Continue with standard curve points 2-16 (rows 30-44)
        setupStandardCurvePoints(sheet)

        // Add regression parameters and other calculations
        setupRegressionParameters(sheet)
    }

    private fun setupRegressionParameters(sheet: Sheet) {
        // Setup regression parameters in rows 18-22
        val row18 = sheet.getRow(17) ?: sheet.createRow(17)
        row18.createCell(17).cellFormula = "COUNT(M25:M40)"
        row18.createCell(18).setCellValue("SD")

        val row19 = sheet.getRow(18) ?: sheet.createRow(18)
        row19.createCell(17).cellFormula =
            "(COUNT(M25:M38)*SUMPRODUCT(M25:M38,N25:N38)-SUM(M25:M38)*SUM(N25:N38))/(COUNT(M25:M38)*SUMSQ(M25:M38)-(SUM(M25:M38))^2)"
        row19.createCell(18).cellFormula =
            "(((COUNT(M25:M40)*SUMSQ(N25:N40)-SUM(N25:N40)^2)/(COUNT(M25:M40)*SUMSQ(M25:M40)-SUM(M25:M40)^2))*(1-N21^2)/(COUNT(M25:M40)-2))^0.5"

        val row20 = sheet.getRow(19) ?: sheet.createRow(19)
        row20.createCell(16).setCellValue("a=")
        row20.createCell(17).cellFormula = "SUM(N25:N38)/COUNT(M25:M38)-N19*SUM(M25:M38)/COUNT(M25:M38)"
        row20.createCell(18).cellFormula = "(O19^2*SUMSQ(M25:M40)/COUNT(M25:M40))^0.5"

        val row21 = sheet.getRow(20) ?: sheet.createRow(20)
        row21.createCell(16).setCellValue("b=")
        row21.createCell(17).cellFormula = "CORREL(M25:M40,N25:N40)"
        row21.createCell(18).setCellValue("") // Empty cell

        val row22 = sheet.getRow(21) ?: sheet.createRow(21)
        row22.createCell(16).setCellValue("r=")
        row22.createCell(17).cellFormula = "N21"
    }


    private fun setupStandardCurvePoints(sheet: Sheet) {
        // Standard curve points 2-16
        val standardPoints = listOf(
            // row, F_col_value, G_col_cpm, point_number
            listOf("29", "20", "174", "1"),
            listOf("30", "40", "102", "2"),
            listOf("31", "40", "116", "2"),
            listOf("32", "80", "74", "3"),
            listOf("33", "80", "65", "3"),
            listOf("34", "1.25", "412", "4"),
            listOf("35", "1.25", "390", "4"),
            listOf("36", "2.5", "378", "5"),
            listOf("37", "2.5", "352", "5"),
            listOf("38", "5", "322", "6"),
            listOf("39", "5", "316", "6"),
            listOf("40", "10", "225", "7"),
            listOf("41", "10", "249", "7")
        )

        standardPoints.forEachIndexed { index, point ->
            val (fValue, _, pointNum) = point
            val rowNum = 28 + index
            val row = sheet.createRow(rowNum)
            val excelRowNum = rowNum + 1

            // Column F - Standard concentration
            row.createCell(5).setCellValue(fValue.toDouble())

            // Column G - CPM value
            when (rowNum) {
                29 -> row.createCell(6).cellFormula = "G24"
                30 -> row.createCell(6).cellFormula = "G25"
                31 -> row.createCell(6).cellFormula = "G26"
                32 -> row.createCell(6).cellFormula = "G27"
                33 -> row.createCell(6).cellFormula = "G28"
                34 -> row.createCell(6).cellFormula = "G29"
                35 -> row.createCell(6).cellFormula = "G30"
                36 -> row.createCell(6).cellFormula = "G31"
                37 -> row.createCell(6).cellFormula = "G32"
                38 -> row.createCell(6).cellFormula = "G33"
                39 -> row.createCell(6).cellFormula = "G34"
                40 -> row.createCell(6).cellFormula = "G35"
                41 -> row.createCell(6).cellFormula = "G36"
            }

            // Column H - %Bo-Bg
            row.createCell(6).cellFormula = "(G$excelRowNum-\$I\$16)*100/\$J\$18"

            // Column I - Log(dose)
            row.createCell(7).cellFormula = "LOG(F$excelRowNum)"

            // Column J - Logit(B-Bg)
            row.createCell(8).cellFormula = "LOG(H$excelRowNum/(100-H$excelRowNum))"

            // Column K - X (logDose)
            row.createCell(10).cellFormula = "I$excelRowNum"

            // Column L - Y (logit)
            row.createCell(11).cellFormula = "J$excelRowNum"

            // Column M - Odczyt [pg] calculation
            row.createCell(12).cellFormula =
                "10^((LOG((G$excelRowNum-\$I\$16)*100/\$J\$18/(100-(G$excelRowNum-\$I\$16)*100/\$J\$18))-\$R\$19)/\$R\$20)"

            // Column N - Punkt nr
            row.createCell(13).setCellValue(pointNum)

            // Column O - c.v. % (only for first of each duplicate)
            if (pointNum.endsWith("1") || standardPoints.getOrNull(index + 1)?.get(2) != pointNum) {
                val nextRow = excelRowNum + 1
                row.createCell(14).cellFormula = "100*STDEV(O$excelRowNum:O$nextRow)/AVERAGE(O$excelRowNum:O$nextRow)"
            }

            // Column P - Delta%
            row.createCell(15).cellFormula = "(O$excelRowNum-F$excelRowNum)*100/F$excelRowNum"

            // Column Q - ng*N
            row.createCell(16).cellFormula = "O$excelRowNum/F$excelRowNum"
        }

        // Add the sum formulas
        val sumRow = sheet.createRow(42)
        sumRow.createCell(7).setCellValue("SUMA:")
        sumRow.createCell(8).cellFormula = "SUM(J23:J36)"
    }

}

private fun SheetBuilder.dataHeaders() {
    this[42]["           "]["Wprowadż"]["     "]["     "]["Wynik"]["Średnia"]
    this[43]["Nr probówki"]["cpm     "]["ng/ml"]["rozc."]["ng/ml"]["c.v.   "]
}

private fun SheetBuilder.sumFormula() {
    this[36][7, "SUMA:", VAL][8, "SUM(J23:J36)", FORM]
}

private fun SheetBuilder.standarCurveHeadersAndInitialCalc() {
    this[6][4, "c.v. [%]", VAL][5, "STDEV(G14:G15)*100/F5", FORM][6, "STDEV(G16:G17)*100/G5", FORM][7, "STDEV(G18:G21)*100/H5", FORM][8, "STDEV(G23:G24)*100/I5", FORM][9, "STDEV(G25:G26)*100/J5", FORM][10, "STDEV(G27:G28)*100/K5", FORM][11, "STDEV(G29:G30)*100/L5", FORM][12, "STDEV(G31:G32)*100/M5", FORM][13, "STDEV(G33:G34)*100/N5", FORM][14, "STDEV(G35:G36)*100/O5", FORM][15, "STDEV(G37:G39)*100/P5", FORM]
    this[7][4, "Diff%Mean", VAL][5, "G14*100/\$F\$5-100", FORM][6, "G16*100/\$G\$5-100", FORM][7, "G18*100/\$H\$5-100", FORM][8, "G23*100/\$I\$5-100", FORM][9, "G25*100/\$J\$5-100", FORM][10, "G27*100/\$K\$5-100", FORM][11, "G29*100/\$L\$5-100", FORM][12, "G31*100/\$M\$5-100", FORM][13, "G33*100/\$N\$5-100", FORM][14, "G35*100/\$N\$5-100", FORM][15, "G37*100/\$N\$5-100", FORM]
    this[8][5, "G15*100/\$F\$5-100", FORM][6, "G17*100/\$G\$5-100", FORM][7, "G19*100/\$H\$5-100", FORM][8, "G24*100/\$I\$5-100", FORM][9, "G26*100/\$J\$5-100", FORM][10, "G28*100/\$K\$5-100", FORM][11, "G30*100/\$L\$5-100", FORM][12, "G32*100/\$M\$5-100", FORM][13, "G34*100/\$N\$5-100", FORM][14, "G36*100/\$N\$5-100", FORM][15, "G38*100/\$N\$5-100", FORM]
}

private fun SheetBuilder.controlCurve() {
    this[12][4, "Tube No.", VAL][5, "Content", VAL][6, "[cpm]", VAL][7, "Mean; c.v.", VAL][8, "Akceptuj", VAL]
    this[13][4, "A7", FORM][5, "B7", FORM][6, "C7", FORM][7, "AVERAGE(G14:G15)", FORM][8, "H14", FORM]
    this[14][4, "A8", FORM][5, "B8", FORM][6, "C8", FORM][7, "STDEV(G14:G15)*100/H14", FORM]
    this[15][4, "A9", FORM][5, "O", VAL][6, "C9", FORM][7, "AVERAGE(G16:G17)", FORM][8, "H16", FORM][9, "Bo-Bg", VAL]
    this[16][4, "A10", FORM][5, "O", VAL][6, "C10", FORM][7, "STDEV(G16:G17)*100/H16", FORM][9, "I18-I16", FORM]
    this[17][4, "A11", FORM][5, "N", VAL][6, "C11", FORM][7, "AVERAGE(G18:G20)", FORM][8, "H18", FORM][9, "I18-I16", FORM]
    this[18][4, "A12", FORM][5, "N", VAL][6, "C12", FORM][7, "STDEV(G18:G20)*100/H18", FORM]
    this[19][4, "A13", FORM][5, "N", VAL][6, "C13", FORM]
}

private fun SheetBuilder.renderStandardCurve() {
    // Row 23 - Wzorzec headers and binding calculations
    this[22][10, "Parametry regresji", VAL][13, "Y=a+bX", VAL]
    // Row 24 - N, SD headers
    this[23][10, "N", VAL][12, "SD", VAL]
    // Row 25 - N count and b calculation
    this[24][10, "COUNT(M25:M40)", FORM][11, "(COUNT(M25:M38)*SUMPRODUCT(M25:M38,N25:N38)-SUM(M25:M38)*SUM(N25:N38))/(COUNT(M25:M38)*SUMSQ(M25:M38)-(SUM(M25:M38))^2)", FORM][12, "(((COUNT(M25:M40)*SUMSQ(N25:N40)-SUM(N25:N40)^2)/(COUNT(M25:M40)*SUMSQ(M25:M40)-SUM(M25:M40)^2))*(1-N21^2)/(COUNT(M25:M40)-2))^0.5", FORM]
    // Row 26 - a calculation
    this[25][9, "a=", VAL][10, "SUM(N25:N38)/COUNT(M25:M38)-N19*SUM(M25:M38)/COUNT(M25:M38)", FORM][11, "(O19^2*SUMSQ(M25:M40)/COUNT(M25:M40))^0.5", FORM][13, "b=", VAL][14, "N19", FORM]
    // Row 27 - r calculation
    this[26][9, "r=", VAL][10, "CORREL(M25:M40,N25:N40)", FORM][14, "N21", FORM]
    // Row 28 - Logit transformation headers
    this[27][6, "bindingPercent", VAL][7, "logDose", VAL][8, "logarithmRealZero", VAL][11, "logDose", VAL][12, "logarithmRealZero", VAL][14, "Logit(B-Bg)", VAL]
    // Row 29 - Standard curve point 1 calculations
    this[28][5, "STD", VAL][6, "(G23-\$I\$16)*100/\$J\$18", FORM][7, "LOG(F23)", FORM][8, "LOG(H23/(100-H23))", FORM][10, "I23", FORM][11, "J23", FORM][12, "10^((LOG((G23-\$I\$16)*100/\$J\$18/(100-(G23-\$I\$16)*100/\$J\$18))-\$R\$19)/\$R\$20)", FORM][13, "1", VAL][14, "100*STDEV(O25:O26)/AVERAGE(O25:O26)", FORM][15, "(O25-F23)*100/F23", FORM][16, "O25/F23", FORM]

    this[21][5, "STD", VAL][7, "%Bo-Bg", VAL][8, "Log(dose)", VAL][9, "Logit(B-Bg)", VAL]
}

private fun SheetBuilder.renderPoints(cpmData: List<Int>) {
    (44..404).forEach { i ->
        val rowIndex = i + 1 // For 1-based indexing in formulas
        this[i].also { row ->
            // Column A - Tube numbers (continuing from previous)
            if (i == 44) row["25"] else row[0, "A${i}+1", FORM]
            // Column B - CPM values
            if (i - 44 < cpmData.size) row[1, cpmData[i - 44], VAL]
            // Column C - Cortisol concentration formula
            row[2, "10^((LOG((B$rowIndex-\$I\$16)*100/\$J\$18/(100-(B$rowIndex-\$I\$16)*100/\$J\$18))-\$R\$19)/\$R\$20)", FORM]
            // Column D - Dilution factor
            row[3, if (i == 44) 1.0 else "D${i}", if (i == 44) VAL else FORM]
            // Column E - Result (concentration * dilution)
            row[4, "C$rowIndex*D$rowIndex", FORM]
            when (i) {
                // Column F, G, H - Average (for duplicate samples), Coefficient of variation,  Sample identifiers
                in (44..404 step 2) -> row[5, "AVERAGE(E${i}:E${i + 1})", FORM][6, "100*STDEV(E${i}:E${i + 1})/F$rowIndex", FORM][7, i - 43, VAL]
                // Column H - Sample identifiers
                in (45..403 step 2) -> row[7, i - 43, VAL]
            }
        }
    }
}

private fun SheetBuilder.renderStandardPoints(standardPoints: List<List<String>>) {
    standardPoints.forEachIndexed { index, point ->
        val (fValue, _, pointNum) = point
        val rowNum = 22 + index
        val excelRowNum = rowNum + 1
        val dataIndex = 15 + 1
        val `Bo-Bg` = "J\$18"
        val `%Bo-Bg` = "(G$excelRowNum-\$I\$$dataIndex)*100/\$${`Bo-Bg`}" // col H
        val `Log(dose)` = "LOG(F$excelRowNum)" // col I
        val `Logit(B-Bg)` = "LOG(H$excelRowNum/(100-H$excelRowNum))" // col J
        // Column F - Standard concentration
        val row = this[rowNum][5, fValue.toDouble(), VAL][6, "C$dataIndex", FORM]

        // Column H - %Bo-Bg, I - Log(dose), J - Logit(B-Bg), K - X (logDose), L - Y (logit), M - Odczyt [pg] calculation, N - Punkt nr
        row[4, "A${dataIndex}", FORM][6, `%Bo-Bg`, FORM][7, `Log(dose)`, FORM][8, `Logit(B-Bg)`, FORM][10, "I$excelRowNum", FORM][11, "J$excelRowNum", FORM][12, "10^((LOG((G$excelRowNum-\$I\$16)*100/\$J\$18/(100-(G$excelRowNum-\$I\$16)*100/\$J\$18))-\$R\$19)/\$R\$20)", FORM][13, pointNum, VAL]

        // Column O - c.v. % (only for first of each duplicate)
        if (pointNum.endsWith("1") || standardPoints.getOrNull(index + 1)?.get(2) != pointNum) {
            row[14, "100*STDEV(O$excelRowNum:O${excelRowNum + 1})/AVERAGE(O$excelRowNum:O${excelRowNum + 1})", FORM]
        }

        // Column P, Q - Delta%, ng*N
        row[15, "(O$excelRowNum-F$excelRowNum)*100/F$excelRowNum", FORM][16, "O$excelRowNum/F$excelRowNum", FORM]
    }
}