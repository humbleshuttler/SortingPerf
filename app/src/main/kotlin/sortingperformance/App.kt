package sortingperformance

import io.prometheus.client.Gauge
import io.prometheus.client.Histogram
import kotlinx.coroutines.*
import telemetry.Prometheus
import telemetry.Prometheus.registry
import kotlin.math.pow
import kotlin.system.measureTimeMillis

private val sortTimeMetrics: Gauge =
    Gauge.build("sort_time", "Time taken to sort the elements").labelNames("algo", "size").register(registry)
private val sortTimeHist: Histogram =
    Histogram.build("sort_time_hist", "Histogram to track sort time").labelNames("algo", "size").register(
        registry
    )

private val powers = (1..5)

fun main() {
    repeat(100) {
        sortingperformance.println("running iteration: $it")
        mainRun()
    }
}

fun mainRun() {
    val totalTime = measureTimeMillis {
        runBlocking {
            for (power in powers) {
                async {
                    val elements = 10.0.pow(power)
                    println("Starting sort for $elements")
                    runSorting(elements.toInt())
                    println("completed sorting for $elements")
                }
            }
        }
    }
    println("total time: $totalTime")
    Prometheus.pg.push(registry, "sorting-performance")
}

private suspend fun runSorting(elementCount: Int) {
    val sorting = Sorting(elementCount)
    coroutineScope {
        println("running bubblesort...")
        launch {
            performBenchmark(1, "bubble_sort", elementCount) {
                sorting.bubbleSort(sorting.getValues().clone())
            }
        }

        println("running insertion sort")
        launch {
            performBenchmark(1, "insertion_sort", elementCount) {
                sorting.insertionSort(sorting.getValues().clone())
            }
        }
        println("running selection sort")
        launch {
            performBenchmark(1, "selection_sort", elementCount) {
                sorting.selectionSort(sorting.getValues().clone())
            }
        }
        println("waiting for all the jobs to finish...")
    }
}

private suspend fun performBenchmark(times: Int, algoLabelValue: String, size: Int, fn: () -> Unit) {
    coroutineScope {
        val timer = sortTimeHist.labels(algoLabelValue, size.toString()).startTimer()
        repeat(times) { fn() }
        timer.close()
    }
}

internal fun println(message: Any?) {
    kotlin.io.println("[${Thread.currentThread().name}]: $message")
}
