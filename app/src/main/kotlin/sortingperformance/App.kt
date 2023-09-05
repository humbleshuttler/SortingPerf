package sortingperformance

import io.opentelemetry.api.common.AttributeKey
import io.opentelemetry.api.common.Attributes
import java.util.concurrent.atomic.AtomicLong
import kotlin.system.measureTimeMillis
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import telemetry.OpenTelemetry

private val sortTimeMetrics =
    OpenTelemetry.getMeter()
        .histogramBuilder("sort_time")
        .setDescription("Time taken to sort")
        .setUnit("ms")
        .ofLongs()
        .build()

private val sortTimeMs: AtomicLong = AtomicLong()

fun main() {
  val totalTime = measureTimeMillis {
    val sorting = Sorting(100000)

    val job1 =
        GlobalScope.launch {
          println("running bubblesort...")
          performBenchmark(1, Attributes.of(AttributeKey.stringKey("algo"), "bubble_sort")) {
            sorting.bubbleSort(sorting.getValues().clone())
          }
        }

    val job2 =
        GlobalScope.launch {
          println("running insertion sort")
          performBenchmark(1, Attributes.of(AttributeKey.stringKey("algo"), "insertion_sort")) {
            sorting.insertionSort(sorting.getValues().clone())
          }
        }
    val job3 =
        GlobalScope.launch {
          println("running selection sort")
          performBenchmark(1, Attributes.of(AttributeKey.stringKey("algo"), "selection_sort")) {
            sorting.selectionSort(sorting.getValues().clone())
          }
        }

    runBlocking {
      println("waiting for all the jobs to finish...")
      job1.join()
      job2.join()
      job3.join()
    }
  }
  println("total time: $totalTime")
  OpenTelemetry.openTelemetrySdk.sdkMeterProvider.shutdown()
}

private fun performBenchmark(times: Int, attributes: Attributes, fn: () -> Unit) {
  val timeElapsed = measureTimeMillis { repeat(times) { fn() } }
  sortTimeMetrics.record(timeElapsed, attributes)
}

internal inline fun println(message: Any?) {
  kotlin.io.println("[${Thread.currentThread().name}]: $message")
}
