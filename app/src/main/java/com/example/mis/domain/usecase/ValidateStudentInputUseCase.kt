package com.example.mis.domain.usecase

import com.example.mis.data.repository.StudentRepository
import com.example.mis.domain.model.StudentFormErrors

class ValidateStudentInputUseCase(
    private val repository: StudentRepository
) {
    suspend operator fun invoke(
        name: String,
        matricNo: String,
        course: String,
        faculty: String
    ): StudentFormErrors {
        val nameErr = if (name.isBlank()) "Student name is required" else null

        val matricClean = matricNo.trim()
        val matricErr = when {
            matricClean.isBlank() -> "Matric No. is required"
            repository.getStudentByMatricNo(matricClean) != null -> "Matric No. '$matricClean' is already registered"
            else -> null
        }

        val courseErr = if (course.isBlank()) "Course is required" else null
        val facultyErr = if (faculty.isBlank()) "Faculty is required" else null

        return StudentFormErrors(
            nameError = nameErr,
            matricError = matricErr,
            courseError = courseErr,
            facultyError = facultyErr
        )
    }
}
