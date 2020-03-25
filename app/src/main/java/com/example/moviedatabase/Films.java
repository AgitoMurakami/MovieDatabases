package com.example.moviedatabase;

public class Films {

    private String title;
    private String description;
    private String year;
    private String posterPath;

    public String getTitle() {
        return title;
    }

    public void setTitle(String judul) {
        this.title = judul;
    }

    public String getPoster(){
        return posterPath;
    }

    public void setPoster() {
        this.posterPath = posterPath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getYear() {
        return year;
    }

    public void getYear(String year) {
        this.year = year;
    }
}
