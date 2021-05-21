package be.bakmix.eindproject.pdf.util;

import be.bakmix.eindproject.pdf.service.dto.Order;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 *
 */
public class GeneratePdf {


    public static ByteArrayInputStream makeLabel(Order order) {

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            Font fontSize_24 =  FontFactory.getFont(FontFactory.HELVETICA, 24f);
            PdfWriter.getInstance(document, out);
            document.open();
            document.add(new Paragraph((order.getCustomer().getFirstName() + " " + order.getCustomer().getLastName()), fontSize_24));
            document.add(new Paragraph((order.getCustomer().getAddress()), fontSize_24));
            document.add(new Paragraph((order.getCustomer().getZipCode() + " " +  order.getCustomer().getCity()), fontSize_24));


            document.close();

        } catch (DocumentException ex) {

            System.out.printf("Error occurred: {0}", ex);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
