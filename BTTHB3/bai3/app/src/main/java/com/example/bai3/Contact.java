package com.example.bai3;

public class Contact {
    private String name;
    private String idStudent;
    private int color;

    public Contact(String name, String idStudent, int color) {
        this.name = name;
        this.idStudent = idStudent;
        this.color = color;
    }

    public String getName() { return name; }
    public String getIdStudent() { return idStudent; }
    public int getColor() { return color; }
}
