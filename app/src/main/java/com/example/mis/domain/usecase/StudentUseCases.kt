package com.example.mis.domain.usecase

import com.example.mis.data.repository.StudentRepository

data class StudentUseCases(
    val getStudents: GetStudentsUseCase,
    val searchStudents: SearchStudentsUseCase,
    val registerStudent: RegisterStudentUseCase,
    val updateStudent: UpdateStudentUseCase,
    val deleteStudent: DeleteStudentUseCase,
    val validateInput: ValidateStudentInputUseCase
) {
    companion object {
        fun create(repository: StudentRepository): StudentUseCases {
            val validateInput = ValidateStudentInputUseCase(repository)
            return StudentUseCases(
                getStudents = GetStudentsUseCase(repository),
                searchStudents = SearchStudentsUseCase(),
                registerStudent = RegisterStudentUseCase(repository, validateInput),
                updateStudent = UpdateStudentUseCase(repository),
                deleteStudent = DeleteStudentUseCase(repository),
                validateInput = validateInput
            )
        }
    }
}
