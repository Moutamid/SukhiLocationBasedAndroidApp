package com.example.sukhilocationbasedapp.Model;

public class Vehicle {

    private String id;
    private String riderId;
    private String brand;
    private String model;
    private String year;
    private String license;
    private String color;

    public Vehicle(){

    }

    public Vehicle(String id, String riderId, String brand, String model, String year, String license, String color) {
        this.id = id;
        this.riderId = riderId;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.license = license;
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRiderId() {
        return riderId;
    }

    public void setRiderId(String riderId) {
        this.riderId = riderId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
