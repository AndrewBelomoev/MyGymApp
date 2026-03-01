package com.bel.mygymapp.ui.archive

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bel.mygymapp.data.local.entity.WorkoutWithDetails
import com.bel.mygymapp.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ArchiveViewModel(
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    val archivedWorkouts: StateFlow<List<WorkoutWithDetails>> = workoutRepository.getWorkoutsWithDetails(isArchived = true)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun restoreWorkout(workoutId: Long) {
        viewModelScope.launch {
            workoutRepository.updateArchiveStatus(workoutId, isArchived = false)
        }
    }

    fun deleteWorkoutPermanently(workoutId: Long) {
        viewModelScope.launch {
            workoutRepository.deleteWorkoutPermanently(workoutId)
        }
    }
}
