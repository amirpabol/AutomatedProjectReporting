package com.project.automated_project_reporting.service;

import com.project.automated_project_reporting.model.ProjectReport;
import com.project.automated_project_reporting.model.ReportImage;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

public class WordGenerator {

    public static byte[] generate(ProjectReport report) throws Exception {
        XWPFDocument doc = new XWPFDocument();

        // Cover Page
        XWPFParagraph title = doc.createParagraph();
        title.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun titleRun = title.createRun();
        titleRun.setText(report.getTitle());
        titleRun.setFontSize(24);
        titleRun.setBold(true);

        XWPFRun companyRun = doc.createParagraph().createRun();
        companyRun.setText("Company: " + report.getCompanyName());
        companyRun.setFontSize(16);

        XWPFRun dateRun = doc.createParagraph().createRun();
        dateRun.setText("Date: " + report.getDate());
        dateRun.setFontSize(12);

        XWPFRun locationRun = doc.createParagraph().createRun();
        locationRun.setText("Location: " + report.getLocation());
        locationRun.setFontSize(12);

        if (report.getCompanyLogo() != null) {
            XWPFParagraph logoPara = doc.createParagraph();
            logoPara.setAlignment(ParagraphAlignment.RIGHT);
            XWPFRun logoRun = logoPara.createRun();
            logoRun.addPicture(new ByteArrayInputStream(report.getCompanyLogo()),
                    XWPFDocument.PICTURE_TYPE_PNG,
                    report.getCompanyLogoFilename(),
                    Units.toEMU(100),
                    Units.toEMU(100));
        }

        doc.createParagraph().setPageBreak(true);

        List<ReportImage> images = report.getImages();
        for (int i = 0; i < images.size(); i += 4) {
            XWPFTable table = doc.createTable(2, 2);

            for (int j = 0; j < 4; j++) {
                int row = j / 2;
                int col = j % 2;

                if (i + j < images.size()) {
                    ReportImage img = images.get(i + j);
                    XWPFTableCell cell = table.getRow(row).getCell(col);

                    XWPFParagraph stagePara = cell.addParagraph();
                    stagePara.setAlignment(ParagraphAlignment.CENTER);
                    XWPFRun stageRun = stagePara.createRun();
                    stageRun.setText(img.getStage());
                    stageRun.setBold(true);

                    XWPFRun imgRun = cell.addParagraph().createRun();
                    imgRun.addPicture(new ByteArrayInputStream(img.getImageData()),
                            XWPFDocument.PICTURE_TYPE_JPEG,
                            img.getFilename(),
                            Units.toEMU(200),
                            Units.toEMU(200));

                    XWPFParagraph notePara = cell.addParagraph();
                    notePara.setAlignment(ParagraphAlignment.CENTER);
                    XWPFRun noteRun = notePara.createRun();
                    noteRun.setText(img.getNote());
                } else {
                    table.getRow(row).getCell(col).setText("");
                }
            }

            doc.createParagraph().setPageBreak(true);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        doc.write(out);
        doc.close();
        return out.toByteArray();
    }
}
