package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.io.source.ByteArrayOutputStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
public class PdfControllerTest {

    @Mock
    PdfGeneratorService pdfGeneratorService;

    @InjectMocks
    PdfController pdfController;

    private Invoice invoice;

    @BeforeEach
    public void setUp() {
        invoice = new Invoice();
        invoice.setSeller("XYZ Pvt. Ltd.");
        invoice.setSellerGstin("29AABBCCDD121ZD");
        invoice.setSellerAddress("New Delhi, India");
        invoice.setBuyer("Vedant Computers");
        invoice.setBuyerGstin("29AABBCCDD131ZD");
        invoice.setBuyerAddress("New Delhi, India");

        List<Item> items = new ArrayList<>();
        Item item = new Item();
        item.setName("Product 1");
        item.setQuantity(12);
        item.setPrice(123.00);
        items.add(item);
        invoice.setItems(items);
    }

    public byte[] serializeInvoice(Invoice invoice) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(invoice);
        return json.getBytes("UTF-8");
    }

    @Test
    void testGeneratePdf() throws Exception {
        when(pdfGeneratorService.generateByteArray(invoice))
                .thenReturn(new ByteArrayOutputStream().assignBytes(serializeInvoice(invoice)));

        ResponseEntity<byte[]> responseEntity = pdfController.generatePdf(invoice);

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(MediaType.APPLICATION_PDF, responseEntity.getHeaders().getContentType());
        Assertions.assertEquals("attachment", responseEntity.getHeaders().getContentDisposition().getType());
        Assertions.assertEquals("invoice.pdf", responseEntity.getHeaders().getContentDisposition().getFilename());

        byte[] responseBody = responseEntity.getBody();
        Assertions.assertNotNull(responseBody);
        Assertions.assertTrue(responseBody.length > 0);
    }
}
