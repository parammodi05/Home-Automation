package com.example.hostelautomation;

public class Act {
    int Appimage;
    String Apptext;
    boolean isChecked1;
    boolean isChecked2;
    String codeon;
    String codeoff;

    public Act(int appimage, String apptext, boolean isChecked1, boolean isChecked2,String codeon, String codeoff) {
        Appimage = appimage;
        Apptext = apptext;
        this.isChecked1 = isChecked1;
        this.isChecked2 = isChecked2;
        this.codeon=codeon;
        this.codeoff=codeoff;
    }

    public int getAppimage() {
        return Appimage;
    }

    public void setAppimage(int appimage) {
        Appimage = appimage;
    }

    public String getApptext() {
        return Apptext;
    }

    public void setApptext(String apptext) {
        Apptext = apptext;
    }

    public boolean isChecked1() {
        return isChecked1;
    }

    public void setChecked1(boolean checked1) {
        isChecked1 = checked1;
    }

    public boolean isChecked2() {
        return isChecked2;
    }

    public void setChecked2(boolean checked2) {
        isChecked2 = checked2;
    }

    public String getCodeon() {
        return codeon;
    }

    public void setCodeon(String codeon) {
        this.codeon = codeon;
    }

    public String getCodeoff() {
        return codeoff;
    }

    public void setCodeoff(String codeoff) {
        this.codeoff = codeoff;
    }
}
