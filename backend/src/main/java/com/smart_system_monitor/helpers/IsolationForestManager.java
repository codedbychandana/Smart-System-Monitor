package com.smart_system_monitor.helpers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smart_system_monitor.config.IsolationForestConfig;

import smile.anomaly.IsolationForest;

@Component
public class IsolationForestManager{
    @Autowired
    private IsolationForest model;

    @Autowired
    private IsolationForestConfig modelConfig;

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
    public boolean isAnomaly(double[] sample, double[] data){
        double percentile = 85;
        
        return model.score(sample) * 100 > percentile;
    }

    /**
     * train the model
    */
    public void train(double[][] data) throws IOException, FileNotFoundException{
        model = IsolationForest.fit(data);
        modelConfig.save(model);
    }
}
