package dev.vadzimv.oom.example.memory

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

fun trackMemoryUsage(
    interval: Duration = 100.toDuration(DurationUnit.MILLISECONDS)
) = flow<HeapUsage> {
    while (true) {
        val runtime = Runtime.getRuntime()
        val maxMemory = runtime.maxMemory().bytes()
        val freeMemory = runtime.freeMemory().bytes()
        val totalMemory = runtime.totalMemory().bytes()
        val usedMemory = totalMemory - freeMemory
        val availableMemory = maxMemory - usedMemory
        emit(
            HeapUsage(
                maxMemory = maxMemory,
                usedMemory = usedMemory,
                availableMemory = availableMemory
            )
        )
        delay(interval)
    }
}


data class HeapUsage(
    val maxMemory: Bytes,
    val usedMemory: Bytes,
    val availableMemory: Bytes
)