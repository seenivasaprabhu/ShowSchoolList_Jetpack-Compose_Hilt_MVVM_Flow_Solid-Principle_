package com.example.a20250308_seenivasaprabhuramdoss_nycschools

import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.whenever

class GetSchoolDetailsUseCaseTest {

    private lateinit var getSchoolDetailsUseCase: GetSchoolDetailsUseCase
    private lateinit var schoolRepository: SchoolRepository

    @Before
    fun setUp() {
        schoolRepository = mock(SchoolRepository::class.java)
        getSchoolDetailsUseCase = GetSchoolDetailsUseCase(schoolRepository)
    }

    @Test
    fun `invoke should return school details`() = runTest {
        val dbn = "01M292"
        val mockSchoolDetail = listOf(
            SchoolDetailModel(dbn, "Test School", "450", "420", "430","")
        )

        // Mock repository response
        whenever(schoolRepository.getSchoolDetails(dbn)).thenReturn(flow { emit(mockSchoolDetail) })

        // Execute use case
        val result = getSchoolDetailsUseCase(dbn)

        // Collect the result and assert
        result.collect { details ->
            assertEquals(1, details.size)
            assertEquals("Test School", details.first().school_name)
            assertEquals("450", details.first().sat_math_avg_score)
            assertEquals("420", details.first().sat_critical_reading_avg_score)
            assertEquals("430", details.first().sat_writing_avg_score)
        }

        // Verify repository method was called
        verify(schoolRepository).getSchoolDetails(dbn)
    }
}
