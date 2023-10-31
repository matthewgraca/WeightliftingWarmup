package com.example.weightliftingwarmup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weightliftingwarmup.ui.theme.WeightliftingWarmupTheme
import kotlin.math.round

val LBS_SYSTEM = listOf(2.5, 5.0, 10.0, 25.0, 35.0, 45.0, 55.0)
val KGS_SYSTEM = listOf(0.25, 0.5, 1.25, 2.5, 5.0, 10.0, 15.0, 20.0, 25.0)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeightliftingWarmupTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WeightliftingWarmupLayout()
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun WeightliftingWarmupLayout(){
    var initialWeightInput by remember { mutableStateOf("") }
    var finalWeightInput by remember { mutableStateOf("") }
    var setInput by remember { mutableStateOf("") }
    var isMetricInput by remember { mutableStateOf(false) }

    //  guarantees input is a valid number
    val initialWeight = initialWeightInput.toDoubleOrNull() ?: 0.0
    val finalWeight = finalWeightInput.toDoubleOrNull() ?: 0.0
    val numOfSets = setInput.toIntOrNull() ?: 0
    val warmupWeightList: List<Double>
    val warmupPlateList: MutableList<MutableList<Int>>

    // perform calculations if input is valid
    if (invalidInputs(numOfSets, initialWeight, finalWeight, isMetricInput)){
        warmupWeightList = emptyList()
        warmupPlateList = ArrayList()
    }
    else{ // if at any point the input is invalid, make the list empty
        warmupWeightList = calculateWarmupList(
            initialWeight,
            finalWeight,
            numOfSets,
            isMetricInput
        )
        warmupPlateList = calculatePlateList(initialWeight, warmupWeightList, isMetricInput)
    }

    Column(
        modifier = Modifier
            .padding(40.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        // title
        Text(
            text = stringResource(R.string.app_title),
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(alignment = Alignment.Start)
        )
        // input starting weight
        EditNumberField(
            label = R.string.initial_weight,
            leadingIcon = R.drawable.barbell,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            value = initialWeightInput,
            onValueChanged = { initialWeightInput = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth(),
        )
        // input working weight
        EditNumberField(
            label = R.string.final_weight,
            leadingIcon = R.drawable.barbell,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            value = finalWeightInput,
            onValueChanged = { finalWeightInput = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth(),
        )
        // input set count
        EditNumberField(
            label = R.string.number_of_sets,
            leadingIcon = R.drawable.number,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            value = setInput,
            onValueChanged = { setInput = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth(),
        )
        // imperial/metric button
        MetricSwitchButtonRow(
            button = isMetricInput,
            onButtonChanged = { isMetricInput = it },
            text = stringResource(id = R.string.metric_or_imperial),
            modifier = Modifier.padding(bottom = 32.dp)
        )
        // display list
        Text(
            text = stringResource(R.string.warmup_weight_list),
            style = MaterialTheme.typography.displaySmall
        )
        LazyColumn(
            modifier = Modifier
                .size(300.dp)
                .fillMaxWidth()
        )
        {
            item{
                warmupWeightList.zip(warmupPlateList){ a, b ->
                    Text(
                        text = "$a: $b",
                        //style = MaterialTheme.typography.displaySmall,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNumberField(
    @StringRes label: Int,
    @DrawableRes leadingIcon: Int,
    keyboardOptions: KeyboardOptions,
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        singleLine = true,
        leadingIcon = { Icon(painter = painterResource(id = leadingIcon), null) },
        modifier = modifier,
        onValueChange = onValueChanged,
        label = { Text(stringResource(label)) },
        keyboardOptions = keyboardOptions
    )
}

@Composable
fun MetricSwitchButtonRow(
    button: Boolean,
    onButtonChanged:(Boolean) -> Unit,
    text: String,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .size(48.dp),
    ) {
        Text(
            text = text
        )
        Switch(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End),
            checked = button,
            onCheckedChange = onButtonChanged
        )
    }
}

/**
 * Calculates the list of values that work up to a given weight. Assumes valid inputs.
 * @param   initialWeight: the initial value
 * @param   finalWeight: the value being worked up to
 * @param   numOfSets: the number of increments
 * @param   isMetric: determines how the list of values should be rounded
 * @return  the list of values working up to a given weight
 */
@VisibleForTesting
internal fun calculateWarmupList(
    initialWeight: Double,
    finalWeight: Double,
    numOfSets: Int,
    isMetric: Boolean
): List<Double>{
    // calculate increments
    val warmupList = MutableList(numOfSets){0.0}
    val weightIncrement = (finalWeight - initialWeight) / (numOfSets - 1)
    var currentWeightValue = initialWeight

    for (i in warmupList.indices){
        warmupList[i] =
            if (isMetric) round(currentWeightValue)
            else roundDoubleTo5th(currentWeightValue)
        currentWeightValue += weightIncrement
    }
    return warmupList
}

/**
 * Checks if all inputs are valid
 * @param   numOfSets: the number of sets
 * @param   initialWeight: the initial weight
 * @param   finalWeight: the final weight
 * @param   isMetric: determines if metric or imperial is used
 * @return  true if the inputs are invalid, false if they are valid
 */
@VisibleForTesting
internal fun invalidInputs(
    numOfSets: Int,
    initialWeight: Double,
    finalWeight: Double,
    isMetric: Boolean
): Boolean{
    return invalidNumOfSets(numOfSets) || invalidWeights(initialWeight, finalWeight, isMetric)
}
/**
 * Validates numOfSets input. Must be larger than a min value, and smaller than a max value.
 * @param   x: the value of numOfSets
 * @return  true if invalid, false if valid
 */
private fun invalidNumOfSets(x: Int): Boolean{
    val min = 2
    val max = 20
    return x < min || x > max
}

/**
 * Validates initialWeight and finalWeight inputs.
 * Must be positive and init > final.
 * If metric, it cannot have precision finer than ones
 * If imperial, it cannot have precision finer than fifths
 * @param   initialWeight: the value of initialWeight
 * @param   finalWeight: the value of finalWeight
 * @param   isMetric: determines if the function uses metric or imperial
 * @return  true if invalid, false if valid
 */
@VisibleForTesting
internal fun invalidWeights(
    initialWeight: Double,
    finalWeight: Double,
    isMetric: Boolean
): Boolean{
    // 1. check precision
    if (isMetric) {   // reject kgs if precision is finer than ones
        if (finalWeight % 1 != 0.0 || initialWeight % 1 != 0.0) return true
    }
    else {            // reject lbs if precision is finer than fifths
        if (finalWeight % 5 != 0.0 || initialWeight % 5 != 0.0) return true
    }
    // 2. check bounds
    return (initialWeight > finalWeight || initialWeight <= 0 || finalWeight <= 0)
}

/**
 * Takes a given weight list and coin system, and generates a list of coin change solutions
 * for each weight
 */
@VisibleForTesting
internal fun calculatePlateList(
    initialWeight: Double,
    weightList: List<Double>,
    isMetric: Boolean
): MutableList<MutableList<Int>>{
    val plateList: MutableList<MutableList<Int>> = ArrayList()
    weightList.forEach{weight ->
        val modifiedWeight = (weight - initialWeight) / 2
        plateList.add(plateSchemeOf(modifiedWeight, isMetric))
    }
    return plateList
}

/**
 * Calculates the stack of plates needed to arrive at a given weight. This is the coin
 * change problem. Using the assumption that the coin (weight) system is canonical, we use
 * the greedy method.
 * @param   weight: the value being reached
 * @param   isMetric: determines if the list uses LBS or KGS coin system
 * @return  a list containing the amount of weights needed to reach the weight based on the
 * coin system.
 */
@VisibleForTesting
internal fun plateSchemeOf(weight: Double, isMetric: Boolean): MutableList<Int>{
    var x = weight
    val coinSystem = if (isMetric) KGS_SYSTEM else LBS_SYSTEM
    val plateScheme = MutableList(coinSystem.size){0}
    var i = coinSystem.size - 1
    while (x != 0.0){
        if (x - coinSystem[i] < 0)
            i -= 1
        else{
            plateScheme[i] += 1
            x -= coinSystem[i]
        }
    }
    return plateScheme
}

/**
 * Rounds a double value to the nearest fifth; e.g. 6.0 -> 10.0, 4.0 -> 5.0, 1.0 -> 0.0
 * @param   x: the value being rounded
 * @return  the value, rounded to the nearest fifth
 */
private fun roundDoubleTo5th(x: Double): Double {
    val remainder = x % 10
    return if (remainder < 2.5)
        x - remainder       // round down to 0
    else if (remainder > 5)
        x - remainder + 10  // round up to 10
    else // 2.5 <= remainder <= 5
        x - remainder + 5   // round up to 5
}