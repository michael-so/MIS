package com.example.mis.domain.usecase

import com.example.mis.data.model.Student
import com.example.mis.data.repository.StudentRepository
import kotlinx.coroutines.flow.Flow

class GetStudentsUseCase(
    private val repository: StudentRepository
) {
    operator fun invoke(): Flow<List<Student>> = repository.getStudents()
}
