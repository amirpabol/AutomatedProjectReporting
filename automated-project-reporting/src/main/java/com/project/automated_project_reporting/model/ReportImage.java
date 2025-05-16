package com.project.automated_project_reporting.model;

import java.sql.Types;

import org.hibernate.annotations.JdbcTypeCode;

import jakarta.persistence.*;

@Entity
public class ReportImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filename;

    @Lob
    @Column(name ="image_data") 
    @JdbcTypeCode(Types.BINARY)
    private byte[] imageData;

    private String note;
    private String stage;

    @ManyToOne
    @JoinColumn(name = "project_report_id")
    private ProjectReport projectReport;

    public ReportImage() {}

    public ReportImage(String filename, byte[] imageData, String note) {
        this.filename = filename;
        this.imageData = imageData;
        this.note = note;
        this.stage = "Semasa";
    }

    // Getters and Setters ...

    public Long getId() {
        return id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public ProjectReport getProjectReport() {
        return projectReport;
    }

    public void setProjectReport(ProjectReport projectReport) {
        this.projectReport = projectReport;
    }

    public String getStage() {
        return stage;
    }
    
    public void setStage(String stage) {
        this.stage = stage;
    }
}
