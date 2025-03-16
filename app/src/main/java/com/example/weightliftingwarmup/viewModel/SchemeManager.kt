package com.example.weightliftingwarmup.viewModel

import com.example.weightliftingwarmup.model.Kilos
import com.example.weightliftingwarmup.model.Pounds
import com.example.weightliftingwarmup.model.Scheme
import com.example.weightliftingwarmup.model.Setting
import com.example.weightliftingwarmup.model.WeightSystem

class SchemeManager {
    private val scheme = Scheme()

    fun getWeightScheme(): List<Double> = scheme.workingWeights.toList()
    fun getPlateScheme(): List<List<Double>> = scheme.plateScheme.toList()

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
    ): MutableList<List<Double>>{
        val plateScheme: MutableList<List<Double>> = mutableListOf()
        weights.forEach{ plateScheme.add(greedyCoinChange((it - weights[0]) / 2 , sys)) }
        return plateScheme
    }

    // assumes a solution exists
    fun greedyCoinChange(weight: Double, sys: WeightSystem): List<Double>{
        val l: MutableList<Double> = mutableListOf()
        var remainingWeight = weight
        val coins = when(sys){
            WeightSystem.POUNDS -> Pounds().lbs
            WeightSystem.KILOS -> Kilos().kgs
        }

        while (remainingWeight > 0){
            // find largest coin that fits the remaining weight
            var i = coins.size - 1
            while (coins[i] > remainingWeight){
                i--
            }

            // add that coin to the list
            l.add(coins[i])
            remainingWeight -= coins[i]
        }

        // checking floating point equality, so arbitrarily have an epsilon of 0.00001
        check(remainingWeight < 0.00001) {"Given input weight cannot create a valid solution"}

        return l
    }

    //TODO
    //fun createLazyWeightScheme()
    //fun createLazyPlateScheme()
}