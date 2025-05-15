package com.example.weightliftingwarmup.model

data class Scheme(
    var workingWeights: List<Double> = emptyList(),
    var plateScheme: List<Map<Double, Int>> = emptyList()
)

enum class Setting{
    GREEDY, // greedy method
    LAZY,   // least plate swap method
}

enum class WeightSystem{
    POUNDS,
    KILOS
}

data class Kilos(
    val kgs: List<Double> = listOf(
        0.25, 0.5, 1.25, 2.5, 5.0, 10.0, 15.0, 20.0, 25.0
    )
)

data class Pounds(
    val lbs: List<Double> = listOf(
        0.5, 1.0, 1.5, 2.5, 5.0, 10.0, 25.0, 35.0, 45.0, 55.0
    )
)
