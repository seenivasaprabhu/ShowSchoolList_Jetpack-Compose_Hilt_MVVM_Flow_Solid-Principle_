package com.example.a20250308_seenivasaprabhuramdoss_nycschools

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SchoolRepository @Inject constructor(private val api: SchoolApi) {
    fun getSchools(): Flow<List<SchoolList>> = flow { emit(api.getSchools()) }
    fun getSchoolDetails(dbn: String): Flow<List<SchoolDetailModel>> = flow { emit(api.getSchoolDetails(dbn)) }
}