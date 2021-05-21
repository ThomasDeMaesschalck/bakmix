package be.bakmix.eindproject.mail.web.rest;

import be.bakmix.eindproject.mail.service.MailService;
import be.bakmix.eindproject.mail.service.dto.Order;
import be.bakmix.eindproject.mail.service.dto.RecallMail;
import be.bakmix.eindproject.mail.service.dto.TrackingMail;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

/**
 * Resource layer of the Mail microservice. Generates the REST API endpoints.
 */
@CrossOrigin("*")
@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Log4j2
public class MailResource {

    /**
     * The Mail microservice service layer
     */
    @Autowired
    private MailService mailService;


    /**
     * API for sending a tracking e-mail to customers.
     * @param id The id of the Order
     * @param trackingMail Properties that need to be inserted into the tracking email template
     * @return TrackingMail
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.POST,path = "/trackingmail")
    @ResponseBody
    @RolesAllowed({"bakmix-admin"})
    public TrackingMail send(@RequestParam Long id, @Valid @RequestBody TrackingMail trackingMail) throws Exception {
        mailService.sendTrackingEmail(id, trackingMail);
        log.info("Mail send to customer from order #" + id);
        return trackingMail;
    }

    /**
     * API for sending a recall e-mail to one or more customer(s).
     * @param id The uniqueCode of the recalled ingredient
     * @return ResponseEntity
     * @throws Exception
     */
    @GetMapping("/sendrecall/")
    @RolesAllowed({"bakmix-admin"})
    public ResponseEntity<List<Order>> sendRecallEmail(@RequestParam String id) throws Exception{
        List<Order> orders = mailService.sendRecallEmail(id);
        log.info("Recall mail send to customer(s) with ingredient # " + id);
        return ResponseEntity.ok(orders);
    }

}
