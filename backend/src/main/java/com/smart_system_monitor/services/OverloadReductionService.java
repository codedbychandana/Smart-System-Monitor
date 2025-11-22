package com.smart_system_monitor.services;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.springframework.stereotype.Service;

import oshi.SystemInfo;
import oshi.software.os.OperatingSystem;
import oshi.software.os.OperatingSystem.ProcessSorting;
import oshi.software.os.OSProcess;

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

    /** kills process consuming the most cpu load
    */
    private void killTopProcess() throws IOException{
        // get top 5 running processes in order of cpu usage
        List<OSProcess> processes = OS.getProcesses(null, (p1, p2) -> Double.compare(p2.getProcessCpuLoadCumulative(), p1.getProcessCpuLoadCumulative()), 5);
        if (processes.isEmpty()){
            return;
        }
        int pid = processes.get(0).getProcessID();
        
        // kill top process
        Runtime.getRuntime().exec("taskkill /PID " + pid + "/F");
        System.out.println("Process " + pid + " has been terminated.");
    }
}
