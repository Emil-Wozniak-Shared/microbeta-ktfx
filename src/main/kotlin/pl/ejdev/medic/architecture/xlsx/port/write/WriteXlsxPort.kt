package pl.ejdev.medic.architecture.xlsx.port.write

import pl.ejdev.medic.architecture.shared.dto.NoResult
import pl.ejdev.medic.architecture.shared.port.Port
import pl.ejdev.medic.architecture.xlsx.dto.WriteXlsxEvent

interface WriteXlsxPort: Port<WriteXlsxEvent, NoResult>