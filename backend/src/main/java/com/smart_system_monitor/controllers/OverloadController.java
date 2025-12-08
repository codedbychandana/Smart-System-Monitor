package com.smart_system_monitor.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.smart_system_monitor.models.ProcessLogItem;
import com.smart_system_monitor.services.OverloadReductionService;

@RestController
@RequestMapping("/overload")
public class OverloadController {
    private final OverloadReductionService overloadReduction;

    public OverloadController(OverloadReductionService overloadReduction){
        this.overloadReduction = overloadReduction;
    }

    /**
     * api endpoint to check if cpu is overloaded
    */
    @PostMapping("/monitor")
    public ResponseEntity<Map<String, Boolean>> monitor(@RequestBody Map<String, Double> body){
        boolean isOverloaded = overloadReduction.monitorCpuLoad(body.get("threshold"));
        
        // response
        Map<String, Boolean> res = new HashMap<>();
        res.put("overloaded", isOverloaded);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }  

    /**
     * terminates anomalous cpu process when overload detected
    */
    @PostMapping("/reduceLoad")
    public ResponseEntity<Map<String, ProcessLogItem>> reduceLoad(@RequestBody Map<String, Double> body){
        try{
            ProcessLogItem logItem = overloadReduction.reduceCpuLoad(body.get("threshold"));
            // response
            Map<String, ProcessLogItem> payload = new HashMap<>();
            payload.put("processRemoved", logItem);

            return new ResponseEntity<>(payload, HttpStatus.OK);
        } catch (IOException e){
            System.out.println(e);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
