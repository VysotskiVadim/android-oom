package dev.vadzimv.oom.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import dev.vadzimv.oom.example.ui.theme.OOMExampleTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

class MainActivity : ComponentActivity() {

    private val strings = mutableListOf<String>()
    private val arrays = mutableListOf<IntArray>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OOMExampleTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Button(
                        modifier = Modifier.wrapContentHeight(),
                        onClick = {
                            lifecycleScope.launch {
                                launch {
                                    while (true) {
                                        strings.add("hello")
                                        yield()
                                    }
                                }
                                launch {
                                    while (true) {
                                        arrays.add(intArrayOf(1, 2, 3, 4, 5, 6, 7, 8))
                                        yield()
                                    }
                                }
                            }
                    }) {
                        Text(text = "Start allocation")
                    }
                }
            }
        }
    }
}
