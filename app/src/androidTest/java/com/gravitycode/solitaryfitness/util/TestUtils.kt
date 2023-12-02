package com.gravitycode.solitaryfitness.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.runBlocking

/**
 * From: [Synchronization and Thread-Safety Techniques in Java and Kotlin](https://proandroiddev.com/synchronization-and-thread-safety-techniques-in-java-and-kotlin-f63506370e6d)
 *
 * Any tests run with this function will be at minimum [androidx.test.filters.MediumTest], but more likely
 * [androidx.test.filters.LargeTest]
 * */
@OptIn(DelicateCoroutinesApi::class)
fun attack(threads: Int = 4, couroutines: Int = 1000, iterations: Int = 1000, block: () -> Unit) {
    runBlocking {
        val scope = CoroutineScope(newFixedThreadPoolContext(threads, "synchronizationPool"))

        scope.launch {
            val coroutines = 1.rangeTo(couroutines).map { //create 1000 coroutines (light-weight threads).
                launch {
                    for (i in 1..iterations) {
                        block()
                    }
                }
            }

            coroutines.forEach { coroutine ->
                coroutine.join() // wait for all coroutines to finish their jobs.
            }
        }.join()
    }
}