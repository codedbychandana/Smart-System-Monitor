package com.smart_system_monitor.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import smile.anomaly.IsolationForest;

@Configuration
public class IsolationForestConfig {
    /**
     * loads existing model or creates new isolation forest model saving it
    */
    @Bean
    public IsolationForest isolationForestModel() throws ClassNotFoundException, IOException {
        String path = "src/main/resources/model/IsolationForest.bin";
        IsolationForest model = null;

        try{
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
            model = (IsolationForest) ois.readObject();
            ois.close();
        } catch (FileNotFoundException e){
            model = IsolationForest.fit(null);
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
            oos.writeObject(model);
            oos.close();
        }

        return model;
    }
}
