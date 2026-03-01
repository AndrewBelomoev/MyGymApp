package com.bel.mygymapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bel.mygymapp.data.local.entity.WorkoutEntity
import com.bel.mygymapp.data.local.entity.WorkoutWithDetails
import com.bel.mygymapp.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date

class HomeViewModel(
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    val workouts: StateFlow<List<WorkoutWithDetails>> = workoutRepository.getWorkoutsWithDetails(isArchived = false)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun createNewWorkout() {
        viewModelScope.launch {
            val workout = WorkoutEntity(date = Date().time)
            workoutRepository.insertWorkout(workout)
        }
    }

    fun archiveWorkout(workoutId: Long) {
        viewModelScope.launch {
            workoutRepository.updateArchiveStatus(workoutId, isArchived = true)
        }
    }
}
