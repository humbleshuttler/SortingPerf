package sortingperformance

import com.jakewharton.fliptables.FlipTable
import kotlin.system.measureTimeMillis
import kotlinx.coroutines.*

fun main() {
  val totalTime = measureTimeMillis {
    val sorting = Sorting(100000)
    val header: MutableList<String> = mutableListOf()
    val timeValues: MutableList<Long> = mutableListOf()

    val job1 =
        GlobalScope.launch {
          println("running bubblesort...")
          performBenchmark(1, header, timeValues, "BubbleSort") {
            sorting.bubbleSort(sorting.getValues().clone())
          }
        }

    val job2 =
        GlobalScope.launch {
          println("running insertion sort")
          performBenchmark(1, header, timeValues, "InsertionSort") {
            sorting.insertionSort(sorting.getValues().clone())
          }
        }

    runBlocking {
      println("waiting for all the jobs to finish...")
      job1.join()
      job2.join()
    }

    printPerformance(timeValues, header)
  }
  println("total time: $totalTime")
}

private fun printPerformance(timeValues: List<Long>, header: List<String>) {
  val arr2D = Array(1) { Array(timeValues.size) { "" } }
  for (i in timeValues.indices) {
    arr2D[0][i] = timeValues[i].toString()
  }

  kotlin.io.println(FlipTable.of(header.toTypedArray(), arr2D))
}

private fun performBenchmark(
    times: Int,
    headers: MutableList<String>,
    values: MutableList<Long>,
    header: String,
    fn: () -> Unit
) {
  var timeElapsed = 0L
  timeElapsed = measureTimeMillis { repeat(times) { fn() } }
  headers.add(header)
  values.add(timeElapsed)
}

internal inline fun println(message: Any?) {
  kotlin.io.println("[${Thread.currentThread().name}]: $message")
}
