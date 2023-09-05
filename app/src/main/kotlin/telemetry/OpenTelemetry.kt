package telemetry

import io.opentelemetry.api.common.Attributes
import io.opentelemetry.api.metrics.Meter
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator
import io.opentelemetry.context.propagation.ContextPropagators
import io.opentelemetry.exporter.otlp.metrics.OtlpGrpcMetricExporter
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter
import io.opentelemetry.sdk.OpenTelemetrySdk
import io.opentelemetry.sdk.metrics.SdkMeterProvider
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader
import io.opentelemetry.sdk.resources.Resource
import io.opentelemetry.sdk.trace.SdkTracerProvider
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor
import io.opentelemetry.semconv.resource.attributes.ResourceAttributes
import java.time.Duration

private const val SCOPE = "Sorting-performance"
private const val EXPORTER_ENDPOINT = "http://127.0.0.1:4317"

public object OpenTelemetry {

  var openTelemetrySdk: OpenTelemetrySdk
  init {
    val resource: Resource =
        Resource.getDefault()
            .merge(Resource.create(Attributes.of(ResourceAttributes.SERVICE_NAME, SCOPE)))

    val sdkTracerProvider: SdkTracerProvider =
        SdkTracerProvider.builder()
            .addSpanProcessor(
                BatchSpanProcessor.builder(
                        OtlpGrpcSpanExporter.builder().setEndpoint(EXPORTER_ENDPOINT).build())
                    .build())
            .setResource(resource)
            .build()

    val sdkMeterProvider: SdkMeterProvider =
        SdkMeterProvider.builder()
            .registerMetricReader(
                PeriodicMetricReader.builder(
                        OtlpGrpcMetricExporter.builder().setEndpoint(EXPORTER_ENDPOINT).build())
                    .setInterval(Duration.ofSeconds(5))
                    .build())
            .setResource(resource)
            .build()

    openTelemetrySdk =
        OpenTelemetrySdk.builder()
            .setTracerProvider(sdkTracerProvider)
            .setMeterProvider(sdkMeterProvider)
            .setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
            .build()
  }

  fun getMeter(): Meter {
    return openTelemetrySdk.meterBuilder(SCOPE).setInstrumentationVersion("1.0.0").build()
  }
}
