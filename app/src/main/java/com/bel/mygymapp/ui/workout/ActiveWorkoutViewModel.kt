package com.bel.mygymapp.ui.workout

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bel.mygymapp.data.local.entity.SetLogEntity
import com.bel.mygymapp.data.local.entity.WorkoutExerciseEntity
import com.bel.mygymapp.data.local.entity.WorkoutWithDetails
import com.bel.mygymapp.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ActiveWorkoutViewModel(
    savedStateHandle: SavedStateHandle,
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    private val workoutId: Long = checkNotNull(savedStateHandle["workoutId"])

    val workoutDetails: StateFlow<WorkoutWithDetails?> = workoutRepository.getWorkoutWithDetails(workoutId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun addExerciseToWorkout(exerciseId: Long) {
        viewModelScope.launch {
            val workoutExercise = WorkoutExerciseEntity(
                workoutId = workoutId,
                exerciseId = exerciseId
            )
            workoutRepository.insertWorkoutExercise(workoutExercise)
        }
    }

    fun addSet(workoutExerciseId: Long, setNumber: Int, weight: Float, reps: Int) {
        viewModelScope.launch {
            val setLog = SetLogEntity(
                workoutExerciseId = workoutExerciseId,
                setNumber = setNumber,
                weight = weight,
                reps = reps
            )
            workoutRepository.insertSetLog(setLog)
        }
    }

    fun updateSet(setId: Long, weight: Float, reps: Int) {
        viewModelScope.launch {
            workoutRepository.updateSet(setId, weight, reps)
        }
    }

    fun deleteSet(setId: Long) {
        viewModelScope.launch {
            workoutRepository.deleteSet(setId)
        }
    }

    fun deleteWorkoutExercise(workoutExerciseId: Long) {
        viewModelScope.launch {
            workoutRepository.deleteWorkoutExercise(workoutExerciseId)
        }
    }
}
