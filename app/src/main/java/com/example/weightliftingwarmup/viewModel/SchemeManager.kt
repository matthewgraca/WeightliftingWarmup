package com.example.weightliftingwarmup.viewModel

import com.example.weightliftingwarmup.model.*

object SchemeManager {
    private val scheme = Scheme()

    fun getWeightScheme(): List<Double> = scheme.workingWeights
    fun getPlateScheme(): List<Map<Double, Int>> = scheme.plateScheme

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
        // expect the coins to be sorted due to findLast{}
        check(coins.sorted() == coins) { 
            "Weight system is not sorted, received: $coins" 
        }
        
        val coinCount = coins.associateWith{ 0 }.toMutableMap()

        var remainingTotal = weight
        val maxIterations = 10000
        var i = 0
        check(weight % coins.first() == 0.0) {
            "No solution can be generated for $remainingTotal, " +
            "from the coins: $coins."
        }
        // checking fp equality, so arbitrarily have an epsilon of 0.00001
        while (remainingTotal > 0.00001){
            val largestFitCoin: Double = coins.findLast{ 
                it <= remainingTotal 
            } ?: -1.0
            check(largestFitCoin != -1.0) {
                "A solution should be possible, but was not found."
            }

            coinCount[largestFitCoin] = coinCount.getValue(largestFitCoin) + 1
            remainingTotal -= largestFitCoin 

            i += 1
            check(i != maxIterations) { 
                "A solution should be possible, but was not found within " +
                "$maxIterations iterations. " + 
                "Remaining total: $remainingTotal, coins: $coins."
            }
        }

        return coinCount
    }

    /**
     * Lazy weight scheme creation section
     */
    fun createLazyWeightScheme(): Nothing = TODO()
    fun createLazyPlateScheme(): Nothing = TODO()

    /**
     * Input validation section. The idea here is that if there is no error, 
     * an empty string will be returned; otherwise, the first error to hit
     * has its message returned.
     */
    fun validateInputs(
        startWeight: Double?,
        endWeight: Double?,
        numSets: Int?,
        setting: Setting,
        sys: WeightSystem
    ): String{
        if (startWeight == null || startWeight < 0.0) 
            return "Start weight must be non-negative"

        if (endWeight == null || endWeight < startWeight) 
            return "End weight cannot be less than start weight" 

        if (numSets == null || numSets <= 0)
            return "Number of sets must be positive"
        
        return validateSolvability(
            startWeight, endWeight, numSets, setting, sys
        )
    }

    /**
     * Checks if the given inputs can generate a solution. Expects that 
     * startWeight, endWeight, and numSets are valid.
     */
    fun validateSolvability(
        startWeight: Double,
        endWeight: Double,
        numSets: Int,
        setting: Setting,
        sys: WeightSystem
    ): String{
        // weight should be divisible by the smallest unit to have a solution
        val weightsAvailable = when (sys){
            WeightSystem.POUNDS -> Pounds().lbs
            WeightSystem.KILOS -> Kilos().kgs
        }
        check (weightsAvailable.sorted() == weightsAvailable) {
            "The weights available is expected to be sorted: $weightsAvailable."
        }
        if ((endWeight - startWeight) / 2 % weightsAvailable.first() != 0.0)
            return "A solution is not possible with the weights available"

        // sometimes the number of sets divides the weight too precisely
        val increment = (endWeight - startWeight) / 2 / (numSets - 1)
        val validIncrement = when (setting){
            Setting.GREEDY -> increment % weightsAvailable.first() == 0.0
            Setting.LAZY -> true 
        }
        if (!validIncrement){
            return "A solution is not possible with the number of sets given"
        }

        return ""
    }
}
