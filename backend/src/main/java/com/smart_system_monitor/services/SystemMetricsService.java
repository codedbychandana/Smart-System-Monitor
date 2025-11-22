package com.smart_system_monitor.services;

import org.springframework.stereotype.Service;

import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;

@Service
public class SystemMetricsService {
    private final SystemInfo sysInfo;
    private final HardwareAbstractionLayer HAL;

    public SystemMetricsService(){
        sysInfo = new SystemInfo();
        HAL = sysInfo.getHardware();
    }


}
