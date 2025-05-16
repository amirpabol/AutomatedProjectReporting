package com.project.automated_project_reporting.repository;

import com.project.automated_project_reporting.model.ProjectReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectReportRepository extends JpaRepository<ProjectReport, Long> {
    // Optional: Add custom query methods if needed later
}
