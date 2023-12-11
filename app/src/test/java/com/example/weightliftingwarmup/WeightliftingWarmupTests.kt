package com.example.weightliftingwarmup

import org.junit.Test
import org.junit.Assert.*

class WeightliftingWarmupTests {
    /**
     * Invalid inputs tests
     */
    @Test
    fun invalidInputs_setsOutOfMinRange() {
        val initialWeight = 45.0
        val finalWeight = 205.0
        val numOfSets = 1
        val isMetric = false
        val actualBool = invalidInputs(numOfSets, initialWeight, finalWeight, isMetric)
        val expectedBool = true
        assertEquals(expectedBool, actualBool)
    }

    @Test
    fun invalidInputs_setsOutOfMaxRange() {
        val initialWeight = 45.0
        val finalWeight = 205.0
        val numOfSets = 51
        val isMetric = false
        val actualBool = invalidInputs(numOfSets, initialWeight, finalWeight, isMetric)
        val expectedBool = true
        assertEquals(expectedBool, actualBool)
    }

    @Test
    fun invalidInputs_zeroWeightOutOfRange() {
        val initialWeight = 45.0
        val finalWeight = 0.0
        val numOfSets = 5
        val isMetric = false
        val actualBool = invalidInputs(numOfSets, initialWeight, finalWeight, isMetric)
        val expectedBool = true
        assertEquals(expectedBool, actualBool)
    }

    @Test
    fun invalidInputs_negWeightOutOfRange() {
        val initialWeight = 45.0
        val finalWeight = -10.3
        val numOfSets = 5
        val isMetric = false
        val actualBool = invalidInputs(numOfSets, initialWeight, finalWeight, isMetric)
        val expectedBool = true
        assertEquals(expectedBool, actualBool)
    }

    @Test
    fun invalidInputs_negInitialWeightOutOfRange() {
        val initialWeight = -10.0
        val finalWeight = 200.0
        val numOfSets = 5
        val isMetric = false
        val actualBool = invalidInputs(numOfSets, initialWeight, finalWeight, isMetric)
        val expectedBool = true
        assertEquals(expectedBool, actualBool)
    }

    @Test
    fun invalidInput_initialLessThanFinalWeightFails() {
        val initialWeight = 100.0
        val finalWeight = 50.0
        val numOfSets = 5
        val isMetric = false
        val actualBool = invalidInputs(numOfSets, initialWeight, finalWeight, isMetric)
        val expectedBool = true
        assertEquals(expectedBool, actualBool)
    }

    @Test
    fun calculateWarmupList_initialWeight155AndFinalWeight405() {
        val initialWeight = 155.0
        val finalWeight = 405.0
        val numOfSets = 5
        val isMetric = false
        val actualList = calculateWarmupList(initialWeight, finalWeight, numOfSets, isMetric)
        val expectedList = listOf(155.0, 220.0, 280.0, 345.0, 405.0)
        assertEquals(expectedList, actualList)
    }

    @Test
    fun invalidInputs_initialWeight155AndFinalWeight405() {
        val initialWeight = 155.0
        val finalWeight = 405.0
        val numOfSets = 5
        val isMetric = false
        val actualBool = invalidInputs(numOfSets, initialWeight, finalWeight, isMetric)
        val expectedBool = false
        assertEquals(expectedBool, actualBool)
    }

    @Test
    fun calculateWarmupList_irrationalIncrementsAreTruncatedMetricOn() {
        val initialWeight = 45.0
        val finalWeight = 145.0
        val numOfSets = 4
        val isMetric = true
        val actualList = calculateWarmupList(initialWeight, finalWeight, numOfSets, isMetric)
        val expectedList = listOf(45.0, 78.0, 112.0, 145.0)
        assertEquals(expectedList, actualList)
    }

    /**
     * Calculate Warmup List tests
     */
    @Test
    fun calculateWarmupList_evenlyDivisibleIntIncrements() {
        val initialWeight = 45.0
        val finalWeight = 205.0
        val numOfSets = 5
        val isMetric = false
        val actualList = calculateWarmupList(initialWeight, finalWeight, numOfSets, isMetric)
        val expectedList = listOf(45.0, 85.0, 125.0, 165.0, 205.0)
        assertEquals(expectedList, actualList)
    }

    @Test
    fun calculateWarmupList_unevenlyDivisibleDoubleIncrements() {
        val initialWeight = 45.0
        val finalWeight = 200.0
        val numOfSets = 5
        val isMetric = false
        val actualList = calculateWarmupList(initialWeight, finalWeight, numOfSets, isMetric)
        val expectedList = listOf(45.0, 85.0, 125.0, 160.0, 200.0)
        assertEquals(expectedList, actualList)
    }
    @Test
    fun calculatePlateList_worksWithLbs(){
        val weightList = listOf(45.0, 135.0, 225.0, 315.0, 405.0)
        val initialWeight = 45.0
        val isMetric = false
        val actualList = listOf(
            listOf(0, 0, 0, 0, 0, 0, 0),
            listOf(0, 0, 0, 0, 0, 1, 0),
            listOf(0, 0, 0, 0, 1, 0, 1),
            listOf(0, 0, 0, 1, 0, 0, 2),
            listOf(0, 1, 1, 0, 0, 0, 3)
        )
        val expectedList = calculatePlateList(initialWeight, weightList, isMetric)
        assertEquals(expectedList, actualList)
    }

    /**
     * Invalid weights tests
     */
    @Test
    fun invalidWeights_trueForKgMorePreciseThanOnes(){
        val x = 5.1
        val y = 15.0
        val metric = true
        val actualBool = invalidWeights(x, y, metric)
        val expectedBool = true
        assertEquals(expectedBool, actualBool)
    }

    @Test
    fun invalidWeights_trueForLbsMorePreciseThanFifths(){
        val x = 13.0
        val y = 15.0
        val metric = false
        val actualBool = invalidWeights(x, y, metric)
        val expectedBool = true
        assertEquals(expectedBool, actualBool)
    }

    /**
     * Plate Scheme tests
     */
    @Test
    fun greedyPlateSchemeOf_worksWithLbs(){
        // 405 / 2 - 45
        val weight = 180.0
        val isMetric = false
        val actualList = greedyPlateSchemeOf(weight, isMetric)
        val expectedList = listOf(0, 1, 1, 0, 0, 0, 3)
        assertEquals(expectedList, actualList)
    }

    @Test(expected = IllegalArgumentException::class)
    fun greedyPlateSchemeOf_lbsFailsWithBadPrecisionInput(){
        val weight = 183.0
        val isMetric = false
        greedyPlateSchemeOf(weight, isMetric)
    }

    @Test
    fun dynamicPlateSchemeOf_worksWithLbs(){
        // 405 / 2 - 45
        val weight = 180.0
        val isMetric = false
        val actualList = dynamicPlateSchemeOf(weight, isMetric)
        val expectedList = listOf(0, 1, 1, 0, 0, 0, 3)
        //assertEquals(expectedList, actualList)
    }

    @Test
    fun dynamicPlateSchemeOf_beatsGreedy(){
        val weight = 70.0
        val isMetric = false
        val actualList = dynamicPlateSchemeOf(weight, isMetric)
        val expectedList = listOf(0, 0, 0, 0, 2, 0, 0)
        //assertEquals(expectedList, actualList)
    }

    /**
     * Dynamic solution test (helper functions)
     */
    @Test
    fun initSolutionsMatrix_initializesProperly(){
        val setOfCoins = listOf(1, 2, 4, 10, 14, 18, 22)
        val target = 10
        val expectedList = mutableListOf<MutableList<Int>>()
        expectedList.add(mutableListOf(0, Int.MAX_VALUE, Int.MAX_VALUE, Int.MAX_VALUE,
            Int.MAX_VALUE, Int.MAX_VALUE, Int.MAX_VALUE, Int.MAX_VALUE, Int.MAX_VALUE,
            Int.MAX_VALUE, Int.MAX_VALUE))
        expectedList.add(mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
        expectedList.add(mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
        expectedList.add(mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
        expectedList.add(mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
        expectedList.add(mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
        expectedList.add(mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
        expectedList.add(mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))

        val actualList = initSolutionMatrix(setOfCoins, target)
        assertEquals(expectedList, actualList)
    }

    @Test
    fun weightMaking_getsMinimumCoins(){
        // lbs, target = 70 scaled to ints
        val coins = listOf(1, 2, 4, 10, 14, 18, 22)
        val target = 28
        val expected = 2 // two 35 lbs plates
        val actual = weightMaking(coins, target)
        assertEquals(expected, actual)
    }
}