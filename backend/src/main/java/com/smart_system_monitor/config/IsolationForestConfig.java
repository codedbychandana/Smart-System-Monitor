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
    // saved model path
    private final String path = "src/main/resources/model/IsolationForest.bin";

    /**
     * loads existing model or creates new isolation forest model saving it
    */
    @Bean
    public IsolationForest isolationForestModel() throws ClassNotFoundException, IOException {
        IsolationForest model = null;

        try{
            model = load();
        } catch (FileNotFoundException e){
            // create model and serialize
            model = IsolationForest.fit(null);
            save(model);
        }

        return model;
    }

    /**
     * deserializer for trained model
    */
    private IsolationForest load() throws ClassNotFoundException, FileNotFoundException, IOException{
        IsolationForest model = null;

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
