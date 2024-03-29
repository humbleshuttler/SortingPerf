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

  fun selectionSort(input: Array<Int>) {
    println("selection sort started")
    for (i in input.indices) {
      var low = i
      for (j in i + 1 until input.size) {
        if (input[j] < input[low]) {
          low = j
        }
      }
      if (input[low] < input[i]) {
        swap(input, i, low)
      }
    }
    println("Selection sort completed")
  }

  private fun swap(input: Array<Int>, i: Int, j: Int) {
    input[i] = input[i] xor input[j]
    input[j] = input[i] xor input[j]
    input[i] = input[i] xor input[j]
  }
}
