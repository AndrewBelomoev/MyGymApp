package com.bel.mygymapp.data.repository

import com.bel.mygymapp.data.local.dao.ExerciseDao
import com.bel.mygymapp.data.local.entity.ExerciseEntity
import com.bel.mygymapp.data.local.entity.MuscleGroup
import com.bel.mygymapp.domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow

class ExerciseRepositoryImpl(
    private val dao: ExerciseDao
) : ExerciseRepository {
    override fun getAllExercises(): Flow<List<ExerciseEntity>> = dao.getAllExercises()
    
    override fun getExercisesByMuscleGroup(muscleGroup: MuscleGroup): Flow<List<ExerciseEntity>> =
        dao.getExercisesByMuscleGroup(muscleGroup)

    override fun searchExercises(searchQuery: String): Flow<List<ExerciseEntity>> =
        dao.searchExercises(searchQuery)

    override suspend fun insertExercise(exercise: ExerciseEntity): Long = dao.insertExercise(exercise)

    override suspend fun insertExercises(exercises: List<ExerciseEntity>): List<Long> = dao.insertExercises(exercises)
}
