package com.bel.mygymapp.data.repository

import com.bel.mygymapp.data.local.dao.WorkoutDao
import com.bel.mygymapp.data.local.entity.SetLogEntity
import com.bel.mygymapp.data.local.entity.WorkoutEntity
import com.bel.mygymapp.data.local.entity.WorkoutExerciseEntity
import com.bel.mygymapp.data.local.entity.WorkoutWithDetails
import com.bel.mygymapp.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow

class WorkoutRepositoryImpl(
    private val dao: WorkoutDao
) : WorkoutRepository {
    override fun getWorkouts(isArchived: Boolean): Flow<List<WorkoutEntity>> = dao.getWorkouts(isArchived)

    override fun getWorkoutWithDetails(workoutId: Long): Flow<WorkoutWithDetails> =
        dao.getWorkoutWithDetails(workoutId)

    override fun getWorkoutsWithDetails(isArchived: Boolean): Flow<List<WorkoutWithDetails>> =
        dao.getWorkoutsWithDetails(isArchived)

    override suspend fun insertWorkout(workout: WorkoutEntity): Long = dao.insertWorkout(workout)

    override suspend fun insertWorkoutExercise(workoutExercise: WorkoutExerciseEntity): Long = dao.insertWorkoutExercise(workoutExercise)

    override suspend fun insertSetLog(setLog: SetLogEntity): Long = dao.insertSetLog(setLog)

    override suspend fun insertSetLogs(setLogs: List<SetLogEntity>): List<Long> = dao.insertSetLogs(setLogs)

    override suspend fun updateSet(setId: Long, weight: Float, reps: Int): Int {
        return dao.updateSet(setId, weight, reps)
    }

    override suspend fun deleteSet(setId: Long): Int {
        return dao.deleteSet(setId)
    }

    override suspend fun deleteWorkoutExercise(workoutExerciseId: Long) {
        dao.deleteSetLogsForExercise(workoutExerciseId)
        dao.deleteWorkoutExerciseById(workoutExerciseId)
    }

    override suspend fun updateArchiveStatus(workoutId: Long, isArchived: Boolean): Int {
        return dao.updateArchiveStatus(workoutId, isArchived)
    }

    override suspend fun deleteWorkoutPermanently(workoutId: Long) {
        dao.deleteSetLogsForWorkout(workoutId)
        dao.deleteWorkoutExercisesForWorkout(workoutId)
        dao.deleteWorkout(workoutId)
    }
}
