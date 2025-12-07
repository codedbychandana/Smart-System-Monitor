package com.smart_system_monitor.services;

import java.io.IOException;
import java.util.Date;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.smart_system_monitor.helpers.IsolationForestManager;
import com.smart_system_monitor.models.ProcessLogItem;

import oshi.software.os.OSProcess;

@Service
public class OverloadReductionService {
    private final SystemMetricsService sysMetrics;

    private final MonitoringStatsService stats;

    private final IsolationForestManager isolationForestManager;

    // keeps track of cpu load readings
    private final Deque<Double> cpuLoadHistory;

    // max history dequeue size
    private final int HISTORY_MAX_SIZE;

    // user specified threshold
    private double threshold = 90;

    @Autowired
    public OverloadReductionService(SystemMetricsService sysMetrics, IsolationForestManager isolationForestManager, MonitoringStatsService stats){
        this.HISTORY_MAX_SIZE = 15;

        this.sysMetrics = sysMetrics;
        this.stats = stats;
        this.isolationForestManager = isolationForestManager;
        cpuLoadHistory = new LinkedList<>(
            sysMetrics.getProcesses(HISTORY_MAX_SIZE)
            .stream()
            .map(p -> p.getProcessCpuLoadCumulative())
            .toList()
        );
    }

    /**
     * monitors cpu overload and terminates detected overloaded process
    */
    public ProcessLogItem reduceCpuLoad(double threshold) throws IOException{
        if (monitorCpuLoad(threshold)){

            // terminate top process
            int pid = killTopProcess();

            // record overload prevented
            stats.recordOverloadPrevented();

            return new ProcessLogItem(new Date(), pid, "Process terminated");
        }
        // no overload detected
        return null;
    }

    /**
     * returns true if anomalous cpu overload present
    */
    public boolean monitorCpuLoad(double threshold){
        // get current cpu load
        double curCpuLoad = sysMetrics.getMetrics().get(0);
        
        // update user provided threshold
        setThreshold(threshold);

        // add to cpu history
        addCpuLoad(curCpuLoad);

        return isOverloaded() || predictOverloading();
    }

    /**
     * add cpu load reading to history
    */
    private void addCpuLoad(double cpuLoad){
        if (cpuLoadHistory.size() == HISTORY_MAX_SIZE){
            cpuLoadHistory.poll();
        }
        cpuLoadHistory.offer(cpuLoad);
    }

    /** 
     * returns true if cpu is overloaded without ml (only rule based) 
    */
    private boolean isOverloaded(){
        final double PERCENT = 0.7; 
        long countThreshold = Math.round(cpuLoadHistory.size() * PERCENT);

        // count how many records of cpu loads are too high
        long highLoadCount = cpuLoadHistory.stream()
        .filter(load -> load > threshold)
        .count();
        
        return highLoadCount >= countThreshold;
    }

    /** 
     * uses isolation forest to predict overloading possibility 
    */
    private boolean predictOverloading(){
        if (cpuLoadHistory.isEmpty()){
            return false;
        }
        double[] cpuLoad = new double[]{cpuLoadHistory.peekLast()};

        // use isolation forest model to see if anomaly detected based on user
        // provided threshold for cpu load
        return isolationForestManager.isAnomaly(cpuLoad, cpuLoadHistory);
    }

    /** 
     * kills process consuming the most cpu load
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

    /** 
     * sets user provided threshold
    */
    private void setThreshold(double threshold){
        this.threshold = Math.abs(threshold);
    }
}
