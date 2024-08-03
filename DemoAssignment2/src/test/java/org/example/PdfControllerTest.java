package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.io.source.ByteArrayOutputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PdfControllerTest {

    @Mock
    private PdfGeneratorService pdfGeneratorService;

    @InjectMocks
    private PdfController pdfController;

    private Invoice invoice;

    @BeforeEach
    public void setUp() {
        invoice = new Invoice();
        invoice.setCustomer_ref("QF-081");
        invoice.setInvoice_no(BigInteger.valueOf(319594156));
        invoice.setInvoice_type("Prime Billing");
        invoice.setSettlement_method("BSP");
        invoice.setStation("Pune");
        invoice.setDate("03/Apr/2020");
        invoice.setBilling_month("Jan");
        invoice.setBilling_category("MISC");
        invoice.setSettlement_period(20200304);

        List<Item> items = new ArrayList<>();
        Item item = new Item();
        item.setCustomer_name("Quantas Airways Limited");
        item.setCustomer_details("10 Bourke Road, MASCOT, AUSTRALIA, Zip/Postal Code: 2020. Tax Reg No: 16 009 661 901");
        item.setVendor_name("PT ANGKASA PURA I (PERSERO)");
        item.setVendor_details("WWW.IATA.ORG/CS, Kota Baru Bandar Kemayoran Blok B.12, Kav. 2 Jakarta Pusat, Jakarta, INDONESIA. Email: WWW.IATA.ORG/CS");
        items.add(item);
        invoice.setItems(items);
    }

    @Test
    void testGeneratePdf() throws Exception {
        when(pdfGeneratorService.generateByteArray(invoice))
                .thenReturn(new PdfGeneratorService().generateByteArray(invoice));

        ResponseEntity<byte[]> responseEntity = pdfController.generatePdf(invoice);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_PDF);
        assertThat(responseEntity.getHeaders().getContentDisposition().getType()).isEqualTo("attachment");
        assertThat(responseEntity.getHeaders().getContentDisposition().getFilename()).isEqualTo("invoice.pdf");

        byte[] responseBody = responseEntity.getBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.length).isGreaterThan(0);
    }
}
