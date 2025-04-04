package com.example.a20250308_seenivasaprabhuramdoss_nycschools

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SchoolViewModel @Inject constructor(private val getSchoolsUseCase: GetSchoolsUseCase,
                                          private val getSchoolDetailsUseCase: GetSchoolDetailsUseCase) : ViewModel(){
    private val _schoolList = MutableStateFlow<List<SchoolList>>(emptyList())
    val schoolList = _schoolList.asStateFlow()

    private val _schoolDetail = MutableStateFlow<SchoolDetailModel?>(null)
    val satScore = _schoolDetail.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error  = _error.asStateFlow()

    init {
        getSchoolList()
    }

    fun getSchoolList() {
        viewModelScope.launch { getSchoolsUseCase().catch { e-> _error.value  = e.message}.collect { _schoolList.value = it } }
    }

    fun getSchoolDetails(schoolId: String) {
        viewModelScope.launch { getSchoolDetailsUseCase(schoolId).catch { e-> _error.value  = e.message }.collect { _schoolDetail.value = it.firstOrNull() } }
    }

}