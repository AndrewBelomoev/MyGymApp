package com.bel.mygymapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bel.mygymapp.ui.archive.ArchiveScreen
import com.bel.mygymapp.ui.exercise.ExerciseSelectionScreen
import com.bel.mygymapp.ui.home.HomeScreen
import com.bel.mygymapp.ui.settings.SettingsScreen
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import com.bel.mygymapp.ui.workout.ActiveWorkoutScreen
import com.bel.mygymapp.ui.workout.ActiveWorkoutViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToActiveWorkout = { workoutId ->
                    navController.navigate(Screen.ActiveWorkout.createRoute(workoutId))
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                },
                onNavigateToArchive = {
                    navController.navigate(Screen.Archive.route)
                }
            )
        }

        composable(
            route = Screen.ActiveWorkout.route,
            arguments = listOf(navArgument("workoutId") { type = NavType.LongType })
        ) { backStackEntry ->
            val workoutId = backStackEntry.arguments?.getLong("workoutId") ?: return@composable
            
            // Получение ViewModel с передачей SavedStateHandle/Id через Koin
            val activeWorkoutViewModel: ActiveWorkoutViewModel = koinViewModel { parametersOf(workoutId) }

            ActiveWorkoutScreen(
                workoutId = workoutId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToExerciseSelection = { wId ->
                    navController.navigate(Screen.ExerciseSelection.createRoute(wId))
                },
                viewModel = activeWorkoutViewModel
            )
        }

        composable(
            route = Screen.ExerciseSelection.route,
            arguments = listOf(navArgument("workoutId") { type = NavType.LongType })
        ) { backStackEntry ->
            val workoutId = backStackEntry.arguments?.getLong("workoutId") ?: return@composable
            
            val activeWorkoutViewModel: ActiveWorkoutViewModel = koinViewModel(
                viewModelStoreOwner = navController.getBackStackEntry(Screen.ActiveWorkout.createRoute(workoutId))
            )

            ExerciseSelectionScreen(
                onExerciseSelected = { exerciseId ->
                    activeWorkoutViewModel.addExerciseToWorkout(exerciseId)
                    navController.popBackStack()
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Archive.route) {
            ArchiveScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
