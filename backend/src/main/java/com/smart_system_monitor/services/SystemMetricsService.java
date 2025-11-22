package com.smart_system_monitor.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;

@Service
public class SystemMetricsService {
    private final SystemInfo sysInfo;
    private final HardwareAbstractionLayer HAL;

    public SystemMetricsService(){
        sysInfo = new SystemInfo();
        HAL = sysInfo.getHardware();
    }

    /** 
     * returns system metrics as a list including cpu load and percentage of memory in use
    */
    public List<Double> getMetrics(){
        // the result
        List<Double> metrics  = new ArrayList<>();

        CentralProcessor processor = HAL.getProcessor();
        GlobalMemory memory = HAL.getMemory();

        // calculates cpu load and ram used as percentages
        double cpuLoad = processor.getSystemCpuLoad(1000) * 100;
        double ramUsed = ((memory.getTotal() - memory.getAvailable()) / memory.getTotal()) * 100;
        
        metrics.add(cpuLoad);
        metrics.add(ramUsed);
        return metrics;
    }
}
