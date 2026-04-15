package com.example.todolist.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.todolist.model.Task
import com.example.todolist.ui.components.AddTaskDialog
import com.example.todolist.viewmodel.TaskViewModel

@Composable
fun TaskItem(task: Task, onToggle: () -> Unit, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = task.isDone, onCheckedChange = { onToggle() })
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
                .clickable { onClick() }
        ) {
            Text(text = task.name, style = MaterialTheme.typography.bodyLarge)
            Text(
                text = "Début : ${task.dateDebut} - Fin : ${task.dateFin}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(navController: NavController, viewModel: TaskViewModel) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    val tasks by viewModel.tasks.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Liste des Tâches") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Text("+")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            LazyColumn {
                items(tasks) { task ->
                    TaskItem(
                        task = task,
                        onToggle = {
                            viewModel.updateTask(task.copy(isDone = !task.isDone))
                        },
                        onClick = {
                            navController.navigate("task_detail/${task.id}")
                        }
                    )
                }
            }
        }
    }

    if (showDialog) {
        AddTaskDialog(
            context = context,
            onDismiss = { showDialog = false },
            onConfirm = { name, dateDebut, dateFin ->
                if (name.isNotBlank()) {
                    viewModel.addTask(name, dateDebut, dateFin)
                }
                showDialog = false
            }
        )
    }
}
