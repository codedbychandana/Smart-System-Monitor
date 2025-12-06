package com.smart_system_monitor.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;
import oshi.software.os.OperatingSystem.ProcessSorting;
import oshi.software.os.OSProcess;

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
        double ramUsed = (((double) memory.getTotal() - memory.getAvailable()) / memory.getTotal()) * 100;
        
        metrics.add(cpuLoad);
        metrics.add(ramUsed);
        return metrics;
    }

    /**
     * returns list of top n processes (descending order of cpu load consumption)
    */
    public List<OSProcess> getProcesses(int n){
        OperatingSystem OS = sysInfo.getOperatingSystem();
        List<OSProcess> res = OS.getProcesses(null, OperatingSystem.ProcessSorting.CPU_DESC, n);
        for (OSProcess p : res){
            System.out.println(p.getProcessCpuLoadCumulative());
        }
            
        return res;
    }
}
