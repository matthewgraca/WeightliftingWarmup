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

    /** validate input tests **/
    @Test
    fun validateInputs_validInputisValid(){
        val real = SchemeManager.validateInputs(
            45.0,
            205.0,
            5,
            Setting.GREEDY,
            WeightSystem.POUNDS
        )
        val expected = "" 

        assertTrue("Expected: $expected, Actual: $real", real == expected)
    }

    @Test
    fun validateInputs_negativeStartWeight(){
        val real = SchemeManager.validateInputs(
            -1.0,
            205.0,
            5,
            Setting.GREEDY,
            WeightSystem.POUNDS
        )
        val expected = "Start weight must be non-negative" 

        assertTrue("Expected: $expected, Actual: $real", real == expected)
    }

    @Test
    fun validateInputs_nullStartWeight(){
        val real = SchemeManager.validateInputs(
            null,
            205.0,
            5,
            Setting.GREEDY,
            WeightSystem.POUNDS
        )
        val expected = "Start weight must be non-negative" 

        assertTrue("Expected: $expected, Actual: $real", real == expected)
    }

    @Test
    fun validateInputs_endWeightLessThanStartWeight(){
        val real = SchemeManager.validateInputs(
            45.0,
            44.0,
            5,
            Setting.GREEDY,
            WeightSystem.POUNDS
        )
        val expected = "End weight cannot be less than start weight" 

        assertTrue("Expected: $expected, Actual: $real", real == expected)
    } 

    @Test
    fun validateInputs_nullEndWeight(){
        val real = SchemeManager.validateInputs(
            45.0,
            null,
            5,
            Setting.GREEDY,
            WeightSystem.POUNDS
        )
        val expected = "End weight cannot be less than start weight" 

        assertTrue("Expected: $expected, Actual: $real", real == expected)
    }

    @Test
    fun validateInputs_validNumSets(){
        val real = SchemeManager.validateInputs(
            45.0,
            205.0,
            5,
            Setting.GREEDY,
            WeightSystem.POUNDS
        )
        val expected = "" 

        assertTrue("Expected: $expected, Actual: $real", real == expected)
    }

    @Test
    fun validateInputs_nullNumSets(){
        val real = SchemeManager.validateInputs(
            45.0,
            205.0,
            null,
            Setting.GREEDY,
            WeightSystem.POUNDS
        )
        val expected = "Number of sets must be positive" 

        assertTrue("Expected: $expected, Actual: $real", real == expected)
    }

    @Test
    fun validateInputs_zeroNumSets(){
        val real = SchemeManager.validateInputs(
            45.0,
            205.0,
            0,
            Setting.GREEDY,
            WeightSystem.POUNDS
        )
        val expected = "Number of sets must be positive" 

        assertTrue("Expected: $expected, Actual: $real", real == expected)
    }

    @Test
    fun validateSolvability_isSolvable(){
        val real = SchemeManager.validateInputs(
            45.0,
            205.0,
            5,
            Setting.GREEDY,
            WeightSystem.POUNDS
        )
        val expected = "" 

        assertTrue("Expected: $expected, Actual: $real", real == expected)
    }

    @Test
    fun validateSolvability_weightHasNoSolution(){
        val real = SchemeManager.validateInputs(
            45.0,
            205.234,
            5,
            Setting.GREEDY,
            WeightSystem.POUNDS
        )
        val expected = "A solution is not possible with the weights available" 

        assertTrue("Expected: $expected, Actual: $real", real == expected)
    }

    @Test
    fun validateSolvability_weightIncrementsHaveNoSolution(){
        val real = SchemeManager.validateInputs(
            45.0,
            205.0,
            7,
            Setting.GREEDY,
            WeightSystem.POUNDS
        )
        val expected = "A solution is not possible with the number of sets given" 

        assertTrue("Expected: $expected, Actual: $real", real == expected)
    }
}
