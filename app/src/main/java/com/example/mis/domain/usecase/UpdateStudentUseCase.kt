package com.example.mis.domain.usecase

import com.example.mis.data.model.Student
import com.example.mis.data.repository.StudentRepository
import com.example.mis.domain.model.RegistrationResult
import com.example.mis.domain.model.StudentFormErrors

class UpdateStudentUseCase(
    private val repository: StudentRepository
) {
    suspend operator fun invoke(
        studentId: String,
        name: String,
        matricNo: String,
        course: String,
        faculty: String
    ): RegistrationResult {
        val nameErr = if (name.isBlank()) "Student name is required" else null

        val matricClean = matricNo.trim()
        val existingWithMatric = repository.getStudentByMatricNo(matricClean)
        val matricErr = when {
            matricClean.isBlank() -> "Matric No. is required"
            existingWithMatric != null && existingWithMatric.id != studentId ->
                "Matric No. '$matricClean' is already assigned to another student"
            else -> null
        }

        val courseErr = if (course.isBlank()) "Course is required" else null
        val facultyErr = if (faculty.isBlank()) "Faculty is required" else null

        val errors = StudentFormErrors(
            nameError = nameErr,
            matricError = matricErr,
            courseError = courseErr,
            facultyError = facultyErr
        )

        if (!errors.isValid) {
            return RegistrationResult.Error(errors)
        }

        val updatedStudent = Student(
            id = studentId,
            name = name.trim(),
            matricNo = matricClean.uppercase(),
            course = course.trim(),
            faculty = faculty.trim()
        )

        repository.updateStudent(updatedStudent)
        return RegistrationResult.Success(updatedStudent)
    }
}
