package dev.vadzimv.oom.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import dev.vadzimv.oom.example.memory.gigabytesInBytes
import dev.vadzimv.oom.example.memory.listHeapDumps
import dev.vadzimv.oom.example.memory.megabytesInBytes
import dev.vadzimv.oom.example.memory.trackMemoryUsage
import dev.vadzimv.oom.example.ui.theme.OOMExampleTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

class MainActivity : ComponentActivity() {

    private val strings = mutableListOf<String>()
    private val arrays = mutableListOf<IntArray>()
    private var bigArray: ByteArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OOMExampleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val memoryUsage = trackMemoryUsage()
                        .collectAsStateWithLifecycle(null)
                        .value
                    val savedHeapDumps = savedHeapDumps()
                        .collectAsStateWithLifecycle(null)
                        .value
                    Column {
                        Text(text = "Max memory ${memoryUsage?.maxMemory?.formatMb()}")
                        Text(text = "Used memory ${memoryUsage?.usedMemory?.formatMb()}")
                        Text(text = "Available memory ${memoryUsage?.availableMemory?.formatMb()}")
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                bigAllocation()

                            }) {
                            Text(text = "Big allocation")
                        }
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                slowlyFillUpMemory()
                            }) {
                            Text(text = "Slow allocation")
                        }
                        if (!savedHeapDumps.isNullOrEmpty()) {
                            Text(text = "Saved heap dumps:")
                            Column(
                                Modifier.verticalScroll(
                                    rememberScrollState()
                                )
                            ) {
                                savedHeapDumps.forEach {
                                    Text(
                                        text = it,
                                        Modifier.padding(vertical = 10.dp)
                                        )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun bigAllocation() {
        bigArray = ByteArray(200.megabytesInBytes())
    }

    private fun slowlyFillUpMemory() {
        lifecycleScope.launch {
            launch(Dispatchers.Default) {
                while (true) {
                    strings.add("hello")
                    slowDownAllocation()
                }
            }
            launch(Dispatchers.Default) {
                while (true) {
                    arrays.add(intArrayOf(1, 2, 3, 4, 5, 6, 7, 8))
                    slowDownAllocation()
                }
            }
        }
    }

    private suspend fun slowDownAllocation() {
        yield()
    }

    private fun savedHeapDumps() = flow<List<String>?> {
        emit(listHeapDumps(applicationContext))
    }
}

