package be.bakmix.eindproject.pdf.web.rest;

import be.bakmix.eindproject.pdf.service.PdfService;
import be.bakmix.eindproject.pdf.service.dto.Order;
import be.bakmix.eindproject.pdf.util.GeneratePdf;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

@CrossOrigin
@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Log4j2
public class PdfResource {

    @Autowired
    private PdfService pdfService;

    @RequestMapping(value = "/pdfreport", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_PDF_VALUE)
    @RolesAllowed("bakmix-admin")
    public ResponseEntity<InputStreamResource> pdfReport(@RequestParam Long id,
                                                         @RequestParam String type) {


            Order order = pdfService.getById(id);
            ByteArrayInputStream  pdf = GeneratePdf.makeLabel(order);




        var headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=bakmix.pdf");


        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdf));
    }




}
