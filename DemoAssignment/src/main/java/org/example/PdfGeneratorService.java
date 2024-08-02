package org.example;

import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import org.springframework.stereotype.Service;

@Service
public class PdfGeneratorService {
    public ByteArrayOutputStream generateByteArray(Invoice invoice) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(byteArrayOutputStream);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        document.add(new Paragraph("Invoice").setBold().setFontSize(20));
        document.add(new Paragraph("Seller: " + invoice.getSeller()));
        document.add(new Paragraph("Seller GSTIN: " + invoice.getSellerGstin()));
        document.add(new Paragraph("Seller Address: " + invoice.getSellerAddress()));
        document.add(new Paragraph("Buyer: " + invoice.getBuyer()));
        document.add(new Paragraph("Buyer GSTIN: " + invoice.getBuyerGstin()));
        document.add(new Paragraph("Buyer Address: " + invoice.getBuyerAddress()));
        document.add(new Paragraph("\n"));

        float[] pointColumnWidths = {150F, 50F, 100F, 100F};
        Table table = new Table(pointColumnWidths);
        table.addCell(new Cell().add(new Paragraph("Item Name")));
        table.addCell(new Cell().add(new Paragraph("Quantity")));
        table.addCell(new Cell().add(new Paragraph("Price")));
        table.addCell(new Cell().add(new Paragraph("Total")));

        for (Item item : invoice.getItems()) {
            table.addCell(new Cell().add(new Paragraph(item.getName())));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(item.getQuantity()))));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(item.getPrice()))));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(item.getQuantity() * item.getPrice()))));
        }

        document.add(table);
        document.close();

        return byteArrayOutputStream;
    }
}
