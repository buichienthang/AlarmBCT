package com.github.ppartisan.simplealarms.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TypeAlarm implements Parcelable {
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

    protected TypeAlarm(Parcel in) {
        typeTurnOffAlarm = in.readString();
        level = in.readInt();
        times = in.readInt();
        idQrcode = in.readInt();
        typeWrite = in.readString();
    }

    public static final Creator<TypeAlarm> CREATOR = new Creator<TypeAlarm>() {
        @Override
        public TypeAlarm createFromParcel(Parcel in) {
            return new TypeAlarm(in);
        }

        @Override
        public TypeAlarm[] newArray(int size) {
            return new TypeAlarm[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(typeTurnOffAlarm);
        parcel.writeInt(level);
        parcel.writeInt(times);
        parcel.writeInt(idQrcode);
        parcel.writeString(typeWrite);
    }
}
