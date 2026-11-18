package com.example.mis.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mis.data.model.Student
import com.example.mis.data.repository.InMemoryStudentRepository
import com.example.mis.data.repository.StudentRepository
import com.example.mis.domain.model.RegistrationResult
import com.example.mis.domain.usecase.StudentUseCases
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class StudentViewModel(
    repository: StudentRepository = InMemoryStudentRepository(),
    private val useCases: StudentUseCases = StudentUseCases.create(repository)
) : ViewModel() {

    val students: StateFlow<List<Student>> = useCases.getStudents()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val filteredStudents: StateFlow<List<Student>> = combine(students, _searchQuery) { list, query ->
        useCases.searchStudents(list, query)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    private val _selectedStudentForDetail = MutableStateFlow<Student?>(null)
    val selectedStudentForDetail: StateFlow<Student?> = _selectedStudentForDetail.asStateFlow()

    private val _studentToDelete = MutableStateFlow<Student?>(null)
    val studentToDelete: StateFlow<Student?> = _studentToDelete.asStateFlow()

    // Registration Form Inputs
    private val _nameInput = MutableStateFlow("")
    val nameInput: StateFlow<String> = _nameInput.asStateFlow()

    private val _matricInput = MutableStateFlow("")
    val matricInput: StateFlow<String> = _matricInput.asStateFlow()

    private val _courseInput = MutableStateFlow("")
    val courseInput: StateFlow<String> = _courseInput.asStateFlow()

    private val _facultyInput = MutableStateFlow("")
    val facultyInput: StateFlow<String> = _facultyInput.asStateFlow()

    // Form Errors
    private val _nameError = MutableStateFlow<String?>(null)
    val nameError: StateFlow<String?> = _nameError.asStateFlow()

    private val _matricError = MutableStateFlow<String?>(null)
    val matricError: StateFlow<String?> = _matricError.asStateFlow()

    private val _courseError = MutableStateFlow<String?>(null)
    val courseError: StateFlow<String?> = _courseError.asStateFlow()

    private val _facultyError = MutableStateFlow<String?>(null)
    val facultyError: StateFlow<String?> = _facultyError.asStateFlow()

    // UI Message Event (Snackbar)
    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage: SharedFlow<String> = _snackbarMessage.asSharedFlow()

    fun setSelectedTab(index: Int) {
        _selectedTab.value = index
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun onNameChange(value: String) {
        _nameInput.value = value
        if (value.isNotBlank()) _nameError.value = null
    }

    fun onMatricChange(value: String) {
        _matricInput.value = value
        if (value.isNotBlank()) _matricError.value = null
    }

    fun onCourseChange(value: String) {
        _courseInput.value = value
        if (value.isNotBlank()) _courseError.value = null
    }

    fun onFacultyChange(value: String) {
        _facultyInput.value = value
        if (value.isNotBlank()) _facultyError.value = null
    }

    fun showStudentDetail(student: Student?) {
        _selectedStudentForDetail.value = student
    }

    fun requestDeleteStudent(student: Student?) {
        _studentToDelete.value = student
    }

    fun confirmDeleteStudent() {
        val student = _studentToDelete.value ?: return
        viewModelScope.launch {
            useCases.deleteStudent(student.id)
            _studentToDelete.value = null
            if (_selectedStudentForDetail.value?.id == student.id) {
                _selectedStudentForDetail.value = null
            }
            _snackbarMessage.emit("Student '${student.name}' deleted successfully.")
        }
    }

    fun registerStudent() {
        viewModelScope.launch {
            val result = useCases.registerStudent(
                name = _nameInput.value,
                matricNo = _matricInput.value,
                course = _courseInput.value,
                faculty = _facultyInput.value
            )

            when (result) {
                is RegistrationResult.Success -> {
                    _nameError.value = null
                    _matricError.value = null
                    _courseError.value = null
                    _facultyError.value = null
                    clearForm()
                    _snackbarMessage.emit("Student '${result.student.name}' registered successfully!")
                    _selectedTab.value = 0
                }
                is RegistrationResult.Error -> {
                    _nameError.value = result.errors.nameError
                    _matricError.value = result.errors.matricError
                    _courseError.value = result.errors.courseError
                    _facultyError.value = result.errors.facultyError
                }
            }
        }
    }

    fun clearForm() {
        _nameInput.value = ""
        _matricInput.value = ""
        _courseInput.value = ""
        _facultyInput.value = ""
        _nameError.value = null
        _matricError.value = null
        _courseError.value = null
        _facultyError.value = null
    }
}
