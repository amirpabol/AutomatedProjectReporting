package com.project.automated_project_reporting.repository;

import com.project.automated_project_reporting.model.ReportImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportImageRepository extends JpaRepository<ReportImage, Long> {
    // Optional: Add methods to find images by projectReport, etc.
}
