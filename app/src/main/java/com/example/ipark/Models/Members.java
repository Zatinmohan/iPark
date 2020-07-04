package com.example.ipark.Models;

public class Members {
    String First_name;
    String Last_name;
    String Plate_number;
    String id;
    String Car_name;
    String email;
    String loginid;

    public Members(){
    }

    public String getFirst_name() {
        return First_name;
    }

    public void setFirst_name(String first_name) {
        First_name = first_name;
    }

    public String getLast_name() {
        return Last_name;
    }

    public void setLast_name(String last_name) {
        Last_name = last_name;
    }

    public String getPlate_number() {
        return Plate_number;
    }

    public void setPlate_number(String plate_number) {
        Plate_number = plate_number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCar_name() {
        return Car_name;
    }

    public void setCar_name(String car_name) {
        Car_name = car_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLoginid() {
        return loginid;
    }

    public void setLoginid(String loginid) {
        this.loginid = loginid;
    }
}
