package com.smart_system_monitor.services;

import java.util.LinkedList;
import java.util.Queue;

import org.springframework.stereotype.Service;

import oshi.SystemInfo;
import oshi.software.os.OperatingSystem;

@Service
public class OverloadReductionService {
    private SystemMetricsService sysMetricsService;
    private final OperatingSystem OS;
    // keeps track of cpu load readings
    private final Queue<Double> cpuLoadHistory;

    public OverloadReductionService(SystemMetricsService sysMetricsService){
        this.sysMetricsService = sysMetricsService;
        OS = new SystemInfo().getOperatingSystem();
        cpuLoadHistory = new LinkedList<>();
    }

    /**
     * add cpu load reading to queue
    */
    private void addCpuLoad(double cpuLoad){
        final int MAX_SIZE = 5;

        if (cpuLoadHistory.size() == MAX_SIZE){
            cpuLoadHistory.poll();
        }
        cpuLoadHistory.offer(cpuLoad);
    }

    /** returns true if cpu is overloaded 
    */
    private boolean isOverloaded(){
        return cpuLoadHistory.stream()
        .allMatch(load -> load > 80);
    }

    /** uses isolation forest to predict overloading possibility 
    */
    private boolean predictOverloading(){
        /* stub */
        return false;
    }
}
