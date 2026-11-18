package com.example.mis.domain.usecase

import com.example.mis.data.model.Student

class SearchStudentsUseCase {
    operator fun invoke(students: List<Student>, query: String): List<Student> {
        if (query.isBlank()) return students
        val q = query.trim().lowercase()
        return students.filter {
            it.name.lowercase().contains(q) ||
                    it.matricNo.lowercase().contains(q) ||
                    it.course.lowercase().contains(q) ||
                    it.faculty.lowercase().contains(q)
        }
    }
}
