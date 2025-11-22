package com.smart_system_monitor.helpers;

import smile.anomaly.IsolationForest;

public class IsolationForestManager{
    private IsolationForest model;

    /**
     * get threshold based on cpu readings
    */
    private double getThreshold(double[] data, double percentile){
        int n = data.length;
        int index = (int) Math.ceil(percentile/100 * n) - 1;
        return data[index];
    }

    private void train(double[][] data){
        ModelSerializer.
    }
}
