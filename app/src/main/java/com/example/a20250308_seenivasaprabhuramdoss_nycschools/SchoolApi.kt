package com.example.a20250308_seenivasaprabhuramdoss_nycschools

import retrofit2.http.GET
import retrofit2.http.Query

interface SchoolApi {
    @GET("s3k6-pzi2.json")
    suspend fun getSchools(): List<SchoolList>

    @GET("f9bf-2cp4.json")
    suspend fun getSchoolDetails(@Query("dbn") dbn: String): List<SchoolDetailModel>
}