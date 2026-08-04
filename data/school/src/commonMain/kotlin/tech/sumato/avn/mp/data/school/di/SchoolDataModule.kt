package tech.sumato.avn.mp.data.school.di

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import tech.sumato.avn.mp.data.school.mapper.SchoolMapper
import tech.sumato.avn.mp.data.school.remote.SchoolApi
import tech.sumato.avn.mp.data.school.repository.SchoolRepositoryImpl
import tech.sumato.avn.mp.domain.school.repository.SchoolRepository
import tech.sumato.avn.mp.domain.school.usecase.GetSchoolsUseCase

val SchoolDataModule = module {
    singleOf(::SchoolApi)
    singleOf(::SchoolMapper)
    singleOf(::SchoolRepositoryImpl) { bind<SchoolRepository>() }
    singleOf(::GetSchoolsUseCase)
}
