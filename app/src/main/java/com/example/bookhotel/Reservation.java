package com.example.bookhotel;

import java.io.Serializable;

public class Reservation implements Serializable {
    private String id;
    private String user;
    private String city;
    private String arrivalDate;
    private String arrivalTime;
    private String nights;
    private String guests;
    private String status;
    private String bookedOn;
    private String price;

    public Reservation(){}

    public Reservation(String id, String user, String city, String arrivalDate, String arrivalTime, String nights, String guests, String bookedOn,String status, String price) {
        this.id = id;
        this.user= user;
        this.city = city;
        this.arrivalDate = arrivalDate;
        this.arrivalTime = arrivalTime;
        this.nights = nights;
        this.guests = guests;
        this.status = status;
        this.bookedOn = bookedOn;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getUser() {
        return user;
    }
    public String getCity() {
        return city;
    }
    public String getArrivalDate() {
        return arrivalDate;
    }
    public String getArrivalTime() {
        return arrivalTime;
    }
    public String getNights() {
        return nights;
    }
    public String getGuests() {
        return nights;
    }
    public String getStatus() {
        return status;
    }
    public String getBookedOn() {
        return bookedOn;
    }
    public String getPrice() {
        return price;
    }
}
