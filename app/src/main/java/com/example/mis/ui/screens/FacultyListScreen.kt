package com.example.mis.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Domain
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.mis.data.model.Student
import com.example.mis.ui.StudentViewModel
import com.example.mis.ui.components.DeleteConfirmationDialog
import com.example.mis.ui.components.EditStudentDialog
import com.example.mis.ui.components.StudentCard
import com.example.mis.ui.components.StudentDetailDialog

@Composable
fun FacultyListScreen(
    viewModel: StudentViewModel,
    onNavigateToRegister: () -> Unit,
    modifier: Modifier = Modifier
) {
    val facultyMap by viewModel.facultyGroupedStudents.collectAsState()
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

    // Track expanded state for each faculty
    val expandedStates = remember { mutableStateMapOf<String, Boolean>() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        if (facultyMap.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Domain,
                        contentDescription = null,
                        modifier = Modifier.size(72.dp),
                        tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No Faculties Available",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Register students to populate faculty records.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(onClick = onNavigateToRegister) {
                        Icon(imageVector = Icons.Default.PersonAdd, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Register Student")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = facultyMap.keys.toList(),
                    key = { facultyName -> facultyName }
                ) { facultyName ->
                    val studentsInFaculty = facultyMap[facultyName] ?: emptyList()
                    val isExpanded = expandedStates[facultyName] ?: true

                    FacultyCard(
                        facultyName = facultyName,
                        students = studentsInFaculty,
                        isExpanded = isExpanded,
                        onToggleExpand = {
                            expandedStates[facultyName] = !isExpanded
                        },
                        onStudentClick = { student -> viewModel.showStudentDetail(student) },
                        onEditStudent = { student -> viewModel.startEditStudent(student) },
                        onDeleteStudent = { student -> viewModel.requestDeleteStudent(student) }
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

@Composable
private fun FacultyCard(
    facultyName: String,
    students: List<Student>,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onStudentClick: (Student) -> Unit,
    onEditStudent: (Student) -> Unit,
    onDeleteStudent: (Student) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggleExpand() }
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.tertiaryContainer
                ) {
                    Icon(
                        imageVector = Icons.Default.Domain,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(8.dp)
                            .size(24.dp),
                        tint = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = facultyName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${students.size} student${if (students.size == 1) "" else "s"} registered",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }

                Badge(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    Text(
                        text = "${students.size}",
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandMore else Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.outline
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    HorizontalDivider(modifier = Modifier.padding(bottom = 8.dp))
                    students.forEach { student ->
                        StudentCard(
                            student = student,
                            onCardClick = { onStudentClick(student) },
                            onEditClick = { onEditStudent(student) },
                            onDeleteClick = { onDeleteStudent(student) }
                        )
                    }
                }
            }
        }
    }
}
