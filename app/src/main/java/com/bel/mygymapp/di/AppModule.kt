package com.bel.mygymapp.di

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.bel.mygymapp.ui.archive.ArchiveViewModel
import com.bel.mygymapp.ui.exercise.ExerciseSelectionViewModel
import com.bel.mygymapp.ui.home.HomeViewModel
import com.bel.mygymapp.ui.settings.SettingsViewModel
import com.bel.mygymapp.ui.workout.ActiveWorkoutViewModel
import com.bel.mygymapp.data.local.AppDatabase
import com.bel.mygymapp.data.repository.ExerciseRepositoryImpl
import com.bel.mygymapp.data.repository.WorkoutRepositoryImpl
import com.bel.mygymapp.domain.preferences.ThemePreferences
import com.bel.mygymapp.domain.repository.ExerciseRepository
import com.bel.mygymapp.domain.repository.WorkoutRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    
    // Database
    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java,
            "my_gym_app_db"
        )
        .addMigrations(AppDatabase.MIGRATION_1_2)
        .addCallback(object : RoomDatabase.Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                val cursor = db.query("SELECT COUNT(*) FROM exercises")
                var isEmpty = true
                if (cursor.moveToFirst()) {
                    isEmpty = cursor.getInt(0) == 0
                }
                cursor.close()

                if (isEmpty) {
                    val exercises = listOf(
                        "Жим штанги лежа на горизонтальной скамье" to "CHEST",
                        "Жим гантелей лежа на горизонтальной скамье" to "CHEST",
                        "Жим штанги/гантелей на наклонной скамье (вверх или вниз головой)" to "CHEST",
                        "Отжимания на брусьях (широким хватом, с акцентом на грудь)" to "CHEST",
                        "Классические отжимания от пола" to "CHEST",
                        "Разведение гантелей лежа" to "CHEST",
                        "Сведение рук в тренажере \"Бабочка\" (Пек-дек)" to "CHEST",
                        "Сведение рук в кроссовере" to "CHEST",
                        "Кроссовер снизу вверх (акцент на верх груди)" to "CHEST",
                        
                        "Становая тяга (классическая)" to "BACK",
                        "Подтягивания на перекладине (широким и средним хватом)" to "BACK",
                        "Тяга штанги в наклоне к поясу" to "BACK",
                        "Тяга гантели в наклоне одной рукой" to "BACK",
                        "Тяга Т-грифа" to "BACK",
                        "Тяга верхнего блока к груди/за голову" to "BACK",
                        "Тяга нижнего блока к поясу сидя" to "BACK",
                        "Пуловер с гантелью лежа (или в кроссовере стоя)" to "BACK",
                        "Шраги со штангой/гантелями (проработка трапеций)" to "BACK",
                        "Гиперэкстензия (проработка разгибателей спины/поясницы)" to "BACK",
                        
                        "Приседания со штангой на плечах" to "LEGS",
                        "Фронтальные приседания (штанга на груди)" to "LEGS",
                        "Жим ногами в тренажере" to "LEGS",
                        "Выпады (с гантелями или штангой)" to "LEGS",
                        "Болгарские сплит-приседания" to "LEGS",
                        "Румынская тяга (становая тяга на прямых ногах)" to "LEGS",
                        "Разгибание ног в тренажере сидя (квадрицепс)" to "LEGS",
                        "Сгибание ног в тренажере лежа/сидя (бицепс бедра)" to "LEGS",
                        "Подъем на носки стоя (икроножные мышцы)" to "LEGS",
                        "Подъем на носки сидя (камбаловидные мышцы)" to "LEGS",
                        "Сведение/разведение ног в тренажере" to "LEGS",
                        "Отведение ноги назад в кроссовере" to "LEGS",
                        
                        "Армейский жим (жим штанги стоя с груди)" to "SHOULDERS",
                        "Жим гантелей сидя/стоя" to "SHOULDERS",
                        "Жим Арнольда" to "SHOULDERS",
                        "Тяга штанги к подбородку (протяжка) широким хватом" to "SHOULDERS",
                        "Махи (разведение) гантелей в стороны (средняя дельта)" to "SHOULDERS",
                        "Подъем гантелей/блина перед собой (передняя дельта)" to "SHOULDERS",
                        "Махи гантелей в наклоне (задняя дельта)" to "SHOULDERS",
                        "Обратные разведения в тренажере \"Бабочка\" (задняя дельта)" to "SHOULDERS",
                        "Отведение руки в сторону на нижнем блоке кроссовера" to "SHOULDERS",
                        
                        "Жим штанги лежа узким хватом" to "ARMS",
                        "Отжимания на брусьях (узким хватом, корпус прямо)" to "ARMS",
                        "Отжимания от скамьи сзади (обратные отжимания)" to "ARMS",
                        "Французский жим (со штангой или гантелями лежа)" to "ARMS",
                        "Разгибание рук на верхнем блоке кроссовера (с канатной или прямой рукоятью)" to "ARMS",
                        "Разгибание руки с гантелью из-за головы" to "ARMS",
                        "Подтягивания обратным узким хватом" to "ARMS",
                        "Подъем штанги на бицепс стоя (прямой или EZ-гриф)" to "ARMS",
                        "Подъем гантелей на бицепс (с супинацией)" to "ARMS",
                        "\"Молотки\" (Hammer curls) с гантелями (акцент на брахиалис)" to "ARMS",
                        "Подъем на бицепс на скамье Скотта" to "ARMS",
                        "Концентрированный подъем гантели на бицепс сидя" to "ARMS",
                        
                        "Подъем ног в висе на турнике или в упоре на брусьях" to "CORE",
                        "Планка (классическая и боковая)" to "CORE",
                        "Скручивания с роликом для пресса (Ab wheel)" to "CORE",
                        "\"Складка\" (V-ups)" to "CORE",
                        "Классические скручивания на полу" to "CORE",
                        "Обратные скручивания" to "CORE",
                        "Скручивания на верхнем блоке (\"Молитва\")" to "CORE",
                        "Русские скручивания (Russian twists — для косых мышц)" to "CORE"
                    )

                    db.beginTransaction()
                    try {
                        val statement = db.compileStatement("INSERT INTO exercises (name, muscleGroup) VALUES (?, ?)")
                        for (exercise in exercises) {
                            statement.bindString(1, exercise.first)
                            statement.bindString(2, exercise.second)
                            statement.executeInsert()
                            statement.clearBindings()
                        }
                        db.setTransactionSuccessful()
                    } finally {
                        db.endTransaction()
                    }
                }
            }
        })
        .build()
    }

    // Dao
    single { get<AppDatabase>().exerciseDao }
    single { get<AppDatabase>().workoutDao }

    // Repositories
    single<ExerciseRepository> { ExerciseRepositoryImpl(get()) }
    single<WorkoutRepository> { WorkoutRepositoryImpl(get()) }
    
    // Preferences
    single { ThemePreferences(androidContext()) }

    // ViewModels
    factory { HomeViewModel(get()) }
    viewModel { ActiveWorkoutViewModel(get(), get()) }
    factory { ExerciseSelectionViewModel(get()) }
    viewModel { SettingsViewModel(get(), get()) }
    viewModel { ArchiveViewModel(get()) }
}
