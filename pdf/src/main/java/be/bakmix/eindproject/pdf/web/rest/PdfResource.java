package be.bakmix.eindproject.pdf.web.rest;

import be.bakmix.eindproject.pdf.service.PdfService;
import be.bakmix.eindproject.pdf.service.dto.Order;
import be.bakmix.eindproject.pdf.util.GeneratePdf;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.IOException;
import com.itextpdf.io.source.ByteArrayOutputStream;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.annotation.security.RolesAllowed;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

@CrossOrigin
@Controller
@RequestMapping("/api")
@AllArgsConstructor
@Log4j2
public class PdfResource {

    @Autowired
    ServletContext servletContext;

    private final TemplateEngine templateEngine;

    @Autowired
    private PdfService pdfService;


    @RequestMapping(value = "/pdflabel", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_PDF_VALUE)
    @RolesAllowed("bakmix-admin")
    public ResponseEntity<InputStreamResource> pdfLabel(@RequestParam Long id) {

            Order order = pdfService.getById(id);
            ByteArrayInputStream  pdf = GeneratePdf.makeLabel(order);

        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdf));
    }


    @RequestMapping(value = "/pdfinvoice", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_PDF_VALUE)
    @RolesAllowed("bakmix-admin")
    public ResponseEntity<?> getPDF(HttpServletRequest request, HttpServletResponse response) throws IOException {

        /* Do Business Logic*/
        Order order = pdfService.getById(Long.parseLong(String.valueOf(1)));

        /* Create HTML using Thymeleaf template Engine */

        WebContext context = new WebContext(request, response, servletContext);
        context.setVariable("orderEntry", order);
        String orderHtml = templateEngine.process("order", context);

        /* Setup Source and target I/O streams */

        ByteArrayOutputStream target = new ByteArrayOutputStream();

        /*Setup converter properties. */
        ConverterProperties converterProperties = new ConverterProperties();
        converterProperties.setBaseUri("http://localhost:8080");

        /* Call convert method */
        HtmlConverter.convertToPdf(orderHtml, target, converterProperties);

        /* extract output as bytes */
        byte[] bytes = target.toByteArray();


        /* Send the response as downloadable PDF */

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(bytes);

    }



}
