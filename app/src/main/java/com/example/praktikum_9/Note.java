package com.example.praktikum_9;

public class Note {
    private String title;
    private String description;
    private String id;

    public Note(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Note() {
        // Default constructor required for calls to DataSnapshot.getValue(Note.class)
    }


    public String getID(){
        return id;
    }

    public void setID(String key){
        this.id = key;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
