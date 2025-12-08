package com.smart_system_monitor.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import com.smart_system_monitor.services.SystemMetricsService;

import smile.anomaly.IsolationForest;

@Configuration
public class IsolationForestConfig {
    // saved model path
    private final String path = "src/main/java/com/smart_system_monitor/models/IsolationForest.bin";

    private final SystemMetricsService sysMetrics;

    public IsolationForestConfig(SystemMetricsService sysMetricsService){
        this.sysMetrics = sysMetricsService;
    }

    /**
     * returns new isolation forest model 
    */
    @Bean
    public IsolationForest isolationForestModel() throws ClassNotFoundException, IOException {
        return load();
    }

    /**
     * returns a pre-trained isolation forest model using current cpu loads
    */
    private IsolationForest load() throws ClassNotFoundException, FileNotFoundException, IOException{
        IsolationForest model = null;

        final int SAMPLE_COUNT = 15;

        double[][] data = new double[SAMPLE_COUNT][];

        // fetch initial sample loads
        for (int i = 0; i<SAMPLE_COUNT; i++){
            data[i] = new double[]{sysMetrics.getMetrics().get(0)};
        }

        // create & train model
        model = IsolationForest.fit(data);
 
        return model;
    }

    /** 
     * serializer for model
    */
    public void save(IsolationForest model) throws FileNotFoundException, IOException{
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
        oos.writeObject(model);
        oos.close();
    }
}
