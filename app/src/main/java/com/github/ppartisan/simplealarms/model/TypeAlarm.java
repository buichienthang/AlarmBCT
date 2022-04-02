package com.github.ppartisan.simplealarms.model;

public class TypeAlarm {
    String typeTurnOffAlarm;
    int level;
    int times;

    public TypeAlarm(String typeTurnOffAlarm, int level, int times) {
        this.typeTurnOffAlarm = typeTurnOffAlarm;
        this.level = level;
        this.times = times;
    }

    public String getTypeTurnOffAlarm() {
        return typeTurnOffAlarm;
    }

    public void setTypeTurnOffAlarm(String typeTurnOffAlarm) {
        this.typeTurnOffAlarm = typeTurnOffAlarm;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }
}
