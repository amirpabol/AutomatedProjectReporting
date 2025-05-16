package com.project.automated_project_reporting;

import com.project.automated_project_reporting.dto.ProjectReportSummaryDTO;
import com.project.automated_project_reporting.model.ProjectReport;
import com.project.automated_project_reporting.model.ReportImage;
import com.project.automated_project_reporting.service.PDFGenerator;
import com.project.automated_project_reporting.service.WordGenerator;
import com.project.automated_project_reporting.repository.ProjectReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ProjectReportRepository reportRepository;

    @PostMapping("/generate")
    public ResponseEntity<byte[]> generateReport(
            @RequestParam String title,
            @RequestParam String companyName,
            @RequestParam String date,
            @RequestParam String location,
            @RequestParam("images") List<MultipartFile> images,
            @RequestParam("notes") List<String> notes,
            @RequestParam("stages") List<String> stages, // Added stages parameter
            @RequestParam(value = "companyLogo", required = false) MultipartFile companyLogo,
            @RequestParam(defaultValue = "pdf") String format) throws Exception {

        List<ReportImage> reportImages = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            MultipartFile file = images.get(i);
            ReportImage img = new ReportImage(file.getOriginalFilename(), file.getBytes(), notes.get(i));
            img.setStage(stages.get(i)); // Set the stage
            reportImages.add(img);
        }

        ProjectReport report = new ProjectReport(title, companyName, date, location, reportImages);

        // Attach logo if provided
        if (companyLogo != null && !companyLogo.isEmpty()) {
            report.setCompanyLogo(companyLogo.getBytes());
            report.setCompanyLogoFilename(companyLogo.getOriginalFilename());
        }

        for (ReportImage img : reportImages) {
            img.setProjectReport(report); // Set relationship
        }

        reportRepository.save(report);

        // Debug the format value
        System.out.println("Requested format: " + format);
        
        byte[] generatedFile;
        if (format.equalsIgnoreCase("pdf")) {
            System.out.println("Generating PDF file");
            generatedFile = PDFGenerator.generate(report);
        } else {
            System.out.println("Generating DOCX file");
            generatedFile = WordGenerator.generate(report);
        }

        String fileName = title.replaceAll(" ", "_") + "." + format.toLowerCase();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(format.equalsIgnoreCase("pdf")
                ? MediaType.APPLICATION_PDF
                : MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"));
        headers.setContentDisposition(ContentDisposition.attachment().filename(fileName).build());

        return new ResponseEntity<>(generatedFile, headers, HttpStatus.OK);
    }

    @GetMapping("/all")
    public List<ProjectReportSummaryDTO> getAllReports() {
        List<ProjectReport> reports = reportRepository.findAll();

        return reports.stream()
                .map(report -> new ProjectReportSummaryDTO(
                        report.getId(),
                        report.getTitle(),
                        report.getCompanyName(),
                        report.getDate(),
                        report.getLocation(),
                        report.getImages().stream()
                                .map(ReportImage::getFilename)
                                .toList()
                ))
                .toList();
    }
}
