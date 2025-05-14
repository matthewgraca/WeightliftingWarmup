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
     * This would be the "entrance" to the code at large; so input validation
     * should be done here/ensured are valid before being passed here.
     */
    fun createScheme(
        startWeight: Double,
        endWeight: Double,
        numSets: Int,
        setting: Setting,
        sys: WeightSystem
    ){
        when(setting){
            Setting.GREEDY -> {
                scheme.workingWeights = createGreedyWeightScheme(
                    startWeight, 
                    endWeight, 
                    numSets
                )
                scheme.plateScheme = createGreedyPlateScheme(
                    scheme.workingWeights, 
                    sys
                )
            }
            Setting.LAZY -> TODO()
        }
    }

    /**
     * Greedy weight scheme creation section
     */

    /**
     * Uses the given start and end weight with the number of sets to
     * generate a weight scheme. This scheme is linear.
     */
    fun createGreedyWeightScheme(
        startWeight: Double,
        endWeight: Double,
        numSets: Int
    ): List<Double>{
        val weightIncrement: Double = (endWeight - startWeight) / (numSets - 1)
        return List(numSets) { startWeight + weightIncrement * it }
    }

    /**
     * Uses the given list of weights to create a corresponding 
     * plate scheme map using the greedy method. 
     * 
     * Note that we're calculating plates for each side, not weight as a whole.
     */
    fun createGreedyPlateScheme(
        weights: List<Double>,
        sys: WeightSystem
    ): List<Map<Double, Int>>{
        return weights.map{ greedyCoinChange((it - weights.first()) / 2, sys) }
    }

    /**
     * Greedy solution to the coin change problem. Assumes it is given 
     * a total with a valid solution.
     */
    fun greedyCoinChange(
        weight: Double, 
        sys: WeightSystem
    ): MutableMap<Double, Int>{
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
        check(remainingWeight < 0.00001) {
          "Given input weight cannot create a valid solution"
        }

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
    ): String{
        if (startWeight < 0) 
            return "Start weight must be non-negative"

        if (endWeight < startWeight) 
            return "Start weight must be larger than end weight" 

        if (numSets <= 0)
            return "Number of sets must be positive"

        return "" 
    }
}
