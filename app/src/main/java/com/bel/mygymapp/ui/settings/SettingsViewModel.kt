package com.bel.mygymapp.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bel.mygymapp.data.local.entity.ExerciseEntity
import com.bel.mygymapp.data.local.entity.MuscleGroup
import com.bel.mygymapp.domain.preferences.AppTheme
import com.bel.mygymapp.domain.preferences.ThemePreferences
import com.bel.mygymapp.domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val exerciseRepository: ExerciseRepository,
    private val themePreferences: ThemePreferences
) : ViewModel() {

    val currentTheme: StateFlow<AppTheme> = themePreferences.themeFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppTheme.SYSTEM
        )

    val allExercises: StateFlow<List<ExerciseEntity>> = exerciseRepository.getAllExercises()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addExercise(name: String, muscleGroup: MuscleGroup) {
        if (name.isBlank()) return
        
        viewModelScope.launch {
            val exercise = ExerciseEntity(
                name = name.trim(),
                muscleGroup = muscleGroup
            )
            exerciseRepository.insertExercise(exercise)
        }
    }

    fun setTheme(theme: AppTheme) {
        viewModelScope.launch {
            themePreferences.setTheme(theme)
        }
    }
}
