package com.example.a20250308_seenivasaprabhuramdoss_nycschools

import com.google.gson.annotations.SerializedName

data class SchoolList(
    @SerializedName("dbn") val dbn: String,
    @SerializedName("school_name") val school_name: String?,
    @SerializedName("overview_paragraph") val overview_paragraph: String?,
    @SerializedName("borough") val borough: String?,
    @SerializedName("location") val location: String?,
    @SerializedName("phone_number") val phone: String?,
    @SerializedName("website") val website: String?,
)
