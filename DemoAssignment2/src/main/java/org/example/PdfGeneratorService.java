package org.example;

import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PdfGeneratorService {
    public ByteArrayOutputStream generateByteArray(Invoice invoice) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(byteArrayOutputStream);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        // Title and Invoice Details
        document.add(new Paragraph("Invoice").setBold().setFontSize(20).setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph("\n"));

        addInvoiceDetails(document, invoice);
        document.add(new Paragraph("\n"));

        // Items Table
        List<Item> items = invoice.getItems();
        if (items != null && !items.isEmpty()) {
            Table table = new Table(new float[]{120F, 150F, 120F, 150F});
            table.addHeaderCell(new Cell().add(new Paragraph("Customer Name").setBold()).setTextAlignment(TextAlignment.CENTER));
            table.addHeaderCell(new Cell().add(new Paragraph("Customer Details").setBold()).setTextAlignment(TextAlignment.CENTER));
            table.addHeaderCell(new Cell().add(new Paragraph("Vendor Name").setBold()).setTextAlignment(TextAlignment.CENTER));
            table.addHeaderCell(new Cell().add(new Paragraph("Vendor Details").setBold()).setTextAlignment(TextAlignment.CENTER));

            for (Item item : items) {
                table.addCell(new Cell().add(new Paragraph(item.getCustomer_name() != null ? item.getCustomer_name() : "")));
                table.addCell(new Cell().add(new Paragraph(item.getCustomer_details() != null ? item.getCustomer_details() : "")).setPadding(5));
                table.addCell(new Cell().add(new Paragraph(item.getVendor_name() != null ? item.getVendor_name() : "")));
                table.addCell(new Cell().add(new Paragraph(item.getVendor_details() != null ? item.getVendor_details() : "")).setPadding(5));
            }
            document.add(table);
        }

        document.close();
        return byteArrayOutputStream;
    }

    private void addInvoiceDetails(Document document, Invoice invoice) {
        Table table = new Table(new float[]{300F, 300F});
        table.setFixedLayout(); // Enable fixed layout mode
                                 // Add cells to the table


        table.addCell(createCell("Customer Reference:", invoice.getCustomer_ref()));
        table.addCell(createCell("Invoice Number:", invoice.getInvoice_no().toString()));
        table.addCell(createCell("Invoice Type:", invoice.getInvoice_type()));
        table.addCell(createCell("Settlement Method:", invoice.getSettlement_method()));
        table.addCell(createCell("Station:", invoice.getStation()));
        table.addCell(createCell("Date:", invoice.getDate()));
        table.addCell(createCell("Billing Month:", invoice.getBilling_month()));
        table.addCell(createCell("Billing Category:", invoice.getBilling_category()));
        table.addCell(createCell("Settlement Period:", Integer.toString(invoice.getSettlement_period())));

        document.add(table);
    }

    private Cell createCell(String label, String value) {
        return new Cell()
                .add(new Paragraph(label).setBold())
                .add(new Paragraph(value))
                .setPadding(5)
                .setVerticalAlignment(VerticalAlignment.MIDDLE);
    }
}
