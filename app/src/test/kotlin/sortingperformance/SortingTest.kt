package sortingperformance

import org.junit.Test
import kotlin.test.assertTrue

class SortingTest {

    @Test
    fun selectionSort() {
        val sorting = Sorting(10000)
        val values = sorting.getValues()
        sorting.selectionSort(values)
        assertTrue { values.asSequence().zipWithNext().all { (a, b) -> a <= b } }
    }

    @Test
    fun bubbleSort() {
        val sorting = Sorting(10000)
        val values = sorting.getValues()
        sorting.bubbleSort(values)
        assertTrue { values.asSequence().zipWithNext().all { (a, b) -> a <= b } }
    }
}