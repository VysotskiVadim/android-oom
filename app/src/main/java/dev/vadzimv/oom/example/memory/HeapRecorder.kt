package dev.vadzimv.oom.example.memory

import android.content.Context
import android.os.Debug
import android.util.Log
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.Date

private const val LOG_TAG = "OOM-HEAP-RECORDER"
private const val HEAP_DUMP_PREFIX = "error-heap-dump"

fun recordHeapDumpOnOOM(context: Context) {
    val heapDumpName = context
        .filesDir
        .absolutePath + "/$HEAP_DUMP_PREFIX-${Date().time}.hprof"
    val heapDumpCompletedErrorMessage = "heap dump recording completed: $heapDumpName"
    Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
        if (throwable is OutOfMemoryError) {
            Log.e(LOG_TAG, "unhandled exception, recording heap dump")
            Debug.dumpHprofData(heapDumpName)
            Log.e(LOG_TAG, heapDumpCompletedErrorMessage)
        }
        Log.e(LOG_TAG, "Unhandled exception", throwable)
        System.exit(1);
    }
}

suspend fun listHeapDumps(context: Context): List<String> {
    val path = context.filesDir.absolutePath
    return withContext(Dispatchers.IO) {
        try {
            val directory = File(path)
            directory.list { _, name -> name.contains(HEAP_DUMP_PREFIX) }
                ?.toList()
                .orEmpty()
        } catch (ce: CancellationException) {
            throw ce
        } catch (t: Throwable) {
            emptyList()
        }
    }
}