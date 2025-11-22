package com.smart_system_monitor.helpers;

import org.springframework.beans.factory.annotation.Autowired;

import smile.anomaly.IsolationForest;

public class IsolationForestManager{
    @Autowired
    private IsolationForest model;

    /**
     * get threshold based on cpu readings
    */
    private double getThreshold(double[] data, double percentile){
        int n = data.length;
        int index = (int) Math.ceil(percentile/100 * n) - 1;
        return data[index];
    }

    /** 
     * return true if anomaly detected 
    */
    public boolean isAnomaly(double[] samples){
        double percentile = 95;
        return model.score(samples) > getThreshold(samples, percentile);
    }
}
