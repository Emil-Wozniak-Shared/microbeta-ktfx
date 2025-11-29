package pl.ejdev.medic.architecture

import org.koin.dsl.module
import pl.ejdev.medic.architecture.ria.adapter.RiaResultReadAdapter
import pl.ejdev.medic.architecture.ria.port.read.RiaResultReadPort
import pl.ejdev.medic.architecture.ria.usecase.RiaResultReadUseCase
import pl.ejdev.medic.architecture.xlsx.adapter.read.ReadXlsxAdapter
import pl.ejdev.medic.architecture.xlsx.adapter.write.WriteXlsxAdapter
import pl.ejdev.medic.architecture.xlsx.port.read.ReadXlsxPort
import pl.ejdev.medic.architecture.xlsx.port.write.WriteXlsxPort
import pl.ejdev.medic.architecture.xlsx.usecase.read.ReadXlsxUseCase
import pl.ejdev.medic.architecture.xlsx.usecase.write.WriteXlsxUseCase

object ArchitectureConfiguration {
    private val xlsxModule = module {
        single<WriteXlsxPort> { WriteXlsxAdapter() }
        single<ReadXlsxPort> { ReadXlsxAdapter() }
        single { WriteXlsxUseCase(get(), get()) }
        single { ReadXlsxUseCase(get()) }
    }

    private val riaModule = module {
        single<RiaResultReadPort> { RiaResultReadAdapter() }
        single<RiaResultReadUseCase> { RiaResultReadUseCase(get()) }
    }

    val modules = arrayOf(xlsxModule, riaModule)
}