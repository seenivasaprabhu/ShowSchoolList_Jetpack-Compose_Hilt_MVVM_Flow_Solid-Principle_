package com.example.a20250308_seenivasaprabhuramdoss_nycschools

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSchoolDetailsUseCase @Inject constructor(private val repository: SchoolRepository) {
    operator fun invoke(dbn: String): Flow<List<SchoolDetailModel>> = repository.getSchoolDetails(dbn)
}
