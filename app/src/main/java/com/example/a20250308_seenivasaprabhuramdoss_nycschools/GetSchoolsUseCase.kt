package com.example.a20250308_seenivasaprabhuramdoss_nycschools

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSchoolsUseCase @Inject constructor(private val repository: SchoolRepository) {
    operator fun invoke(): Flow<List<SchoolList>> = repository.getSchools()
}
