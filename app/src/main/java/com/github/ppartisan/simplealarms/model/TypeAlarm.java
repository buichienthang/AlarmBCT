package com.github.ppartisan.simplealarms.model;

public class TypeAlarm {
    String typeTurnOffAlarm;
    int level, times, idQrcode;
    String typeWrite;

    public TypeAlarm(String typeTurnOffAlarm, int level, int times, int idQrcode, String typeWrite) {
        this.typeTurnOffAlarm = typeTurnOffAlarm;
        this.level = level;
        this.times = times;
        this.idQrcode = idQrcode;
        this.typeWrite = typeWrite;
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

    public int getIdQrcode() {
        return idQrcode;
    }

    public void setIdQrcode(int idQrcode) {
        this.idQrcode = idQrcode;
    }

    public String getTypeWrite() {
        return typeWrite;
    }

    public void setTypeWrite(String typeWrite) {
        this.typeWrite = typeWrite;
    }
}
