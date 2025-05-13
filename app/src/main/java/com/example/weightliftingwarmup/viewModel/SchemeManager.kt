package com.example.weightliftingwarmup.viewModel

import com.example.weightliftingwarmup.model.Kilos
import com.example.weightliftingwarmup.model.Pounds
import com.example.weightliftingwarmup.model.Scheme
import com.example.weightliftingwarmup.model.Setting
import com.example.weightliftingwarmup.model.WeightSystem

object SchemeManager {
    private val scheme = Scheme()

    fun getWeightScheme(): List<Double> = scheme.workingWeights.toList()
    fun getPlateScheme(): List<Map<Double, Int>> = scheme.plateScheme.toList()

    /**
     * Create a valid scheme based on the user's parameters.
     */
    fun createScheme(
        startWeight: Double,
        endWeight: Double,
        numSets: Int,
        setting: Setting,
        sys: WeightSystem
    ){
        // assumes inputs are valid
        when(setting){
            Setting.GREEDY -> {
                scheme.workingWeights = createGreedyWeightScheme(startWeight, endWeight, numSets)
                scheme.plateScheme = createGreedyPlateScheme(scheme.workingWeights, sys)
            }
            Setting.LAZY -> TODO()
        }
    }

    /**
     * Greedy weight scheme creation section
     */

    fun createGreedyWeightScheme(
        startWeight: Double,
        endWeight: Double,
        numSets: Int
    ): MutableList<Double>{
        // assumes input from addScheme() is valid
        val weightIncrement: Double = (endWeight - startWeight) / (numSets - 1)
        val exactScheme = mutableListOf<Double>(startWeight)
        for (i in 1..numSets-1){
            exactScheme.add(exactScheme[i-1] + weightIncrement)
        }
        return exactScheme
    }

    fun createGreedyPlateScheme(
        weights: List<Double>,
        sys: WeightSystem
    ): List<Map<Double, Int>>{
        val plateScheme: MutableList<MutableMap<Double, Int>> = mutableListOf()
        weights.forEach{
            plateScheme.add(
                greedyCoinChange((it - weights[0]) / 2, sys)
            )
        }
        return plateScheme
    }

    // assumes a solution exists
    fun greedyCoinChange(weight: Double, sys: WeightSystem): MutableMap<Double, Int>{
        val coins = when(sys){
            WeightSystem.POUNDS -> Pounds().lbs
            WeightSystem.KILOS -> Kilos().kgs
        }
        val coinCounts = coins.associateWith{ 0 }.toMutableMap()

        var remainingWeight = weight
        while (remainingWeight > 0){
            // find largest coin that fits the remaining weight
            var i = coins.size - 1
            while (coins[i] > remainingWeight){
                i--
            }

            // add that coin to the list
            val currentCoinValue = coinCounts.getValue(coins[i])
            coinCounts[coins[i]] = currentCoinValue + 1
            remainingWeight -= coins[i]
        }

        // checking floating point equality, so arbitrarily have an epsilon of 0.00001
        check(remainingWeight < 0.00001) {"Given input weight cannot create a valid solution"}

        return coinCounts
    }

    /**
     * Lazy weight scheme creation section
     */
    fun createLazyWeightScheme(): Nothing = TODO()
    fun createLazyPlateScheme(): Nothing = TODO()

    /**
     * Input validation section
     */
    // TODO: If the inputs are invalid, what should we do? Throw errors? Return defaults?
    // maybe under user input, it would notify the error; e.g. end weight must be larger than start weight
    fun validateInputs(
        startWeight: Double,
        endWeight: Double,
        numSets: Int,
        setting: Setting,
        sys: WeightSystem
    ): Boolean{
        if (!validateStartWeight(startWeight)) return false

        if (!validateEndWeight(startWeight, endWeight)) return false

        if (!validateNumSets(numSets)) return false

        return true
    }

    // condition: must be a non-negative number
    fun validateStartWeight(startWeight: Double) = startWeight >= 0

    // condition: ending weight >= starting weight
    fun validateEndWeight(startWeight: Double, endWeight: Double) = endWeight >= startWeight

    // condition: more than 0 sets
    fun validateNumSets(numSets: Int) = numSets > 0
}