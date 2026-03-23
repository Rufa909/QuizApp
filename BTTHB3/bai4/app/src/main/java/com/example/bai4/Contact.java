package com.example.bai4;
public class Contact {
    private String name;
    private String phone;
    private boolean isMale;

    public Contact(String name, String phone, boolean isMale) {
        this.name = name;
        this.phone = phone;
        this.isMale = isMale;
    }

    public String getName() { return name; }
    public String getPhone() { return phone; }
    public boolean isMale() { return isMale; }
}