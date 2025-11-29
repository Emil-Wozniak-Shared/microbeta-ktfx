package pl.ejdev.medic.service

import org.koin.java.KoinJavaComponent.inject
import pl.ejdev.medic.controller.SettingsController
import pl.ejdev.medic.model.Sample
import pl.ejdev.medic.model.Settings

class ControlCurveProvider {
    private val settingsController: SettingsController by inject(SettingsController::class.java)

    fun resolveType(id: Int): Sample.Type {
        val (curve, values) = calibrations()
        val totalEnd = curve.totalCount - 1
        val nsbEnd = totalEnd + curve.nsbCount
        val zeroEnd = nsbEnd + curve.zeroCount
        val calibrationEnd = zeroEnd + values.size

        return when (id) {
            in 0..totalEnd -> Sample.Type.T
            in (totalEnd + 1)..nsbEnd -> Sample.Type.N
            in (nsbEnd + 1)..zeroEnd -> Sample.Type.O
            in (zeroEnd + 1)..calibrationEnd -> Sample.Type.C
            else -> Sample.Type.S
        }
    }

    fun resolveType(id: Int, type: Sample.Type): String {
        val (curve, values) = calibrations()
        val totalEnd = curve.totalCount - 1
        val nsbEnd = totalEnd + curve.nsbCount
        val zeroEnd = nsbEnd + curve.zeroCount
        val calibrationEnd = zeroEnd + values.size

        return when {
            (id in 0..totalEnd) && type.isTotal() -> Sample.Type.T.value
            (id in (totalEnd + 1)..nsbEnd) && type.isNsb() -> Sample.Type.N.value
            (id in (nsbEnd + 1)..zeroEnd) && type.isZero() -> Sample.Type.O.value
            (id in (zeroEnd + 1)..calibrationEnd) && type.isControlPoint() -> {
                val index = id - (zeroEnd + 1)
                return values[index]
            }
            else -> Sample.Type.S.value
        }
    }

    fun calibrations(): Pair<Settings.Curve, List<String>> {
        val settings: Settings = settingsController.settingsProperty.get()
        val curve = settings.curve
        val repeats = curve.calibration.repeats
        val values = curve.calibration.values.flatMap { text ->
            List(repeats) {text}
        }
        return (curve to values)
    }
}