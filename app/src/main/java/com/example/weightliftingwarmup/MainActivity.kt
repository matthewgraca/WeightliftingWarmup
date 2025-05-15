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
import com.example.weightliftingwarmup.viewModel.SchemeManager
import com.example.weightliftingwarmup.model.*
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.text.style.TextAlign


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
    var startNum by remember { mutableStateOf("") }
    var endNum by remember { mutableStateOf("") }
    var setNum by remember { mutableStateOf("") }
    val radioOptionsSetting = listOf("Greedy", "Lazy")
    val (selectedOptionSetting, onOptionSelectedSetting) = remember {
        mutableStateOf(radioOptionsSetting[0])
    }
    val radioOptionsSystem = listOf("Pounds", "Kilograms")
    val (selectedOptionSystem, onOptionSelectedSystem) = remember {
        mutableStateOf(radioOptionsSystem[0])
    }


    Column{
        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            TextField(
                value = startNum,
                onValueChange = { startNum = it },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                label = { Text("Start") },
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
                label = { Text("End") },
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
            )

            TextField(
                value = setNum,
                onValueChange = { setNum = it },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                label = { Text("Sets") },
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
            )
        }

        Column(modifier.selectableGroup()) {
            var text = radioOptionsSetting[0]
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .selectable(
                        selected = (text == selectedOptionSetting),
                        onClick = { onOptionSelectedSetting(text) },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 0.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "\uD83D\uDCC8 System",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .weight(1f)
                )
                radioOptionsSetting.forEach { text ->
                    RadioButton(
                        selected = (text == selectedOptionSetting),
                        onClick = null // null recommended for accessibility with screen readers
                    )
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .padding(start = 8.dp, end = 8.dp)
                            .weight(1f)
                    )
                }
            }
            text = radioOptionsSystem[0]
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .selectable(
                        selected = (text == selectedOptionSystem),
                        onClick = { onOptionSelectedSystem(text) },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 0.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "\uD83D\uDCCF Units",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .weight(1f)
                )
                radioOptionsSystem.forEach{ text ->
                    RadioButton(
                        selected = (text == selectedOptionSystem),
                        onClick = null // null recommended for accessibility with screen readers
                    )
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .padding(start = 8.dp, end = 8.dp)
                            .weight(1f)
                    )
                }
            }
        }

        // TODO -- remove hardcoded scheme.
        // TODO get value of radio button
        val errorMsg = SchemeManager.validateInputs(
            startNum.toDoubleOrNull(),
            endNum.toDoubleOrNull(),
            setNum.toIntOrNull(),
            Setting.GREEDY,
            WeightSystem.POUNDS
        )
        if (errorMsg.isEmpty()){
            SchemeManager.createScheme(
                startNum.toDouble(),
                endNum.toDouble(),
                setNum.toInt(),
                Setting.GREEDY,
                WeightSystem.POUNDS
            )
            val columnItems = SchemeManager.getWeightScheme().zip(SchemeManager.getPlateScheme())

            LazyColumn {
                // Add 5 items
                items(items=columnItems) { (weight, plate) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        // Add a single item
                        Text(
                            text = "$weight",
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp)
                        )
                        Text(
                            text = "${plate.filter{ (_, v) -> v != 0 }}",
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp)
                        )
                    }
                }
            }
        }
        else{
            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = "‼\uFE0F $errorMsg ‼\uFE0F",
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp),
                    textAlign = TextAlign.Center
                )
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