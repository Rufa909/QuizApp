package com.example.sqllite;
public class SinhVien {

    private int id;
    private String hovaten;

    public SinhVien(String hovaten) {
        this.hovaten = hovaten;
    }

    public SinhVien(int id, String hovaten) {
        this.id = id;
        this.hovaten = hovaten;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHovaten() {
        return hovaten;
    }

    public void setHovaten(String hovaten) {
        this.hovaten = hovaten;
    }

    @Override
    public String toString() {
        return "SinhVien{" +
                "hovaten='" + hovaten + '\'' +
                ", id=" + id +
                '}';
    }

}