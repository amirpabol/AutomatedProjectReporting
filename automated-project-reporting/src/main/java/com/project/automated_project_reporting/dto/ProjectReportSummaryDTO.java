package com.project.automated_project_reporting.dto;

import java.util.List;

public class ProjectReportSummaryDTO {
    private Long id;
    private String title;
    private String companyName;
    private String date;
    private String location;
    private List<String> imageFilenames;

    public ProjectReportSummaryDTO(Long id, String title, String companyName, String date, String location, List<String> imageFilenames) {
        this.id = id;
        this.title = title;
        this.companyName = companyName;
        this.date = date;
        this.location = location;
        this.imageFilenames = imageFilenames;
    }

    // Getters only for JSON serialization
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getCompanyName() { return companyName; }
    public String getDate() { return date; }
    public String getLocation() { return location; }
    public List<String> getImageFilenames() { return imageFilenames; }
}
