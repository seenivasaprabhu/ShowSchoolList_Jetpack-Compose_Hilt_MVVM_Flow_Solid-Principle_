package com.example.a20250308_seenivasaprabhuramdoss_nycschools


import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class SchoolViewModelTest {

    private lateinit var viewModel: SchoolViewModel
    private lateinit var getSchoolsUseCase: GetSchoolsUseCase
    private lateinit var getSchoolDetailsUseCase: GetSchoolDetailsUseCase

    @Before
    fun setUp() {
        getSchoolsUseCase = mock(GetSchoolsUseCase::class.java)
        getSchoolDetailsUseCase = mock(GetSchoolDetailsUseCase::class.java)
        viewModel = SchoolViewModel(getSchoolsUseCase, getSchoolDetailsUseCase)
    }

    @Test
    fun `fetchSchools should update school list`() = runTest {
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



        whenever(getSchoolsUseCase()).thenReturn(flow { emit(mockSchools) })

        viewModel.getSchoolList()

        assertEquals(2, viewModel.schoolList.value.size)
        assertEquals("Test School 1", viewModel.schoolList.value[0].school_name)
        assertEquals("Test School 2", viewModel.schoolList.value[1].school_name)

        verify(getSchoolsUseCase).invoke()
    }

    @Test
    fun `fetchSchoolDetail should update school detail`() = runTest {
        val dbn = "01M292"
        val mockDetails = listOf(
            SchoolDetailModel(dbn, "Test School", "450", "420", "430","")
        )

        whenever(getSchoolDetailsUseCase(dbn)).thenReturn(flow { emit(mockDetails) })

        viewModel.getSchoolDetails(dbn)

        assertEquals("Test School", viewModel.satScore.value?.school_name)
        assertEquals("450", viewModel.satScore.value?.sat_math_avg_score)
        assertEquals("420", viewModel.satScore.value?.sat_critical_reading_avg_score)
        assertEquals("430", viewModel.satScore.value?.sat_writing_avg_score)

        verify(getSchoolDetailsUseCase).invoke(dbn)
    }
}