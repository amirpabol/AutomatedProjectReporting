package com.project.automated_project_reporting.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ProjectReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String companyName;
    private String date;
    private String location;

    @OneToMany(mappedBy = "projectReport", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReportImage> images = new ArrayList<>();

    @Lob
    private byte[] companyLogo;

    private String companyLogoFilename;

    public ProjectReport() {
        // JPA requires a no-arg constructor
    }

    public ProjectReport(String title, String companyName, String date, String location, List<ReportImage> images) {
        this.title = title;
        this.companyName = companyName;
        this.date = date;
        this.location = location;
        this.images = images;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<ReportImage> getImages() {
        return images;
    }

    public void setImages(List<ReportImage> images) {
        this.images = images;
    }

    public byte[] getCompanyLogo() {
        return companyLogo;
    }

    public void setCompanyLogo(byte[] companyLogo) {
        this.companyLogo = companyLogo;
    }

    public String getCompanyLogoFilename() {
        return companyLogoFilename;
    }

    public void setCompanyLogoFilename(String companyLogoFilename) {
        this.companyLogoFilename = companyLogoFilename;
    }
}
