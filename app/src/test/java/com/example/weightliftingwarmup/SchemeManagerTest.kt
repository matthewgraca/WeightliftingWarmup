package com.example.weightliftingwarmup

import com.example.weightliftingwarmup.model.Pounds
import com.example.weightliftingwarmup.model.Setting
import com.example.weightliftingwarmup.model.WeightSystem
import com.example.weightliftingwarmup.viewModel.SchemeManager
import org.junit.Test
import org.junit.Assert.*

class SchemeManagerTest {
    @Test
    fun createScheme_successfullyAddsValidWeightScheme(){
        SchemeManager.createScheme(
            45.0,
            205.0,
            5, 
            Setting.GREEDY,
            WeightSystem.POUNDS
        )
        val expected = listOf(45.0, 85.0, 125.0, 165.0, 205.0)
        assertTrue(SchemeManager.getWeightScheme() == expected)
    }

    @Test
    fun createScheme_successfullyAddsValidPlateScheme(){
        SchemeManager.createScheme(
            45.0,
            205.0,
            5,
            Setting.GREEDY,
            WeightSystem.POUNDS
        )

        val mapLBStoZero: () -> MutableMap<Double, Int> = {
            Pounds().lbs.associateWith{ 0 }.toMutableMap()
        }

        val expected = listOf(
            mapLBStoZero(),
            mapLBStoZero().apply { putAll(mapOf(10.0 to 2)) },
            mapLBStoZero().apply { putAll(mapOf(35.0 to 1, 5.0 to 1)) },
            mapLBStoZero().apply { putAll(mapOf(55.0 to 1, 5.0 to 1)) },
            mapLBStoZero().apply { putAll(mapOf(55.0 to 1, 25.0 to 1)) }
        )
        val real = SchemeManager.getPlateScheme()
        assertTrue("Expected: $expected, Actual: $real", real == expected)
    }

    @Test
    fun createScheme_successfullyReplacesWeightScheme(){
        SchemeManager.createScheme(
            45.0,
            305.0,
            5,
            Setting.GREEDY,
            WeightSystem.POUNDS
        )
        SchemeManager.createScheme(
            45.0,
            205.0,
            5,
            Setting.GREEDY,
            WeightSystem.POUNDS
        )
        val expected = listOf(45.0, 85.0, 125.0, 165.0, 205.0)
        assertTrue(SchemeManager.getWeightScheme() == expected)
    }

    @Test
    fun createScheme_successfullyReplacesPlateScheme(){
        SchemeManager.createScheme(
            45.0,
            305.0,
            5,
            Setting.GREEDY,
            WeightSystem.POUNDS
        )
        SchemeManager.createScheme(
            45.0,
            205.0,
            5,
            Setting.GREEDY,
            WeightSystem.POUNDS
        )

        val mapLBStoZero: () -> MutableMap<Double, Int> = {
            Pounds().lbs.associateWith{ 0 }.toMutableMap()
        }

        val expected = listOf(
            mapLBStoZero(),
            mapLBStoZero().apply { putAll(mapOf(10.0 to 2)) },
            mapLBStoZero().apply { putAll(mapOf(35.0 to 1, 5.0 to 1)) },
            mapLBStoZero().apply { putAll(mapOf(55.0 to 1, 5.0 to 1)) },
            mapLBStoZero().apply { putAll(mapOf(55.0 to 1, 25.0 to 1)) }
        )
        assertTrue(SchemeManager.getPlateScheme() == expected)
    }

    @Test
    fun createGreedyPlateScheme_createsValidPlateScheme(){
        SchemeManager.createScheme(
            45.0,
            205.0,
            5,
            Setting.GREEDY,
            WeightSystem.POUNDS
        )
        val real = SchemeManager.createGreedyPlateScheme(
            SchemeManager.getWeightScheme(),
            WeightSystem.POUNDS
        )

        val mapLBStoZero: () -> MutableMap<Double, Int> = {
            Pounds().lbs.associateWith{ 0 }.toMutableMap()
        }

        val expected = listOf(
            mapLBStoZero(),
            mapLBStoZero().apply { putAll(mapOf(10.0 to 2)) },
            mapLBStoZero().apply { putAll(mapOf(35.0 to 1, 5.0 to 1)) },
            mapLBStoZero().apply { putAll(mapOf(55.0 to 1, 5.0 to 1)) },
            mapLBStoZero().apply { putAll(mapOf(55.0 to 1, 25.0 to 1)) }
        )

        assertTrue("Expected: $expected, Actual: $real", real == expected)
    }

    @Test
    fun greedyCoinChange_worksForValidSum(){
        val real = SchemeManager.greedyCoinChange(80.0, WeightSystem.POUNDS)
        val mapLBStoZero: () -> MutableMap<Double, Int> = {
            Pounds().lbs.associateWith{ 0 }.toMutableMap()
        }

        val expected = mapLBStoZero().apply { putAll(mapOf(55.0 to 1, 25.0 to 1)) }

        assertTrue("Expected: $expected, Actual: $real", real == expected)
    }
}
