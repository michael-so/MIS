package com.example.mis.domain.usecase

import com.example.mis.data.model.Student
import com.example.mis.data.repository.StudentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetFacultiesUseCase(
    private val repository: StudentRepository
) {
    operator fun invoke(): Flow<Map<String, List<Student>>> {
        return repository.getStudents().map { students ->
            students.groupBy { it.faculty }
        }
    }
}
