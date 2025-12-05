package com.smart_system_monitor.services;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.smart_system_monitor.helpers.IsolationForestManager;
import com.smart_system_monitor.models.ProcessLogItem;

@Service
public class OverloadReductionService {
    private final SystemMetricsService sysMetrics;

    private final IsolationForestManager isolationForestManager;

    // keeps track of cpu load readings
    private final Queue<Double> cpuLoadHistory;

    @Autowired
    public OverloadReductionService(SystemMetricsService sysMetrics, IsolationForestManager isolationForestManager){
        this.sysMetrics = sysMetrics;
        this.isolationForestManager = isolationForestManager;
        cpuLoadHistory = new LinkedList<>();
    }

    /**
     * reduce overload and monitor every 2 seconds
    */
    public ProcessLogItem monitorCpuLoad() throws IOException{
        double curCpuLoad = sysMetrics.getMetrics().get(0);
        // add to cpu history
        addCpuLoad(curCpuLoad);

        // reduce overload
        if (isOverloaded() || predictOverloading()){
            int pid = killTopProcess();
            return new ProcessLogItem(new Date(), pid, "Process terminated");
        }
        return null;
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
        if (cpuLoadHistory.isEmpty()){
            return false;
        }
        double[] cpuLoad = new double[]{cpuLoadHistory.peek()};

        return isolationForestManager.isAnomaly(cpuLoad, cpuLoadHistory.stream()
            .mapToDouble(Double::valueOf)
            .toArray()
        );
    }

    /** kills process consuming the most cpu load
    */
    private int killTopProcess() throws IOException{
        // get top 5 running processes in order of cpu usage
        List<OSProcess> processes = sysMetrics.getProcesses(5);
        
        if (processes.isEmpty()){
            return -1;
        }
        int pid = processes.get(0).getProcessID();
        
        // kill top process
        Runtime.getRuntime().exec("taskkill /PID " + pid + " /F");
        System.out.println("Process " + pid + " has been terminated.");
        return pid;
    }
}
