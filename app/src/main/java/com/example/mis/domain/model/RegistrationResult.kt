package com.example.mis.domain.model

import com.example.mis.data.model.Student

data class StudentFormErrors(
    val nameError: String? = null,
    val matricError: String? = null,
    val courseError: String? = null,
    val facultyError: String? = null
) {
    val isValid: Boolean get() = nameError == null && matricError == null && courseError == null && facultyError == null
}

sealed class RegistrationResult {
    data class Success(val student: Student) : RegistrationResult()
    data class Error(val errors: StudentFormErrors) : RegistrationResult()
}
