package com.bel.mygymapp.ui.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.foundation.layout.Row
import com.bel.mygymapp.data.local.entity.MuscleGroup
import com.bel.mygymapp.domain.preferences.AppTheme
import com.bel.mygymapp.ui.exercise.ExerciseListItem
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: SettingsViewModel = koinViewModel()
) {
    val allExercises by viewModel.allExercises.collectAsState()

    var newExerciseName by remember { mutableStateOf("") }
    var selectedMuscleGroup by remember { mutableStateOf(MuscleGroup.CHEST) }
    var expanded by remember { mutableStateOf(false) }
    
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("База упражнений", "Прочее")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Настройки БД") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TabRow(selectedTabIndex = selectedTabIndex) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }

            if (selectedTabIndex == 0) {
                Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    Text(text = "Добавить новое упражнение", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    OutlinedTextField(
                        value = newExerciseName,
                        onValueChange = { newExerciseName = it },
                        label = { Text("Название упражнения") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = selectedMuscleGroup.displayName,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Группа мышц") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            MuscleGroup.values().forEach { group ->
                                DropdownMenuItem(
                                    text = { Text(group.displayName) },
                                    onClick = {
                                        selectedMuscleGroup = group
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            viewModel.addExercise(newExerciseName, selectedMuscleGroup)
                            newExerciseName = ""
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Добавить упражнение")
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Text(text = "База упражнений (${allExercises.size})", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)

                    Spacer(modifier = Modifier.height(8.dp))

                    if (allExercises.isEmpty()) {
                        Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                            Text("База упражнений пуста")
                        }
                    } else {
                        LazyColumn(modifier = Modifier.weight(1f)) {
                            items(allExercises) { exercise ->
                                ExerciseListItem(exercise = exercise, onClick = {})
                            }
                        }
                    }
                }
            } else {
                val currentTheme by viewModel.currentTheme.collectAsState()
                
                Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    Text(
                        text = "Оформление", 
                        style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    Column(Modifier.selectableGroup()) {
                        ThemeOptionRow(
                            themeName = "Системная",
                            selected = currentTheme == AppTheme.SYSTEM,
                            onClick = { viewModel.setTheme(AppTheme.SYSTEM) }
                        )
                        ThemeOptionRow(
                            themeName = "Светлая",
                            selected = currentTheme == AppTheme.LIGHT,
                            onClick = { viewModel.setTheme(AppTheme.LIGHT) }
                        )
                        ThemeOptionRow(
                            themeName = "Темная",
                            selected = currentTheme == AppTheme.DARK,
                            onClick = { viewModel.setTheme(AppTheme.DARK) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ThemeOptionRow(themeName: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = themeName, style = androidx.compose.material3.MaterialTheme.typography.bodyLarge)
    }
}
