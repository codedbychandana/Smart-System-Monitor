package com.smart_system_monitor.helpers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smart_system_monitor.config.IsolationForestConfig;
import com.smart_system_monitor.services.MonitoringStatsService;

import smile.anomaly.IsolationForest;

@Component
public class IsolationForestManager{
    
    private IsolationForest model;

    private IsolationForestConfig modelConfig;

    private final MonitoringStatsService stats;

    private IsolationForestManager(IsolationForest model, IsolationForestConfig modelConfig, MonitoringStatsService stats){
        this.model = model;
        this.modelConfig = modelConfig;
        this.stats = stats;
    }

    /**
     * returns model threshold for scores 
    */
    private double getScoreThreshold(List<Double> data, double percentile){
        // compute score for each data point
        List<Double> scores = data.stream()
        .map(load -> model.score(new double[]{load}))
        .collect(Collectors.toList());

        scores.sort((a, b) -> Double.compare(a, b));
        int n = scores.size();

        // compute score threshold
        int index = (int) Math.ceil(percentile/100 * n) - 1;
        return scores.get(index);
    }

    /** 
     * return true if anomaly detected 
    */
    public boolean isAnomaly(double[] sample, List<Double> data, double threshold){
        double percentile = 95;

        // check if isolation forest model detects anomaly
        boolean anomalyDetected = model.score(sample) * 100 > getScoreThreshold(data, percentile);
        
        // check if true anomaly based on user threshold
        boolean trueAnomaly = sample[0] > threshold;
        
        // record anomaly detected
        if (anomalyDetected){
            stats.recordAnomaly();
        }
        
        // record true anomaly detected
        if (trueAnomaly){
            stats.recordTrueAnomaly();
        }
        
        System.out.println("sample: " + sample[0] + "model score: " + model.score(sample));
        System.out.println("true: " + trueAnomaly + " detected: " + anomalyDetected);
        
        // anomaly detected only if both results are true
        return trueAnomaly && anomalyDetected;
    }

    /**
     * train the model
    */
    public void train(double[][] data) throws IOException, FileNotFoundException{
        model = IsolationForest.fit(data);
        modelConfig.save(model);
    }
}
