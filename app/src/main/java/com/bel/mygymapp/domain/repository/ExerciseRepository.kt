package com.bel.mygymapp.domain.repository

import com.bel.mygymapp.data.local.entity.ExerciseEntity
import com.bel.mygymapp.data.local.entity.MuscleGroup
import kotlinx.coroutines.flow.Flow

interface ExerciseRepository {
    fun getAllExercises(): Flow<List<ExerciseEntity>>
    fun getExercisesByMuscleGroup(muscleGroup: MuscleGroup): Flow<List<ExerciseEntity>>
    fun searchExercises(searchQuery: String): Flow<List<ExerciseEntity>>
    suspend fun insertExercise(exercise: ExerciseEntity): Long
    suspend fun insertExercises(exercises: List<ExerciseEntity>): List<Long>
}
