package com.smart_system_monitor.models;

import java.util.Date;

/**
 * Class representing a process log entry
*/
public class ProcessLogItem {
    private Date date;
    private int pid;
    private String actionTaken;

    public ProcessLogItem(Date date, int pid, String actionTaken){
        this.date = date;
        this.pid = pid;
        this.actionTaken = actionTaken;
    }

    public int getPid(){
        return pid;
    }

    public Date getDate(){
        return date;
    }

    public String getActionTaken(){
        return actionTaken;
    }
}
