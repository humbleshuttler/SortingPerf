package telemetry

import io.prometheus.client.CollectorRegistry
import io.prometheus.client.exporter.PushGateway

private const val PUSH_GATEWAY_URL = "127.0.0.1:9091"

public object Prometheus {
    val pg = PushGateway(PUSH_GATEWAY_URL)
    val registry = CollectorRegistry()
}
