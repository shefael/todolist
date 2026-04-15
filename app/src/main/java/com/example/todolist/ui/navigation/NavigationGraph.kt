package com.example.todolist.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.todolist.TodoApplication
import com.example.todolist.ui.TaskDetailScreen
import com.example.todolist.ui.TaskScreen
import com.example.todolist.viewmodel.TaskViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationGraph() {
    val navController = rememberNavController()
    val context = LocalContext.current.applicationContext as TodoApplication
    val repository = context.repository
    val factory = TaskViewModel.Factory(repository)
    val viewModel: TaskViewModel = viewModel(factory = factory)

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            TaskScreen(navController, viewModel)
        }
        composable(
            route = "task_detail/{taskId}",
            arguments = listOf(navArgument("taskId") { type = NavType.IntType })
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getInt("taskId")
            taskId?.let { TaskDetailScreen(navController, it, viewModel) }
        }
    }
}
