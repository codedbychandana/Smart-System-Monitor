package com.smart_system_monitor.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smart_system_monitor.services.SystemMetricsService;

@RestController
@RequestMapping("/metrics")
public class MetricsController {
    @Autowired
    private SystemMetricsService sysMetricsService;

    /** 
     * returns system metrics as JSON response
    */
    @GetMapping
    public ResponseEntity<List<Double>> getMetrics(){
        List<Double> metrics = sysMetricsService.getMetrics();
        return new ResponseEntity<>(metrics, HttpStatus.OK);
    }
}
