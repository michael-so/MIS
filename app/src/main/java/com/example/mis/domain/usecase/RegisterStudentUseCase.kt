package com.example.mis.domain.usecase

import com.example.mis.data.model.Student
import com.example.mis.data.repository.StudentRepository
import com.example.mis.domain.model.RegistrationResult

class RegisterStudentUseCase(
    private val repository: StudentRepository,
    private val validator: ValidateStudentInputUseCase
) {
    suspend operator fun invoke(
        name: String,
        matricNo: String,
        course: String,
        faculty: String
    ): RegistrationResult {
        val errors = validator(name, matricNo, course, faculty)
        if (!errors.isValid) {
            return RegistrationResult.Error(errors)
        }

        val student = Student(
            name = name.trim(),
            matricNo = matricNo.trim().uppercase(),
            course = course.trim(),
            faculty = faculty.trim()
        )
        repository.addStudent(student)
        return RegistrationResult.Success(student)
    }
}
