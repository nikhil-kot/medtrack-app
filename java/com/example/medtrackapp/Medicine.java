/*
Medicine
This class allows for Medicine objects to be instantiated.
Objects of this class are mainly added to the recyclerview adapter and the SQL database.
Version 1 and 6/06/2020
Daniel Sin, Nikhil Kothuru, Sean Rhee
All of the imports below the package statement are dependencies.
 */

package com.example.medtrackapp;

public class Medicine {

    private String name_of_medicine, time_to_take, dayOfWeek, user;
    private int quantity_remaining, dosage, Photo, numberOfDoses;
    private int add;

    public Medicine(String user, String name_of_medicine, String time_to_take, String dayOfWeek, int quantity, int dosage, int numberOfDosage, int photo) {
        this.name_of_medicine = name_of_medicine;
        this.time_to_take = time_to_take;
        this.quantity_remaining = quantity;
        this.dosage = dosage;
        this.dayOfWeek = dayOfWeek;
        this.numberOfDoses = numberOfDosage;
        Photo = photo;
        this.user = user;
    }
    public Medicine() {
        this.name_of_medicine = "none";
        this.time_to_take = "none";
        this.quantity_remaining = 0;
        this.dosage = 0;
    }

    public String getName()
    {
        return name_of_medicine;
    }
    public String getTime()
    {
        return time_to_take;
    }
    public int getFrequency() {
        return quantity_remaining;
    }
    public void takeMeds() {
        if (quantity_remaining > 0) {
            if (add < numberOfDoses) {
                quantity_remaining = quantity_remaining - dosage;
                add += 1;
            }
        }
    }

    public int getNumberOfDoses() {
        return numberOfDoses;
    }

    public int getDosage() {
        return dosage;
    }
    public int getQuantity_remaining() {
        return quantity_remaining;
    }
    public int getPhoto() {
        return Photo;
    }

    public void addMeds() {
        quantity_remaining = quantity_remaining + 1;
    }


    public String getDayOfWeek() {
        return dayOfWeek;
    }
    public String getUser() {
        return user;
    }

    public void setName(String n) {
        name_of_medicine = n;
    }
    public void setTime(String t) {
        time_to_take = t;
    }
    public void setQuantity_remaining(int f) {
        quantity_remaining = f;
    }
    public void setDosage(int d) {
        dosage = d;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setPhoto(int newPhoto) {
        this.Photo = newPhoto;
    }
    public void setUser(String newUser) {
        this.user = newUser;
    }
}
