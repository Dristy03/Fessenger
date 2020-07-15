package com.example.futureme;

public class History {
    private String Date;
    private String Message;
    private String Email;


    public History(String date, String message, String email) {
        Date = date;
        Message = message;
        Email = email;
    }

    public History() {
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    @Override
    public String toString() {
        return "History{" +
                "Date='" + Date + '\'' +
                ", Message='" + Message + '\'' +
                ", Email='" + Email + '\'' +
                '}';
    }
}
