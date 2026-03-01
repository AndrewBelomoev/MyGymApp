package com.bel.mygymapp.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home_screen")
    object ActiveWorkout : Screen("active_workout_screen/{workoutId}") {
        fun createRoute(workoutId: Long) = "active_workout_screen/$workoutId"
    }
    object ExerciseSelection : Screen("exercise_selection_screen/{workoutId}") {
        fun createRoute(workoutId: Long) = "exercise_selection_screen/$workoutId"
    }
    object Settings : Screen("settings_screen")
    object Archive : Screen("archive_screen")
}
