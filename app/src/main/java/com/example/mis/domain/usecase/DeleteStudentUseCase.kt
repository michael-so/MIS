package com.example.mis.domain.usecase

import com.example.mis.data.repository.StudentRepository

class DeleteStudentUseCase(
    private val repository: StudentRepository
) {
    suspend operator fun invoke(studentId: String) {
        repository.deleteStudent(studentId)
    }
}
