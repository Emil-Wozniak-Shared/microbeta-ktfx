package pl.ejdev.medic.service

import javafx.stage.Stage
import ktfx.layouts.label
import pl.ejdev.medic.components.dialog
import pl.ejdev.medic.utils.SheetBuilder
import pl.ejdev.medic.utils.Type.FORM
import pl.ejdev.medic.utils.Type.VAL
import pl.ejdev.medic.utils.excel
import java.io.File

private const val WYDRUK = "Wydruk"
private const val VALUES_SHEET = "Mel8V09"
private const val INT_FORMAT = "#,##0"

interface XslxService {
    fun generate(
        owner: Stage,
        hormone: String,
        date: String,
        subject: String,
        initialData: List<List<String>>,
        cpmData: List<Int>,
        standardPoints: List<List<String>>
    )
}

class XslxServiceImpl : XslxService {
    override fun generate(
        owner: Stage,
        hormone: String,
        date: String,
        subject: String,
        initialData: List<List<String>>,
        cpmData: List<Int>,
        standardPoints: List<List<String>>
    ) {
        val fileName = "${hormone}-${date}.xlsx"
        val userHome = System.getProperty("user.home")
        File(userHome, fileName)
            .runCatching {
                excel(path) {
                    sheet(WYDRUK) { wydrukRows() }
                    sheet(VALUES_SHEET) { valuesRows(hormone, date, subject, initialData, standardPoints, cpmData) }
                }
            }
            .onFailure { e ->
                dialog(owner, "Create XLSX failed!") { label("${e.message}") {} }
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
        this[4][2, "Wpisz", VAL][4, "Mean [cpm]", VAL][5, "AVERAGE(G14:G15)", FORM, INT_FORMAT][6, "AVERAGE(G16:G17)", FORM, INT_FORMAT][7, "AVERAGE(G18:G20)", FORM, INT_FORMAT][8, "AVERAGE(G23:G24)", FORM, INT_FORMAT][9, "AVERAGE(G25:G26)", FORM, INT_FORMAT][10, "AVERAGE(G27:G28)", FORM, INT_FORMAT][11, "AVERAGE(G29:G30)", FORM, INT_FORMAT][12, "AVERAGE(G31:G32)", FORM, INT_FORMAT][13, "AVERAGE(G33:G34)", FORM, INT_FORMAT][14, "AVERAGE(G35:G36)", FORM, INT_FORMAT][15, "AVERAGE(G37:G39)", FORM, INT_FORMAT]
        this[5]["Nr probówki"]["Zawartość"]["cpm"]["N"][5, "COUNT(G14:G15)", FORM, INT_FORMAT][6, "COUNT(G16:G17)", FORM, INT_FORMAT][7, "COUNT(G18:G21)", FORM, INT_FORMAT][8, "COUNT(G23:G24)", FORM, INT_FORMAT][9, "COUNT(G25:G26)", FORM, INT_FORMAT][10, "COUNT(G27:G28)", FORM, INT_FORMAT][11, "COUNT(G29:G30)", FORM, INT_FORMAT][12, "COUNT(G31:G32)", FORM, INT_FORMAT][13, "COUNT(G33:G34)", FORM, INT_FORMAT][14, "COUNT(G35:G36)", FORM, INT_FORMAT][15, "COUNT(G37:G39)", FORM, INT_FORMAT]

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

        standardCurveHeadersAndInitialCalc()
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
        // 14 empty
        this[15][2, "Mel8V09!C42", FORM][4, "Wynik", VAL]
        this[16]["Mel8V09!A43", FORM]["Uwagi", VAL]["Mel8V09!C43", FORM]["Opis próby"]["Mel8V09!F43", FORM]["Mel8V09!G43", FORM]
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
}

private fun SheetBuilder.dataHeaders() {
    this[42]["           "]["Wprowadż"]["     "]["     "]["Wynik"]["Średnia"]
    this[43]["Nr probówki"]["cpm     "]["ng/ml"]["rozc."]["ng/ml"]["c.v.   "]
}

private fun SheetBuilder.sumFormula() {
    this[36][7, "SUMA:", VAL][8, "SUM(J23:J36)", FORM]
}

private fun SheetBuilder.standardCurveHeadersAndInitialCalc() {
    this[6][4, "c.v. [%]", VAL][5, "STDEV(G14:G15)*100/F5", FORM][6, "STDEV(G16:G17)*100/G5", FORM][7, "STDEV(G18:G21)*100/H5", FORM][8, "STDEV(G23:G24)*100/I5", FORM][9, "STDEV(G25:G26)*100/J5", FORM][10, "STDEV(G27:G28)*100/K5", FORM][11, "STDEV(G29:G30)*100/L5", FORM][12, "STDEV(G31:G32)*100/M5", FORM][13, "STDEV(G33:G34)*100/N5", FORM][14, "STDEV(G35:G36)*100/O5", FORM][15, "STDEV(G37:G39)*100/P5", FORM]
    this[7][4, "Diff%Mean", VAL][5, "G14*100/\$F\$5-100", FORM][6, "G16*100/\$G\$5-100", FORM][7, "G18*100/\$H\$5-100", FORM][8, "G23*100/\$I\$5-100", FORM][9, "G25*100/\$J\$5-100", FORM][10, "G27*100/\$K\$5-100", FORM][11, "G29*100/\$L\$5-100", FORM][12, "G31*100/\$M\$5-100", FORM][13, "G33*100/\$N\$5-100", FORM][14, "G35*100/\$N\$5-100", FORM][15, "G37*100/\$N\$5-100", FORM]
    this[8][5, "G15*100/\$F\$5-100", FORM][6, "G17*100/\$G\$5-100", FORM][7, "G19*100/\$H\$5-100", FORM][8, "G24*100/\$I\$5-100", FORM][9, "G26*100/\$J\$5-100", FORM][10, "G28*100/\$K\$5-100", FORM][11, "G30*100/\$L\$5-100", FORM][12, "G32*100/\$M\$5-100", FORM][13, "G34*100/\$N\$5-100", FORM][14, "G36*100/\$N\$5-100", FORM][15, "G38*100/\$N\$5-100", FORM]
}

private fun SheetBuilder.controlCurve() {
    this[12][4, "Tube No.", VAL][5, "Content", VAL][6, "[cpm]", VAL][7, "Mean; c.v.", VAL][8, "Akceptuj", VAL]
    this[13][4, "A7", FORM][5, "B7", FORM][6, "C7", FORM][7, "AVERAGE(G14:G15)", FORM][8, "H14", FORM, INT_FORMAT]
    this[14][4, "A8", FORM][5, "B8", FORM][6, "C8", FORM][7, "STDEV(G14:G15)*100/H14", FORM, INT_FORMAT]
    this[15][4, "A9", FORM][5, "O", VAL][6, "C9", FORM][7, "AVERAGE(G16:G17)", FORM, INT_FORMAT][8, "H16", FORM][9, "Bo-Bg", VAL]
    this[16][4, "A10", FORM][5, "O", VAL][6, "C10", FORM][7, "STDEV(G16:G17)*100/H16", FORM, INT_FORMAT][9, "I18-I16", FORM]
    this[17][4, "A11", FORM][5, "N", VAL][6, "C11", FORM][7, "AVERAGE(G18:G20)", FORM, INT_FORMAT][8, "H18", FORM][9, "I18-I16", FORM]
    this[18][4, "A12", FORM][5, "N", VAL][6, "C12", FORM][7, "STDEV(G18:G20)*100/H18", FORM, INT_FORMAT]
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
                in (44..404 step 2) -> row[5, "AVERAGE(E${i}:E${i + 1})", FORM, INT_FORMAT][6, "100*STDEV(E${i}:E${i + 1})/F$rowIndex", FORM, INT_FORMAT][7, i - 43, VAL]
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