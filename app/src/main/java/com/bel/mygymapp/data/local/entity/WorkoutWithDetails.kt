package com.bel.mygymapp.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Объединяет WorkoutExerciseEntity с ExerciseEntity и списком SetLogEntity.
 * Представляет одно упражнение внутри конкретной тренировки и все его подходы.
 */
data class WorkoutExerciseWithSets(
    @Embedded val workoutExercise: WorkoutExerciseEntity,
    
    @Relation(
        parentColumn = "exerciseId",
        entityColumn = "id"
    )
    val exercise: ExerciseEntity,
    
    @Relation(
        parentColumn = "id",
        entityColumn = "workoutExerciseId"
    )
    val sets: List<SetLogEntity>
)

/**
 * Полная информация о тренировке (Workout) со всеми привязанными упражнениями и их подходами.
 */
data class WorkoutWithDetails(
    @Embedded val workout: WorkoutEntity,
    
    @Relation(
        entity = WorkoutExerciseEntity::class,
        parentColumn = "id",
        entityColumn = "workoutId"
    )
    val workoutExercises: List<WorkoutExerciseWithSets>
)
