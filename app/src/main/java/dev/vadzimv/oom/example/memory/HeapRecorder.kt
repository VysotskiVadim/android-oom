package dev.vadzimv.oom.example.memory

import android.content.Context
import android.os.Debug
import android.util.Log
import java.util.Date

private const val LOG_TAG = "OOM-HEAP-RECORDER"

fun recordHeapDumpOnOOM(context: Context) {
    val heapDumpName = context
        .filesDir
        .absolutePath + "/error-heap-dump-${Date().time}.hprof"
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