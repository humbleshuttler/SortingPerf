package sortingperformance

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SortVerifier {

  fun verify(algoName: String, values: Array<Int>) {
    val scope = CoroutineScope(Dispatchers.Default)
    scope.launch {
      val isSorted = values.asSequence().zipWithNext().all { (a, b) -> a <= b }
      println("$algoName isSorted = $isSorted")
    }
  }
}
