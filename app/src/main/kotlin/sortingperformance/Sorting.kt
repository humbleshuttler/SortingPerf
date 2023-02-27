package sortingperformance

import java.util.*

class Sorting(count: Int) {

    private val random = Random(System.currentTimeMillis())
    private val values: Array<Int> = Array(count) { random.nextInt() }

    fun getValues(): Array<Int> = this.values

    fun bubbleSort(input: Array<Int>) {
        println("Bubble sort started...")
        for (i in input.indices) {
            for (j in 1 until input.size) {
                if (input[j - 1] > input[j]) {
                    swap(input, j - 1, j)
                }
            }
        }
        println("Bubble sort complete")
    }

    fun insertionSort(input: Array<Int>) {
        println("insertion sort started")
        for (i in input.indices) {
            val curr = input[i]
            var j = i - 1
            while (j >= 0 && curr < input[j]) {
                input[j + 1] = input[j]
                j--
            }
            input[j + 1] = curr
        }
        println("Insertion sort completed")
    }

    private fun swap(input: Array<Int>, i: Int, j: Int) {
        val k = input[i]
        input[i] = input[j]
        input[j] = k
    }
}