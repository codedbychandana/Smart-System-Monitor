package com.smart_system_monitor.controllers;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.smart_system_monitor.models.ProcessLogItem;
import com.smart_system_monitor.services.OverloadReductionService;

@RestController("/overload")
public class OverloadController {
    private final OverloadReductionService overloadReduction;
    private final Gson gson;

    public OverloadController(OverloadReductionService overloadReduction, Gson gson){
        this.overloadReduction = overloadReduction;
        this.gson = gson;
    }

    @GetMapping
    public ResponseEntity<String> monitorCpuOverload(){
        try{
            ProcessLogItem logItem = overloadReduction.monitorCpuLoad();
            return new ResponseEntity<>(gson.toJson(logItem), HttpStatus.OK);
        } catch (IOException e){
            System.out.println(e);
        }
        return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
