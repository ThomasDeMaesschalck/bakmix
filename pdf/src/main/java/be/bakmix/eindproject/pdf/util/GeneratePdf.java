package be.bakmix.eindproject.pdf.util;

import be.bakmix.eindproject.pdf.service.dto.Order;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

            System.out.printf("Error occurred: ", ex);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    public static byte[] makeInvoice(Order order, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext, TemplateEngine templateEngine)
    {
        /* Create HTML using Thymeleaf template Engine */
        WebContext context = new WebContext(request, response, servletContext);
        context.setVariable("orderEntry", order);
        String orderHtml = templateEngine.process("order", context);

        /* Setup Source and target I/O streams */
        com.itextpdf.io.source.ByteArrayOutputStream target = new com.itextpdf.io.source.ByteArrayOutputStream();

        /*Setup converter properties. */
        ConverterProperties converterProperties = new ConverterProperties();
        converterProperties.setBaseUri("http://localhost:8080");

        /* Call convert method */
        HtmlConverter.convertToPdf(orderHtml, target, converterProperties);

        /* extract output as bytes */
        return target.toByteArray();
    }
}
