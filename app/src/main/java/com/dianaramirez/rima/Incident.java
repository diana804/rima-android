package com.dianaramirez.rima;

public class Incident {
    private int id;
    private String title;
    private String description;
    private String distance;
    private String date;
    private int type;
    private int image;
    private boolean editable;

    public Incident(int id, String title, String description, String distance, String date, int type, int image, boolean editable) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.distance = distance;
        this.date = date;
        this.type = type;
        this.image = image;
        this.editable = editable;
    }

    public int getId() { return id; }

    public String getTitle() { return title; }

    public String getDescription() {
        return description;
    }

    public String getDistance() {
        return distance;
    }

    public String getDate() {
        return date;
    }

    public int getImage() {
        return image;
    }

    public int getType(){ return type;}

    public boolean getEditable(){return editable;}
}
