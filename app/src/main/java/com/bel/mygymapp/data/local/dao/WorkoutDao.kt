package com.bel.mygymapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.bel.mygymapp.data.local.entity.SetLogEntity
import com.bel.mygymapp.data.local.entity.WorkoutEntity
import com.bel.mygymapp.data.local.entity.WorkoutExerciseEntity
import com.bel.mygymapp.data.local.entity.WorkoutWithDetails
import kotlinx.coroutines.flow.Flow

@Dao
@JvmSuppressWildcards
interface WorkoutDao {
    
    // Получение тренировок без детальной информации
    @Query("SELECT * FROM workouts WHERE isArchived = :isArchived ORDER BY date DESC")
    fun getWorkouts(isArchived: Boolean = false): Flow<List<WorkoutEntity>>

    // Получение конкретной тренировки со всеми связанными упражнениями и подходами
    @Transaction
    @Query("SELECT * FROM workouts WHERE id = :workoutId")
    fun getWorkoutWithDetails(workoutId: Long): Flow<WorkoutWithDetails>

    // Аналогично, но для списка с фильтрацией по статусу архива
    @Transaction
    @Query("SELECT * FROM workouts WHERE isArchived = :isArchived ORDER BY date DESC")
    fun getWorkoutsWithDetails(isArchived: Boolean = false): Flow<List<WorkoutWithDetails>>

    // Обновление статуса архивации
    @Query("UPDATE workouts SET isArchived = :isArchived WHERE id = :workoutId")
    suspend fun updateArchiveStatus(workoutId: Long, isArchived: Boolean): Int

    // Жесткое удаление
    @Query("DELETE FROM set_logs WHERE workoutExerciseId IN (SELECT id FROM workout_exercises WHERE workoutId = :workoutId)")
    suspend fun deleteSetLogsForWorkout(workoutId: Long): Int

    @Query("DELETE FROM workout_exercises WHERE workoutId = :workoutId")
    suspend fun deleteWorkoutExercisesForWorkout(workoutId: Long): Int

    @Query("DELETE FROM workouts WHERE id = :workoutId")
    suspend fun deleteWorkout(workoutId: Long): Int

    @Query("UPDATE set_logs SET weight = :weight, reps = :reps WHERE id = :setId")
    suspend fun updateSet(setId: Long, weight: Float, reps: Int): Int

    @Query("DELETE FROM set_logs WHERE id = :setId")
    suspend fun deleteSet(setId: Long): Int

    @Query("DELETE FROM set_logs WHERE workoutExerciseId = :workoutExerciseId")
    suspend fun deleteSetLogsForExercise(workoutExerciseId: Long): Int

    @Query("DELETE FROM workout_exercises WHERE id = :workoutExerciseId")
    suspend fun deleteWorkoutExerciseById(workoutExerciseId: Long): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: WorkoutEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutExercise(workoutExercise: WorkoutExerciseEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSetLog(setLog: SetLogEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSetLogs(setLogs: List<SetLogEntity>): List<Long>
}
