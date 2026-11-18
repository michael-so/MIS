package com.example.mis.ui

import androidx.lifecycle.ViewModel
import com.example.mis.data.Student
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class StudentViewModel : ViewModel() {

    private val _students = MutableStateFlow<List<Student>>(
        listOf(
            Student(
                name = "Ahmad Bin Zulkifli",
                matricNo = "AI210045",
                course = "Software Engineering",
                faculty = "Faculty of Computing"
            ),
            Student(
                name = "Siti Nurhaliza Binti Hassan",
                matricNo = "BI210112",
                course = "Computer Science",
                faculty = "Faculty of Computing"
            ),
            Student(
                name = "Tan Wei Ming",
                matricNo = "CI220089",
                course = "Information Technology",
                faculty = "Faculty of Computing"
            ),
            Student(
                name = "Nurul Izzah Binti Rosli",
                matricNo = "EI220150",
                course = "Data Science",
                faculty = "Faculty of Computing"
            )
        )
    )
    val students: StateFlow<List<Student>> = _students.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val filteredStudents: StateFlow<List<Student>> = combine(_students, _searchQuery) { list, query ->
        if (query.isBlank()) {
            list
        } else {
            val q = query.trim().lowercase()
            list.filter {
                it.name.lowercase().contains(q) ||
                        it.matricNo.lowercase().contains(q) ||
                        it.course.lowercase().contains(q) ||
                        it.faculty.lowercase().contains(q)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = _students.value
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
        _students.value = _students.value.filter { it.id != student.id }
        _studentToDelete.value = null
        if (_selectedStudentForDetail.value?.id == student.id) {
            _selectedStudentForDetail.value = null
        }
        _snackbarMessage.tryEmit("Student '${student.name}' deleted successfully.")
    }

    fun registerStudent(): Boolean {
        var isValid = true

        val name = _nameInput.value.trim()
        val matricNo = _matricInput.value.trim()
        val course = _courseInput.value.trim()
        val faculty = _facultyInput.value.trim()

        if (name.isBlank()) {
            _nameError.value = "Student name is required"
            isValid = false
        } else {
            _nameError.value = null
        }

        if (matricNo.isBlank()) {
            _matricError.value = "Matric No. is required"
            isValid = false
        } else if (_students.value.any { it.matricNo.equals(matricNo, ignoreCase = true) }) {
            _matricError.value = "Matric No. '$matricNo' is already registered"
            isValid = false
        } else {
            _matricError.value = null
        }

        if (course.isBlank()) {
            _courseError.value = "Course is required"
            isValid = false
        } else {
            _courseError.value = null
        }

        if (faculty.isBlank()) {
            _facultyError.value = "Faculty is required"
            isValid = false
        } else {
            _facultyError.value = null
        }

        if (!isValid) return false

        val newStudent = Student(
            name = name,
            matricNo = matricNo,
            course = course,
            faculty = faculty
        )

        _students.value = listOf(newStudent) + _students.value
        clearForm()
        _snackbarMessage.tryEmit("Student '$name' registered successfully!")
        _selectedTab.value = 0 // Switch to student list tab
        return true
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
