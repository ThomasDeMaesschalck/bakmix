package be.bakmix.eindproject.mail.web.rest;

import be.bakmix.eindproject.mail.service.MailService;
import be.bakmix.eindproject.mail.service.dto.Order;
import be.bakmix.eindproject.mail.service.dto.TrackingMail;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Log4j2
public class MailResource {

    @Autowired
    private MailService mailService;


    @RequestMapping(method = RequestMethod.POST,path = "/trackingmail")
    @ResponseBody
    @RolesAllowed({"bakmix-admin"})
    public String send(@RequestParam Long id, @Valid @RequestBody TrackingMail trackingMail) throws Exception {
        mailService.sendTrackingEmail(id, trackingMail);
        log.info("Mail send to customer from order #");
        return "Email Sent..!";
    }

}
