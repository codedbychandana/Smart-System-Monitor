package com.smart_system_monitor.helpers;

import smile.anomaly.IsolationForest;

public class IsolationForestManager {
    private IsolationForest model;
    
    private double getThreshold(double[] data, double percentile){
        int n = data.length;
        int index = (int) Math.ceil(percentile/100 * n) - 1;
        return data[index];
    }
}
