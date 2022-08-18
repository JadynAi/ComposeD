package com.xixi.composed

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 *Jairett since 2022/7/22
 */

@Composable
fun TestState() {
    val state by remember { mutableStateOf(1) }
    stateScreen(modifier = Modifier)
}

@Composable
private fun stateScreen(modifier: Modifier, viewModel: StateViewModel = viewModel()) {
    Column(modifier = modifier) {
        var count by rememberSaveable { mutableStateOf(0) }
        Column(modifier = Modifier.padding(16.dp)) {
            if (count > 0) {
                Text(text = "had $count glasses")
            }
            Button(
                onClick = { count++ }, enabled = count < 10,
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "Add one")
            }
        }
        val list = arrayListOf<TaskData>()
        for (i in 0..6) {
            list.add(TaskData(i, "$i label"))
        }
        taskList(list = list, { i, b -> }, {})
    }
}

@Composable
private fun taskList(
    list: List<TaskData>,
    onCheckedTask: (TaskData, Boolean) -> Unit,
    onCloseTask: (TaskData) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = Modifier) {
        items(items = list, key = { task -> task.id }) { task ->
            TaskItem(task.label, task.checked, { checked -> onCheckedTask(task, checked) }, { onCloseTask(task) }, modifier)
        }
    }
}

@Composable
fun TaskItem(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit, onClose: () -> Unit, modifier: Modifier = Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(modifier = Modifier
            .weight(1f)
            .padding(start = 16.dp), text = label)
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
        IconButton(onClick = onClose) {
            Icon(Icons.Filled.Close, contentDescription = "Close")
        }
    }
}

class StateViewModel : ViewModel() {

}

class TaskData(val id: Int, val label: String, initialChecked: Boolean = false
) {
    var checked: Boolean by mutableStateOf(initialChecked)
}
