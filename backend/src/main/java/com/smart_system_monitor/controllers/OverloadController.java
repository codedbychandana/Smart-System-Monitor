package com.smart_system_monitor.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
     * api endpoint to monitor cpu load using isolation forest
    */
    @GetMapping("/monitor")
    public ResponseEntity<Map<String, ProcessLogItem>> monitorCpuOverload(){
        try{
            ProcessLogItem logItem = overloadReduction.monitorCpuLoad();
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
