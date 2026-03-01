package com.bel.mygymapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workouts")
data class WorkoutEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: Long, // Unix timestamp in milliseconds
    val notes: String = "",
    val isArchived: Boolean = false
)
