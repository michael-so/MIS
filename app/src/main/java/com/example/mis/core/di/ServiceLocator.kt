package com.example.mis.core.di

import com.example.mis.data.repository.InMemoryStudentRepository
import com.example.mis.data.repository.StudentRepository
import com.example.mis.domain.usecase.StudentUseCases

object ServiceLocator {
    @Volatile
    private var repository: StudentRepository? = null

    @Volatile
    private var studentUseCases: StudentUseCases? = null

    fun provideStudentRepository(): StudentRepository {
        return repository ?: synchronized(this) {
            repository ?: InMemoryStudentRepository().also { repository = it }
        }
    }

    fun provideStudentUseCases(customRepository: StudentRepository? = null): StudentUseCases {
        if (customRepository != null) {
            return StudentUseCases.create(customRepository)
        }
        return studentUseCases ?: synchronized(this) {
            studentUseCases ?: StudentUseCases.create(provideStudentRepository()).also { studentUseCases = it }
        }
    }

    fun setRepository(customRepository: StudentRepository) {
        synchronized(this) {
            repository = customRepository
            studentUseCases = StudentUseCases.create(customRepository)
        }
    }

    fun reset() {
        synchronized(this) {
            repository = null
            studentUseCases = null
        }
    }
}
