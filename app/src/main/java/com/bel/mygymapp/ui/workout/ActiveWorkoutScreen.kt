package com.bel.mygymapp.ui.workout

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Clear
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import com.bel.mygymapp.data.local.entity.MuscleGroup
import com.bel.mygymapp.data.local.entity.SetLogEntity
import com.bel.mygymapp.data.local.entity.WorkoutExerciseWithSets
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveWorkoutScreen(
    workoutId: Long,
    onNavigateBack: () -> Unit,
    onNavigateToExerciseSelection: (Long) -> Unit,
    viewModel: ActiveWorkoutViewModel = koinViewModel()
) {
    val workoutDetails by viewModel.workoutDetails.collectAsState()
    
    val dateFormat = SimpleDateFormat("dd.MM.yyyy - HH:mm - EEEE", Locale.getDefault())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    val dateStr = workoutDetails?.workout?.date?.let { dateFormat.format(Date(it)) } ?: ""
                    Text(dateStr) 
                },
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
            workoutDetails?.let { details ->
                // Группировка упражнений по MuscleGroup
                val groupedExercises = details.workoutExercises.groupBy { it.exercise.muscleGroup }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    groupedExercises.forEach { (muscleGroup, exercisesList) ->
                        item {
                            Text(
                                text = muscleGroup.displayName,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                        
                        items(exercisesList) { exerciseWithSets ->
                            ExerciseItemWithSets(
                                exerciseWithSets = exerciseWithSets,
                                onAddSet = { weight, reps ->
                                    val nextSetNumber = exerciseWithSets.sets.size + 1
                                    viewModel.addSet(
                                        workoutExerciseId = exerciseWithSets.workoutExercise.id,
                                        setNumber = nextSetNumber,
                                        weight = weight,
                                        reps = reps
                                    )
                                },
                                onEditSet = { setId, weight, reps ->
                                    viewModel.updateSet(setId, weight, reps)
                                },
                                onDeleteSet = { setId ->
                                    viewModel.deleteSet(setId)
                                },
                                onDeleteExercise = {
                                    viewModel.deleteWorkoutExercise(exerciseWithSets.workoutExercise.id)
                                }
                            )
                        }
                    }
                }

                Button(
                    onClick = { onNavigateToExerciseSelection(workoutId) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Добавить упражнение")
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExerciseItemWithSets(
    exerciseWithSets: WorkoutExerciseWithSets,
    onAddSet: (Float, Int) -> Unit,
    onEditSet: (Long, Float, Int) -> Unit,
    onDeleteSet: (Long) -> Unit,
    onDeleteExercise: () -> Unit
) {
    var weightInput by remember { mutableStateOf("") }
    var repsInput by remember { mutableStateOf("") }
    
    var setLogToEdit by remember { mutableStateOf<SetLogEntity?>(null) }
    var editWeightInput by remember { mutableStateOf("") }
    var editRepsInput by remember { mutableStateOf("") }
    
    var showInputs by remember { mutableStateOf(false) }
    var showDeleteExerciseDialog by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(16.dp))
            .combinedClickable(
                onClick = { showInputs = !showInputs }
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = exerciseWithSets.exercise.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .combinedClickable(
                            onClick = { },
                            onLongClick = { showDeleteExerciseDialog = true }
                        )
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear, 
                        contentDescription = "Удалить упражнение", 
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))

            // Заголовки таблицы подходов
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Сет", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                Text("Вес (кг)", modifier = Modifier.weight(2f), fontWeight = FontWeight.Bold)
                Text("Повт.", modifier = Modifier.weight(2f), fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(96.dp)) // Место под кнопки
            }

            // Список подходов
            exerciseWithSets.sets.forEach { setLog ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(setLog.setNumber.toString(), modifier = Modifier.weight(1f))
                    Text(setLog.weight.toString(), modifier = Modifier.weight(2f))
                    Text(setLog.reps.toString(), modifier = Modifier.weight(2f))
                    Row(
                        modifier = Modifier.width(96.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(onClick = { 
                            setLogToEdit = setLog
                            editWeightInput = setLog.weight.toString()
                            editRepsInput = setLog.reps.toString()
                        }) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = "Редактировать", tint = MaterialTheme.colorScheme.primary)
                        }
                        IconButton(onClick = { onDeleteSet(setLog.id) }) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Удалить сет", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Ввод нового подхода
            if (showInputs) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = weightInput,
                        onValueChange = { weightInput = it },
                        label = { Text("Вес") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp),
                        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                        )
                    )
                    OutlinedTextField(
                        value = repsInput,
                        onValueChange = { repsInput = it },
                        label = { Text("Повт.") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp),
                        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                        )
                    )
                    Button(
                        onClick = {
                            val weight = weightInput.toFloatOrNull()
                            val reps = repsInput.toIntOrNull()
                            if (weight != null && reps != null) {
                                onAddSet(weight, reps)
                                weightInput = ""
                                repsInput = ""
                                showInputs = false // Hide after saving
                            }
                        }
                    ) {
                        Text("OK")
                    }
                }
            }
    }

    if (setLogToEdit != null) {
        val currentSet = setLogToEdit!!
        AlertDialog(
            onDismissRequest = { setLogToEdit = null },
            title = { Text("Редактировать подход ${currentSet.setNumber}") },
            text = {
                Column {
                    OutlinedTextField(
                        value = editWeightInput,
                        onValueChange = { editWeightInput = it },
                        label = { Text("Вес (кг)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = editRepsInput,
                        onValueChange = { editRepsInput = it },
                        label = { Text("Повторения") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val newWeight = editWeightInput.toFloatOrNull() ?: currentSet.weight
                        val newReps = editRepsInput.toIntOrNull() ?: currentSet.reps
                        onEditSet(currentSet.id, newWeight, newReps)
                        setLogToEdit = null
                    }
                ) {
                    Text("Сохранить")
                }
            },
            dismissButton = {
                TextButton(onClick = { setLogToEdit = null }) {
                    Text("Отмена")
                }
            }
        )
    }

    if (showDeleteExerciseDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteExerciseDialog = false },
            title = { Text("Удалить упражнение") },
            text = { Text("Вы действительно хотите удалить упражнение '${exerciseWithSets.exercise.name}' и все его подходы?") },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteExercise()
                        showDeleteExerciseDialog = false
                    }
                ) {
                    Text("Удалить")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteExerciseDialog = false }) {
                    Text("Отмена")
                }
            }
        )
    }
    }
}
