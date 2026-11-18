package com.example.mis.data.repository

import com.example.mis.data.model.Student
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class InMemoryStudentRepository : StudentRepository {

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

    override fun getStudents(): Flow<List<Student>> = _students.asStateFlow()

    override suspend fun addStudent(student: Student) {
        _students.value = listOf(student) + _students.value
    }

    override suspend fun deleteStudent(studentId: String) {
        _students.value = _students.value.filter { it.id != studentId }
    }

    override suspend fun getStudentByMatricNo(matricNo: String): Student? {
        return _students.value.find { it.matricNo.equals(matricNo, ignoreCase = true) }
    }
}
