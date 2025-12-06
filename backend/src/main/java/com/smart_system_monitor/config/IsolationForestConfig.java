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
    private final String path = "src/main/resources/model/IsolationForest.bin";

    /**
     * loads existing model or creates new isolation forest model saving it
    */
    @Bean
    public IsolationForest isolationForestModel(SystemMetricsService sysMetricsService) throws ClassNotFoundException, IOException {
        IsolationForest model = null;

        try{
            model = load();
        } catch (FileNotFoundException e){
            final int SAMPLE_COUNT = 5;

            // fetch initial sample loads
            double[][] data = new double[SAMPLE_COUNT][];
            
            // convert to 2d double array
            data = sysMetricsService.getProcesses(SAMPLE_COUNT)
            .stream()
            .map(p -> new double[]{p.getProcessCpuLoadCumulative() * 100})
            .toArray(double[][]::new);

            // create model and serialize
            model = IsolationForest.fit(data);
            save(model);
        }

        return model;
    }

    /**
     * deserializer for trained model
    */
    private IsolationForest load() throws ClassNotFoundException, FileNotFoundException, IOException{
        IsolationForest model = null;
        // for test
        File file = new File(path);
        file.delete();


        // load model if exists
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
        model = (IsolationForest) ois.readObject();

        ois.close();

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
