package com.project.automated_project_reporting.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.project.automated_project_reporting.model.ProjectReport;
import com.project.automated_project_reporting.model.ReportImage;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Color;

public class PDFGenerator {
    public static byte[] generate(ProjectReport report) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, baos);
        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
        Font subFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

        // FRONT PAGE
        if (report.getCompanyLogo() != null && report.getCompanyLogo().length > 0) {
            Image logo = Image.getInstance(report.getCompanyLogo());
            logo.scaleToFit(100, 100);
            logo.setAlignment(Image.ALIGN_CENTER);
            document.add(logo);
        }

        document.add(Chunk.NEWLINE);
        Paragraph title = new Paragraph(report.getTitle().toUpperCase(), titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(Chunk.NEWLINE);
        Paragraph subtitle = new Paragraph("LAPORAN BERGAMBAR", headerFont);
        subtitle.setAlignment(Element.ALIGN_CENTER);
        document.add(subtitle);

        document.add(Chunk.NEWLINE);
        Paragraph company = new Paragraph("Company: " + report.getCompanyName(), subFont);
        company.setAlignment(Element.ALIGN_CENTER);
        document.add(company);

        Paragraph date = new Paragraph("Date: " + report.getDate(), subFont);
        date.setAlignment(Element.ALIGN_CENTER);
        document.add(date);

        Paragraph location = new Paragraph("Location: " + report.getLocation(), subFont);
        location.setAlignment(Element.ALIGN_CENTER);
        document.add(location);

        // Group images by stage
        Map<String, List<ReportImage>> imagesByStage = new HashMap<>();
        for (ReportImage img : report.getImages()) {
            String stage = img.getStage() != null ? img.getStage() : "SEMASA";
            imagesByStage.computeIfAbsent(stage, k -> new ArrayList<>()).add(img);
        }

        String[] stageOrder = {"SEBELUM", "SEMASA", "SELEPAS"};
        for (String stage : stageOrder) {
            if (!imagesByStage.containsKey(stage)) continue;
            List<ReportImage> images = imagesByStage.get(stage);
            for (int i = 0; i < images.size(); i += 4) {
                document.newPage();

                // Stage title once at top
                Paragraph stagePara = new Paragraph(stage, headerFont);
                stagePara.setAlignment(Element.ALIGN_CENTER);
                document.add(stagePara);
                document.add(Chunk.NEWLINE);

                PdfPTable table = new PdfPTable(2);
                table.setWidthPercentage(100);
                table.setSpacingBefore(10);
                table.setSpacingAfter(10);

                for (int j = i; j < i + 4 && j < images.size(); j++) {
                    ReportImage img = images.get(j);

                    // Create nested cell with note and image
                    PdfPTable cellTable = new PdfPTable(1);

                    // Note (above image)
                    if (img.getNote() != null && !img.getNote().isEmpty()) {
                        Paragraph note = new Paragraph(img.getNote(), subFont);
                        note.setAlignment(Element.ALIGN_CENTER);
                        PdfPCell noteCell = new PdfPCell();
                        noteCell.setBorder(Rectangle.NO_BORDER);
                        noteCell.addElement(note);
                        cellTable.addCell(noteCell);
                    }

                    // Image with frame-like border
                    Image image = Image.getInstance(img.getImageData());
                    image.scaleToFit(250, 300);
                    PdfPCell imgCell = new PdfPCell(image, true);
                    imgCell.setPadding(5);
                    imgCell.setBorderWidth(2f);
                    imgCell.setBorderColor(Color.BLACK);
                    imgCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    imgCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cellTable.addCell(imgCell);

                    PdfPCell wrapperCell = new PdfPCell(cellTable);
                    wrapperCell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(wrapperCell);
                }

                // Fill remaining cells to maintain layout
                while (table.getNumberOfColumns() * (images.size() % 4 == 0 ? 4 : images.size() % 4) < 4) {
                    table.addCell("");
                }

                document.add(table);
            }
        }

        document.close();
        return baos.toByteArray();
    }
}
