package com.example.mis.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Domain
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mis.ui.StudentViewModel

import androidx.compose.material3.ExposedDropdownMenuAnchorType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterStudentScreen(
    viewModel: StudentViewModel,
    modifier: Modifier = Modifier
) {
    val nameInput by viewModel.nameInput.collectAsState()
    val matricInput by viewModel.matricInput.collectAsState()
    val courseInput by viewModel.courseInput.collectAsState()
    val facultyInput by viewModel.facultyInput.collectAsState()

    val nameError by viewModel.nameError.collectAsState()
    val matricError by viewModel.matricError.collectAsState()
    val courseError by viewModel.courseError.collectAsState()
    val facultyError by viewModel.facultyError.collectAsState()

    // Sample dropdown options for Course and Faculty for user convenience
    val courseOptions = listOf(
        "Software Engineering",
        "Computer Science",
        "Information Technology",
        "Data Science",
        "Cyber Security",
        "Artificial Intelligence",
        "Electrical Engineering",
        "Mechanical Engineering"
    )

    val facultyOptions = listOf(
        "Faculty of Computing",
        "Faculty of Electrical Engineering",
        "Faculty of Mechanical Engineering",
        "Faculty of Science",
        "Faculty of Business & Management"
    )

    var courseExpanded by remember { mutableStateOf(false) }
    var facultyExpanded by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.PersonAdd,
                    contentDescription = null,
                    modifier = Modifier.size(36.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Register New Student",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "Enter the student details below to add them to the system.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                }
            }
        }

        // Form Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 1. Full Name Field
                OutlinedTextField(
                    value = nameInput,
                    onValueChange = { viewModel.onNameChange(it) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Full Name *") },
                    placeholder = { Text("e.g. Ahmad Bin Zulkifli") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null
                        )
                    },
                    isError = nameError != null,
                    supportingText = nameError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors()
                )

                // 2. Matric No Field
                OutlinedTextField(
                    value = matricInput,
                    onValueChange = { viewModel.onMatricChange(it) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Matric Number *") },
                    placeholder = { Text("e.g. AI230045") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Badge,
                            contentDescription = null
                        )
                    },
                    isError = matricError != null,
                    supportingText = matricError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                    singleLine = true
                )

                // 3. Course Field (with Dropdown suggestions)
                ExposedDropdownMenuBox(
                    expanded = courseExpanded,
                    onExpandedChange = { courseExpanded = !courseExpanded }
                ) {
                    OutlinedTextField(
                        value = courseInput,
                        onValueChange = {
                            viewModel.onCourseChange(it)
                            courseExpanded = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable, true),
                        label = { Text("Course *") },
                        placeholder = { Text("e.g. Software Engineering") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Book,
                                contentDescription = null
                            )
                        },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = courseExpanded) },
                        isError = courseError != null,
                        supportingText = courseError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                        singleLine = true
                    )

                    ExposedDropdownMenu(
                        expanded = courseExpanded,
                        onDismissRequest = { courseExpanded = false }
                    ) {
                        courseOptions.filter { it.contains(courseInput, ignoreCase = true) }.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    viewModel.onCourseChange(option)
                                    courseExpanded = false
                                }
                            )
                        }
                    }
                }

                // 4. Faculty Field (with Dropdown suggestions)
                ExposedDropdownMenuBox(
                    expanded = facultyExpanded,
                    onExpandedChange = { facultyExpanded = !facultyExpanded }
                ) {
                    OutlinedTextField(
                        value = facultyInput,
                        onValueChange = {
                            viewModel.onFacultyChange(it)
                            facultyExpanded = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable, true),
                        label = { Text("Faculty *") },
                        placeholder = { Text("e.g. Faculty of Computing") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Domain,
                                contentDescription = null
                            )
                        },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = facultyExpanded) },
                        isError = facultyError != null,
                        supportingText = facultyError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                        singleLine = true
                    )

                    ExposedDropdownMenu(
                        expanded = facultyExpanded,
                        onDismissRequest = { facultyExpanded = false }
                    ) {
                        facultyOptions.filter { it.contains(facultyInput, ignoreCase = true) }.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    viewModel.onFacultyChange(option)
                                    facultyExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Action Buttons
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { viewModel.registerStudent() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PersonAdd,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Register Student",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    OutlinedButton(
                        onClick = { viewModel.clearForm() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Clear Form")
                    }
                }
            }
        }
    }
}
