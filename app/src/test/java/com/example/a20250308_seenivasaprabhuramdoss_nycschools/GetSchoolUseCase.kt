package com.example.a20250308_seenivasaprabhuramdoss_nycschools

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.whenever

class GetSchoolsUseCaseTest {

    private lateinit var getSchoolsUseCase: GetSchoolsUseCase
    private lateinit var schoolRepository: SchoolRepository

    @Before
    fun setUp() {
        schoolRepository = mock(SchoolRepository::class.java)
        getSchoolsUseCase = GetSchoolsUseCase(schoolRepository)
    }

    @Test
    fun `invoke should return list of schools`() = runTest {
        val mockSchools = listOf(
            SchoolList(
                dbn = "01M292",
                school_name = "Test School 1",
                overview_paragraph = "A great school with advanced programs.",
                borough = "Manhattan",
                location = "123 Main St, NY, NY 10001",
                phone = "123-456-7890",
                website = "https://testschool1.edu"
            ),
            SchoolList(
                dbn = "02X114",
                school_name = "Test School 2",
                overview_paragraph = "An innovative learning environment.",
                borough = "Bronx",
                location = "456 Elm St, Bronx, NY 10453",
                phone = "987-654-3210",
                website = "https://testschool2.edu"
            )
        )

        whenever(schoolRepository.getSchools()).thenReturn(flow { emit(mockSchools) })

        val result = getSchoolsUseCase()

        result.collect { schools ->
            assertEquals(2, schools.size)
            assertEquals("Test School 1", schools[0].school_name)
            assertEquals("Test School 2", schools[1].school_name)
        }

        verify(schoolRepository).getSchools()
    }
}