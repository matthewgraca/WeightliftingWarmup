package com.example.weightliftingwarmup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weightliftingwarmup.ui.theme.WeightliftingWarmupTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeightliftingWarmupTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

// TODO compartmentalize these components into functions
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    var checked by remember { mutableStateOf(true) }

    Column{
        Switch(
            checked = checked,
            onCheckedChange = {
                checked = it
            },
            thumbContent = if (checked) {
                {
                    Text(
                        text = "lbs",
                        modifier = modifier
                    )
                }
            } else {
                {
                    Text(
                        text = "kgs",
                        modifier = modifier
                    )
                }
            }
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            var startNum by remember { mutableStateOf("") }
            var endNum by remember { mutableStateOf("") }

            TextField(
                value = startNum,
                onValueChange = { startNum = it },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                label = { Text("Start Weight") },
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
            )

            TextField(
                value = endNum,
                onValueChange = { endNum = it },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                label = { Text("End Weight") },
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
            )
        }

        val radioOptions = listOf("Greedy", "Lazy")
        val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }
        Column(modifier.selectableGroup()) {
            radioOptions.forEach { text ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                        .selectable(
                            selected = (text == selectedOption),
                            onClick = { onOptionSelected(text) },
                            role = Role.RadioButton
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (text == selectedOption),
                        onClick = null // null recommended for accessibility with screen readers
                    )
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }

        LazyColumn {
            // Add 5 items
            items(5) { index ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){
                    // Add a single item
                    Text(
                        text = "First item",
                        modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)
                    )
                    Text(
                        text = "Second item",
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WeightliftingWarmupTheme {
        Greeting("Android")
    }
}