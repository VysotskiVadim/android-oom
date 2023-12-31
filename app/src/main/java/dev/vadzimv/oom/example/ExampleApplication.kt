package dev.vadzimv.oom.example

import android.app.Application
import dev.vadzimv.oom.example.memory.recordHeapDumpOnOOM

class ExampleApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        recordHeapDumpOnOOM(this)
    }
}