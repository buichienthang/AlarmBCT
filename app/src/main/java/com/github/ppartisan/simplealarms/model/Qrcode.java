package com.github.ppartisan.simplealarms.model;

public class Qrcode {

    int id;
    String nameQrcode;
    String qrcodeToText;

    public Qrcode(int id, String nameQrcode, String qrcodeToText) {
        this.id = id;
        this.nameQrcode = nameQrcode;
        this.qrcodeToText = qrcodeToText;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameQrcode() {
        return nameQrcode;
    }

    public void setNameQrcode(String nameQrcode) {
        this.nameQrcode = nameQrcode;
    }

    public String getQrcodeToText() {
        return qrcodeToText;
    }

    public void setQrcodeToText(String qrcodeToText) {
        this.qrcodeToText = qrcodeToText;
    }
}
