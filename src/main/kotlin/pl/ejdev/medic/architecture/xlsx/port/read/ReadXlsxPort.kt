package pl.ejdev.medic.architecture.xlsx.port.read

import pl.ejdev.medic.architecture.shared.port.Port
import pl.ejdev.medic.architecture.xlsx.dto.ReadXlsxEvent
import pl.ejdev.medic.architecture.xlsx.dto.ReadXlsxResult

interface ReadXlsxPort: Port<ReadXlsxEvent, ReadXlsxResult>