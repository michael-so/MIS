package com.example.mis.domain.usecase

import com.example.mis.data.repository.InMemoryStudentRepository
import com.example.mis.domain.model.RegistrationResult
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RegisterStudentUseCaseTest {

    private lateinit var repository: InMemoryStudentRepository
    private lateinit var useCases: StudentUseCases

    @Before
    fun setUp() {
        repository = InMemoryStudentRepository()
        useCases = StudentUseCases.create(repository)
    }

    @Test
    fun registerStudent_success() = runBlocking {
        val result = useCases.registerStudent(
            name = "Test Student",
            matricNo = "TEST1234",
            course = "Computer Science",
            faculty = "Faculty of Computing"
        )

        assertTrue(result is RegistrationResult.Success)
        val students = useCases.getStudents().first()
        assertTrue(students.any { it.matricNo == "TEST1234" })
    }

    @Test
    fun registerStudent_duplicateMatric_returnsError() = runBlocking {
        val result = useCases.registerStudent(
            name = "Duplicate Student",
            matricNo = "AI210045", // Already in sample repository
            course = "Computer Science",
            faculty = "Faculty of Computing"
        )

        assertTrue(result is RegistrationResult.Error)
        val errorResult = result as RegistrationResult.Error
        assertEquals("Matric No. 'AI210045' is already registered", errorResult.errors.matricError)
    }

    @Test
    fun updateStudent_success() = runBlocking {
        val firstStudent = useCases.getStudents().first().first()

        val result = useCases.updateStudent(
            studentId = firstStudent.id,
            name = "Ahmad Updated Name",
            matricNo = firstStudent.matricNo,
            course = "Cyber Security",
            faculty = "Faculty of Computing"
        )

        assertTrue(result is RegistrationResult.Success)
        val updatedStudent = useCases.getStudents().first().find { it.id == firstStudent.id }
        assertEquals("Ahmad Updated Name", updatedStudent?.name)
        assertEquals("Cyber Security", updatedStudent?.course)
    }

    @Test
    fun updateStudent_duplicateMatric_returnsError() = runBlocking {
        val students = useCases.getStudents().first()
        val student1 = students[0]
        val student2 = students[1]

        val result = useCases.updateStudent(
            studentId = student1.id,
            name = student1.name,
            matricNo = student2.matricNo, // Try to steal student2's matric number
            course = student1.course,
            faculty = student1.faculty
        )

        assertTrue(result is RegistrationResult.Error)
        val errorResult = result as RegistrationResult.Error
        assertEquals("Matric No. '${student2.matricNo}' is already assigned to another student", errorResult.errors.matricError)
    }

    @Test
    fun deleteStudent_removesStudentFromRepository() = runBlocking {
        val initialCount = useCases.getStudents().first().size
        val firstStudent = useCases.getStudents().first().first()

        useCases.deleteStudent(firstStudent.id)

        val updatedStudents = useCases.getStudents().first()
        assertEquals(initialCount - 1, updatedStudents.size)
        assertTrue(updatedStudents.none { it.id == firstStudent.id })
    }
}
