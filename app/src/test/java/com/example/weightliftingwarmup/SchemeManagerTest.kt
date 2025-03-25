package com.example.weightliftingwarmup

import com.example.weightliftingwarmup.model.Scheme
import com.example.weightliftingwarmup.model.Setting
import com.example.weightliftingwarmup.model.WeightSystem
import com.example.weightliftingwarmup.viewModel.SchemeManager
import org.junit.Test
import org.junit.Assert.*

class SchemeManagerTest {
    @Test
    fun createScheme_successfullyAddsValidWeightScheme(){
        SchemeManager.createScheme(45.0, 205.0, 5, Setting.GREEDY, WeightSystem.POUNDS)
        val expected = listOf(45.0, 85.0, 125.0, 165.0, 205.0)
        assertTrue(SchemeManager.getWeightScheme() == expected)
    }

    @Test
    fun createScheme_successfullyAddsValidPlateScheme(){
        SchemeManager.createScheme(45.0, 205.0, 5, Setting.GREEDY, WeightSystem.POUNDS)
        val expected = listOf(
            listOf(),
            listOf(10.0, 10.0),
            listOf(35.0, 5.0),
            listOf(55.0, 5.0),
            listOf(55.0, 25.0)
        )
        val real = SchemeManager.getPlateScheme()
        assertTrue("Expected: $expected, Actual: $real", real == expected)
    }

    @Test
    fun createScheme_successfullyReplacesWeightScheme(){
        SchemeManager.createScheme(45.0, 305.0, 5, Setting.GREEDY, WeightSystem.POUNDS)
        SchemeManager.createScheme(45.0, 205.0, 5, Setting.GREEDY, WeightSystem.POUNDS)
        val expected = listOf(45.0, 85.0, 125.0, 165.0, 205.0)
        assertTrue(SchemeManager.getWeightScheme() == expected)
    }

    @Test
    fun createScheme_successfullyReplacesPlateScheme(){
        SchemeManager.createScheme(45.0, 305.0, 5, Setting.GREEDY, WeightSystem.POUNDS)
        SchemeManager.createScheme(45.0, 205.0, 5, Setting.GREEDY, WeightSystem.POUNDS)
        val expected = listOf(
            listOf(),
            listOf(10.0, 10.0),
            listOf(35.0, 5.0),
            listOf(55.0, 5.0),
            listOf(55.0, 25.0)
        )
        assertTrue(SchemeManager.getPlateScheme() == expected)
    }

    @Test
    fun createGreedyPlateScheme_createsValidPlateScheme(){
        SchemeManager.createScheme(45.0, 205.0, 5, Setting.GREEDY, WeightSystem.POUNDS)
        val real = SchemeManager.createGreedyPlateScheme(
            SchemeManager.getWeightScheme(),
            WeightSystem.POUNDS
        )
        val expected = listOf(
            listOf(),
            listOf(10.0, 10.0),
            listOf(35.0, 5.0),
            listOf(55.0, 5.0),
            listOf(55.0, 25.0)
        )
        assertTrue("Expected: $expected, Actual: $real", real == expected)
    }

    @Test
    fun greedyCoinChange_worksForValidSum(){
        val real = SchemeManager.greedyCoinChange(80.0, WeightSystem.POUNDS)
        val expected = listOf<Double>(55.0, 25.0)
        assertTrue("Expected: $expected, Actual: $real", real == expected)
    }
}