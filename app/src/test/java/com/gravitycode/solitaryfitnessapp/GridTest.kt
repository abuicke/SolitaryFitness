package com.gravitycode.solitaryfitnessapp

import com.gravitycode.solitaryfitnessapp.util.ui.compose.calculateGridRows
import junit.framework.TestCase.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class GridTest {

    @Test
    fun `test calculateGridRows(Int, Int) helper function throws exception when columns less than one`() {
        assertThrows("columns must be at least 1", IllegalArgumentException::class.java) {
            calculateGridRows(columns = 0, items = 9)
        }
        assertThrows("columns must be at least 1", IllegalArgumentException::class.java) {
            calculateGridRows(columns = -1, items = 9)
        }
        assertThrows("columns must be at least 1", IllegalArgumentException::class.java) {
            calculateGridRows(columns = -10, items = 9)
        }
    }

    @Test
    fun `test calculateGridRows(Int, Int) helper function throws exception when items less than one`() {
        assertThrows("items must be at least 1", IllegalArgumentException::class.java) {
            calculateGridRows(columns = 3, items = 0)
        }
        assertThrows("items must be at least 1", IllegalArgumentException::class.java) {
            calculateGridRows(columns = 3, items = -1)
        }
        assertThrows("items must be at least 1", IllegalArgumentException::class.java) {
            calculateGridRows(columns = 3, items = -10)
        }
    }

    @Test
    fun `test calculateGridRows(Int, Int) helper function gives correct answers`() {
        assertEquals(3, calculateGridRows(columns = 3, items = 9))
        assertEquals(3, calculateGridRows(columns = 3, items = 8))
        assertEquals(3, calculateGridRows(columns = 3, items = 7))
        assertEquals(2, calculateGridRows(columns = 3, items = 6))
        assertEquals(2, calculateGridRows(columns = 3, items = 5))
        assertEquals(2, calculateGridRows(columns = 3, items = 4))
        assertEquals(1, calculateGridRows(columns = 3, items = 3))
        assertEquals(1, calculateGridRows(columns = 3, items = 2))
        assertEquals(1, calculateGridRows(columns = 3, items = 1))
    }
}