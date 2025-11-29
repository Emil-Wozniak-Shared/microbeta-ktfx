package pl.ejdev.medic.architecture.ria.port.read

import pl.ejdev.medic.architecture.ria.dto.RiaResultReadEvent
import pl.ejdev.medic.architecture.shared.port.Port
import pl.ejdev.medic.architecture.xlsx.dto.RiaResultReadResult

interface RiaResultReadPort: Port<RiaResultReadEvent, RiaResultReadResult>