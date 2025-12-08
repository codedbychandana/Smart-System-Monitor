package com.smart_system_monitor.services;

import org.springframework.stereotype.Service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;

@Service
public class MonitoringStatsService {
    private final Counter metricsCounter;
    private final Counter anomalyCounter;
    private final Counter trueAnomalyCounter;
    private final Counter overloadPreventedCounter;
    private final Gauge anomalyAccuracy;

    public MonitoringStatsService(MeterRegistry registry){
        metricsCounter = Counter.builder("total_metrics")
        .description("total metrics counted")
        .register(registry);

        anomalyCounter = Counter.builder("total_anomalies")
        .description("total anomalies counted")
        .register(registry);

        trueAnomalyCounter = Counter.builder("total_true_anomalies")
        .description("total true anomalies counted")
        .register(registry);

        overloadPreventedCounter = Counter.builder("total_overloads_prevented")
        .description("total cpu overloads prevented")
        .register(registry);

        anomalyAccuracy = Gauge.builder("isolation_forest_accuracy", this::getAnomalyAccuracy)
        .description("anomaly detection accuracy")
        .register(registry);
        
    }

    public void recordMetric() {
        metricsCounter.increment();
    }

    public void recordAnomaly() {
        anomalyCounter.increment();
    }

    public void recordTrueAnomaly(){
        trueAnomalyCounter.increment();
    }

    public void recordOverloadPrevented() {
        overloadPreventedCounter.increment();
    }

    public double getAnomalyAccuracy() {
        if (anomalyCounter.count() == 0) return 0;
        return (trueAnomalyCounter.count() / anomalyCounter.count());
    }
}
