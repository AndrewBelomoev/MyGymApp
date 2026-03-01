package com.bel.mygymapp.domain.repository

import com.bel.mygymapp.data.local.entity.SetLogEntity
import com.bel.mygymapp.data.local.entity.WorkoutEntity
import com.bel.mygymapp.data.local.entity.WorkoutExerciseEntity
import com.bel.mygymapp.data.local.entity.WorkoutWithDetails
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
    fun getWorkouts(isArchived: Boolean = false): Flow<List<WorkoutEntity>>
    fun getWorkoutWithDetails(workoutId: Long): Flow<WorkoutWithDetails>
    fun getWorkoutsWithDetails(isArchived: Boolean = false): Flow<List<WorkoutWithDetails>>
    suspend fun insertWorkout(workout: WorkoutEntity): Long
    suspend fun insertWorkoutExercise(workoutExercise: WorkoutExerciseEntity): Long
    suspend fun insertSetLog(setLog: SetLogEntity): Long
    suspend fun insertSetLogs(setLogs: List<SetLogEntity>): List<Long>

    suspend fun updateSet(setId: Long, weight: Float, reps: Int): Int
    suspend fun deleteSet(setId: Long): Int
    suspend fun deleteWorkoutExercise(workoutExerciseId: Long)

    suspend fun updateArchiveStatus(workoutId: Long, isArchived: Boolean): Int
    suspend fun deleteWorkoutPermanently(workoutId: Long)
}
