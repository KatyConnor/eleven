package com.hx.domain.framework.test;

public class DemoDTO {

    private long gid;

    private float fnum;

    private double money;

    private Float ffnums;

    private Boolean trues;

    private Double totale;

    public DemoDTO() {
    }

    public DemoDTO(long gid, float fnum, double money, Float ffnums, Boolean trues, Double totale) {
        this.gid = gid;
        this.fnum = fnum;
        this.money = money;
        this.ffnums = ffnums;
        this.trues = trues;
        this.totale = totale;
    }

    public long getGid() {
        return gid;
    }

    public void setGid(long gid) {
        this.gid = gid;
    }

    public float getFnum() {
        return fnum;
    }

    public void setFnum(float fnum) {
        this.fnum = fnum;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public Float getFfnums() {
        return ffnums;
    }

    public void setFfnums(Float ffnums) {
        this.ffnums = ffnums;
    }

    public Boolean getTrues() {
        return trues;
    }

    public void setTrues(Boolean trues) {
        this.trues = trues;
    }

    public Double getTotale() {
        return totale;
    }

    public void setTotale(Double totale) {
        this.totale = totale;
    }
}
