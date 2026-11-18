package com.example.mis.data

import java.util.UUID

data class Student(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val matricNo: String,
    val course: String,
    val faculty: String
)
