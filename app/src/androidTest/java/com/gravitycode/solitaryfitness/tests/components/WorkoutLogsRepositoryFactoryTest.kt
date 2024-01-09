package com.gravitycode.solitaryfitness.tests.components

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.gravitycode.solitaryfitness.log_workout.data.LazyWorkoutLogsRepositoryFactory
import com.gravitycode.solitaryfitness.test_implementations.EmptyWorkoutLogsRepository
import com.gravitycode.solitaryfitness.test_utils.attack
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class WorkoutLogsRepositoryFactoryTest {

    private val offlineRepository = EmptyWorkoutLogsRepository()
    private val onlineRepository = EmptyWorkoutLogsRepository()

    @Test
    fun testFactoryNeverReturnsWrongInstanceWhenAccessedFromMultipleThreads() {
        val factory = LazyWorkoutLogsRepositoryFactory(
            { offlineRepository },
            { onlineRepository }
        )

        attack {
            assertSame(offlineRepository, factory.getOfflineRepository())
            assertSame(onlineRepository, factory.getOnlineRepository())
        }
    }
}