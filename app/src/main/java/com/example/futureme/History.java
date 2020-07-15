package com.example.futureme;

public class History {
    private String Date;
    private String Message;
    private String Email;
    private String Title;


    public History(String date, String message,String title, String email) {
        Date = date;
        Message = message;
        Email = email;
        Title = title;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
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
                ", Title='" + Title + '\'' +
                '}';
    }
}
