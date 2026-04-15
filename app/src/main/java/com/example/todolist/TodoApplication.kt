package com.example.todolist

import android.app.Application
import com.example.todolist.data.TaskDatabase
import com.example.todolist.data.repository.TaskRepository

class TodoApplication : Application() {
    private val database by lazy { TaskDatabase.getDatabase(this) }
    val repository by lazy { TaskRepository(database.taskDao()) }
}
