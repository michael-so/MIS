package com.example.mis.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mis.ui.StudentViewModel
import com.example.mis.ui.common.AppSearchBar
import com.example.mis.ui.common.EmptyStateView
import com.example.mis.ui.common.SummaryHeaderCard
import com.example.mis.ui.components.DeleteConfirmationDialog
import com.example.mis.ui.components.EditStudentDialog
import com.example.mis.ui.components.StudentCard
import com.example.mis.ui.components.StudentDetailDialog

@Composable
fun StudentListScreen(
    viewModel: StudentViewModel,
    onNavigateToRegister: () -> Unit,
    modifier: Modifier = Modifier
) {
    val filteredStudents by viewModel.filteredStudents.collectAsState()
    val allStudents by viewModel.students.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedStudentDetail by viewModel.selectedStudentForDetail.collectAsState()
    val studentToDelete by viewModel.studentToDelete.collectAsState()

    val studentToEdit by viewModel.studentToEdit.collectAsState()
    val editNameInput by viewModel.editNameInput.collectAsState()
    val editMatricInput by viewModel.editMatricInput.collectAsState()
    val editCourseInput by viewModel.editCourseInput.collectAsState()
    val editFacultyInput by viewModel.editFacultyInput.collectAsState()

    val editNameError by viewModel.editNameError.collectAsState()
    val editMatricError by viewModel.editMatricError.collectAsState()
    val editCourseError by viewModel.editCourseError.collectAsState()
    val editFacultyError by viewModel.editFacultyError.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Search Bar
        AppSearchBar(
            query = searchQuery,
            onQueryChange = { viewModel.setSearchQuery(it) },
            placeholder = "Search by name, matric, course, faculty..."
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Total registered summary banner
        SummaryHeaderCard(
            title = "Total Registered Students",
            countText = "${allStudents.size}",
            icon = Icons.Default.Group
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Student List or Empty States
        if (allStudents.isEmpty()) {
            EmptyStateView(
                icon = Icons.Default.Group,
                title = "No Students Registered Yet",
                message = "Click below to add a new student to the system.",
                actionText = "Register Student",
                onActionClick = onNavigateToRegister,
                modifier = Modifier.weight(1f)
            )
        } else if (filteredStudents.isEmpty()) {
            EmptyStateView(
                icon = Icons.Default.Search,
                title = "No Matching Student Found",
                message = "Try searching with a different keyword or matric number.",
                modifier = Modifier.weight(1f)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentPadding = PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(
                    items = filteredStudents,
                    key = { student -> student.id }
                ) { student ->
                    StudentCard(
                        student = student,
                        onCardClick = { viewModel.showStudentDetail(student) },
                        onEditClick = { viewModel.startEditStudent(student) },
                        onDeleteClick = { viewModel.requestDeleteStudent(student) }
                    )
                }
            }
        }
    }

    // Detail Modal Dialog
    selectedStudentDetail?.let { student ->
        StudentDetailDialog(
            student = student,
            onDismiss = { viewModel.showStudentDetail(null) },
            onEdit = {
                viewModel.showStudentDetail(null)
                viewModel.startEditStudent(student)
            },
            onDelete = {
                viewModel.showStudentDetail(null)
                viewModel.requestDeleteStudent(student)
            }
        )
    }

    // Edit Modal Dialog
    studentToEdit?.let {
        EditStudentDialog(
            name = editNameInput,
            matricNo = editMatricInput,
            course = editCourseInput,
            faculty = editFacultyInput,
            nameError = editNameError,
            matricError = editMatricError,
            courseError = editCourseError,
            facultyError = editFacultyError,
            onNameChange = { viewModel.onEditNameChange(it) },
            onMatricChange = { viewModel.onEditMatricChange(it) },
            onCourseChange = { viewModel.onEditCourseChange(it) },
            onFacultyChange = { viewModel.onEditFacultyChange(it) },
            onConfirm = { viewModel.confirmEditStudent() },
            onDismiss = { viewModel.cancelEditStudent() }
        )
    }

    // Delete Confirmation Dialog
    studentToDelete?.let { student ->
        DeleteConfirmationDialog(
            student = student,
            onConfirm = { viewModel.confirmDeleteStudent() },
            onDismiss = { viewModel.requestDeleteStudent(null) }
        )
    }
}
