package com.example.mis.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Domain
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mis.ui.AppScreen
import com.example.mis.ui.StudentViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentManagementApp(
    viewModel: StudentViewModel = viewModel()
) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val students by viewModel.students.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.snackbarMessage.collectLatest { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    // Handle back button on Android to return to Home screen if in sub-screen
    BackHandler(enabled = currentScreen != AppScreen.HOME_MENU) {
        viewModel.navigateBack()
    }

    val screenTitle = when (currentScreen) {
        AppScreen.HOME_MENU -> "Student Management System"
        AppScreen.STUDENT_LIST -> "Registered Students Information"
        AppScreen.FACULTY_LIST -> "Faculty Directory"
        AppScreen.REGISTER_STUDENT -> "Register New Student"
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = screenTitle,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    if (currentScreen == AppScreen.HOME_MENU) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = null,
                            modifier = Modifier.padding(start = 12.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        IconButton(onClick = { viewModel.navigateBack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back to Home",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentScreen == AppScreen.HOME_MENU,
                    onClick = { viewModel.navigateTo(AppScreen.HOME_MENU) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Home"
                        )
                    },
                    label = { Text("Home") }
                )

                NavigationBarItem(
                    selected = currentScreen == AppScreen.STUDENT_LIST,
                    onClick = { viewModel.navigateTo(AppScreen.STUDENT_LIST) },
                    icon = {
                        BadgedBox(
                            badge = {
                                if (students.isNotEmpty()) {
                                    Badge {
                                        Text("${students.size}")
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Group,
                                contentDescription = "Students"
                            )
                        }
                    },
                    label = { Text("Students") }
                )

                NavigationBarItem(
                    selected = currentScreen == AppScreen.FACULTY_LIST,
                    onClick = { viewModel.navigateTo(AppScreen.FACULTY_LIST) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Domain,
                            contentDescription = "Faculties"
                        )
                    },
                    label = { Text("Faculties") }
                )

                NavigationBarItem(
                    selected = currentScreen == AppScreen.REGISTER_STUDENT,
                    onClick = { viewModel.navigateTo(AppScreen.REGISTER_STUDENT) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.PersonAdd,
                            contentDescription = "Register"
                        )
                    },
                    label = { Text("Register") }
                )
            }
        },
        floatingActionButton = {
            if (currentScreen == AppScreen.STUDENT_LIST || currentScreen == AppScreen.FACULTY_LIST) {
                FloatingActionButton(
                    onClick = { viewModel.navigateTo(AppScreen.REGISTER_STUDENT) },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    Icon(
                        imageVector = Icons.Default.PersonAdd,
                        contentDescription = "Add Student"
                    )
                }
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                AppScreen.HOME_MENU -> HomeScreen(
                    onNavigateToStudentList = { viewModel.navigateTo(AppScreen.STUDENT_LIST) },
                    onNavigateToFacultyList = { viewModel.navigateTo(AppScreen.FACULTY_LIST) },
                    onNavigateToRegister = { viewModel.navigateTo(AppScreen.REGISTER_STUDENT) }
                )
                AppScreen.STUDENT_LIST -> StudentListScreen(
                    viewModel = viewModel,
                    onNavigateToRegister = { viewModel.navigateTo(AppScreen.REGISTER_STUDENT) }
                )
                AppScreen.FACULTY_LIST -> FacultyListScreen(
                    viewModel = viewModel,
                    onNavigateToRegister = { viewModel.navigateTo(AppScreen.REGISTER_STUDENT) }
                )
                AppScreen.REGISTER_STUDENT -> RegisterStudentScreen(
                    viewModel = viewModel
                )
            }
        }
    }
}
