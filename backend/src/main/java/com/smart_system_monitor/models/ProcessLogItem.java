package com.smart_system_monitor.models;

import java.util.Date;

/**
 * Class representing a process log entry
*/
public class ProcessLogItem {
    private Date date;
    private int pid;

    public ProcessLogItem(Date date, int pid){
        this.date = date;
        this.pid = pid;
    }

    public int getPid(){
        return pid;
    }

    public Date getDate(){
        return date;
    }
}
