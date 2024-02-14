package com.gravitycode.solitaryfitnessapp.tests.components

//@MediumTest
//@RunWith(AndroidJUnit4::class)
//@ExperimentalCoroutinesApi
class PreferencesWorkoutLogsRepositoryTest {

//    companion object {
//
//        private const val TEST_DATA_STORE = "test_workout_history"
//        private val testDate = LocalDate.of(2000, 1, 1)
//
//        private val applicationContext: Context = ApplicationProvider.getApplicationContext()
//        private val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create {
//            applicationContext.preferencesDataStoreFile(TEST_DATA_STORE)
//        }
//        private val repository: WorkoutLogsRepository = PreferencesWorkoutLogsRepository(dataStore)
//
//        @AfterClass
//        @JvmStatic
//        @JvmName("staticClearTestDataStore")
//        fun clearTestDataStore() {
//            runBlocking {
//                dataStore.edit { preferences ->
//                    preferences.clear()
//                }
//            }
//        }
//    }
//
//    @Before
//    fun clearTestDataStore() {
//        PreferencesWorkoutLogsRepositoryTest.clearTestDataStore()
//    }
//
//    @Test
//    fun readEmptyRecord() {
//        runTest {
//            val result = repository.readWorkoutLog(testDate)
//            assertSuccess(result)
//            assertEquals(null, result.getOrNull())
//        }
//    }
//
//    @Test
//    fun writeAndReadRecord() {
//        runTest {
//            val workoutLog = WorkoutLog(
//                pressUps = 15,
//                handstandPressUps = 5,
//                starJumps = 25
//            )
//
//            repository.writeWorkoutLog(testDate, workoutLog)
//            val result = repository.readWorkoutLog(testDate)
//            assertSuccess(result)
//            assertEquals(workoutLog, result.getOrNull())
//        }
//    }
//
//    @Test
//    fun updateRecord() {
//        runTest {
//            val workoutLog = WorkoutLog(
//                squats = 50,
//                squatThrusts = 25,
//                sitUps = 100
//            )
//
//            repository.writeWorkoutLog(testDate, workoutLog)
//            repository.updateWorkoutLog(testDate, Workout.SQUAT_THRUST, 50)
//
//            val updatedLog = workoutLog.copy(squatThrusts = 50)
//
//            val result = repository.readWorkoutLog(testDate)
//            assertSuccess(result)
//            assertEquals(updatedLog, result.getOrNull())
//        }
//    }
//
//    @Test
//    fun deleteRecord() {
//        runTest {
//            val workoutLog = WorkoutLog(
//                squats = 50,
//                squatThrusts = 25,
//                sitUps = 100
//            )
//
//            repository.writeWorkoutLog(testDate, workoutLog)
//            repository.deleteWorkoutLog(testDate)
//
//            val result = repository.readWorkoutLog(testDate)
//
//            assertSuccess(result)
//            assertEquals(null, result.getOrNull())
//        }
//    }
}