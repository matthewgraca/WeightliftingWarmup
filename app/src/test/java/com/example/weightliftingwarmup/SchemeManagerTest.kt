package com.example.weightliftingwarmup

import com.example.weightliftingwarmup.model.Scheme
import com.example.weightliftingwarmup.model.Setting
import com.example.weightliftingwarmup.model.WeightSystem
import com.example.weightliftingwarmup.viewModel.SchemeManager
import org.junit.Test
import org.junit.Assert.*

class SchemeManagerTest {
    @Test
    fun getWeightScheme_getsEmptyScheme(){
        val sm = SchemeManager()
        assertTrue(sm.getWeightScheme().isEmpty())
    }

    @Test
    fun getPlateScheme_getsEmptyScheme(){
        val sm = SchemeManager()
        assertTrue(sm.getPlateScheme().isEmpty())
    }

    @Test
    fun createScheme_successfullyAddsValidWeightScheme(){
        val sm = SchemeManager()
        sm.createScheme(45.0, 205.0, 5, Setting.GREEDY, WeightSystem.POUNDS)
        val expected = listOf(45.0, 85.0, 125.0, 165.0, 205.0)
        assertTrue(sm.getWeightScheme() == expected)
    }

    @Test
    fun createScheme_successfullyAddsValidPlateScheme(){
        val sm = SchemeManager()
        sm.createScheme(45.0, 205.0, 5, Setting.GREEDY, WeightSystem.POUNDS)
        val expected = listOf(
            listOf(),
            listOf(10.0, 10.0),
            listOf(35.0, 5.0),
            listOf(55.0, 5.0),
            listOf(55.0, 25.0)
        )
        val real = sm.getPlateScheme()
        assertTrue("Expected: $expected, Actual: $real", real == expected)
    }

    @Test
    fun createScheme_successfullyReplacesWeightScheme(){
        val sm = SchemeManager()
        sm.createScheme(45.0, 305.0, 5, Setting.GREEDY, WeightSystem.POUNDS)
        sm.createScheme(45.0, 205.0, 5, Setting.GREEDY, WeightSystem.POUNDS)
        val expected = listOf(45.0, 85.0, 125.0, 165.0, 205.0)
        assertTrue(sm.getWeightScheme() == expected)
    }

    @Test
    fun createScheme_successfullyReplacesPlateScheme(){
        val sm = SchemeManager()
        sm.createScheme(45.0, 305.0, 5, Setting.GREEDY, WeightSystem.POUNDS)
        sm.createScheme(45.0, 205.0, 5, Setting.GREEDY, WeightSystem.POUNDS)
        val expected = listOf(
            listOf(),
            listOf(10.0, 10.0),
            listOf(35.0, 5.0),
            listOf(55.0, 5.0),
            listOf(55.0, 25.0)
        )
        assertTrue(sm.getPlateScheme() == expected)
    }

    @Test
    fun createGreedyPlateScheme_createsValidPlateScheme(){
        val sm = SchemeManager()
        val real = sm.createGreedyPlateScheme(
            listOf(45.0, 85.0, 125.0, 165.0, 205.0),
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
        val sm = SchemeManager()
        val real = sm.greedyCoinChange(80.0, WeightSystem.POUNDS)
        val expected = listOf<Double>(55.0, 25.0)
        assertTrue("Expected: $expected, Actual: $real", real == expected)
    }
}