package com.codflix.backend.models;

import com.codflix.backend.core.Conf;
import com.codflix.backend.core.Database;
import spark.Session;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class Media {

    private int id;
    private int genreId;
    private String title;
    private String type;
    private String status;
    private Date releaseDate;
    private String summary;
    private String trailerUrl;
    // add image url
    private String urlImage;
    private int year;

    public Media(int id, int genreId, String title, String type, String status, Date releaseDate, String summary, String trailerUrl,String urlImage) {
        this.id = id;
        this.genreId = genreId;
        this.title = title;
        this.type = type;
        this.status = status;
        this.releaseDate = releaseDate;
        this.summary = summary;
        this.trailerUrl = trailerUrl;
        // add image url
        this.urlImage = urlImage;
        this.year=getcreateYear();
     }

    @Override
    public String toString() {
        return "Media{" +
                "id=" + id +
                ", genreId=" + genreId +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", releaseDate=" + releaseDate +
                ", summary='" + summary + '\'' +
                ", trailerUrl='" + trailerUrl + '\'' +
                ", urlImage='" + urlImage + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGenreId() {
        return genreId;
    }


    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getReleaseDate() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String dateString = format.format( releaseDate  );
        Date   date       = format.parse ( dateString );
        return date;
    }

    public String gooddate(){
        DateFormat mediumDateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
        String format = mediumDateFormat.format(releaseDate);
        String test ="lol";
        return test;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTrailerUrl() {
        return trailerUrl;
    }

    public void setTrailerUrl(String trailerUrl) {
        this.trailerUrl = trailerUrl;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public int getcreateYear() {
        Date date =this.releaseDate;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        year = calendar.get(Calendar.DAY_OF_MONTH+Calendar.MONTH+Calendar.YEAR);
        return year;
    }
}

