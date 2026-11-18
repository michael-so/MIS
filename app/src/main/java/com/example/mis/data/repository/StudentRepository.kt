package com.example.mis.data.repository

import com.example.mis.data.model.Student
import kotlinx.coroutines.flow.Flow

interface StudentRepository {
    fun getStudents(): Flow<List<Student>>
    suspend fun addStudent(student: Student)
    suspend fun deleteStudent(studentId: String)
    suspend fun getStudentByMatricNo(matricNo: String): Student?
}
