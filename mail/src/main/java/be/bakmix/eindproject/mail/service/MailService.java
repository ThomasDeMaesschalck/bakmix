package be.bakmix.eindproject.mail.service;


import be.bakmix.eindproject.mail.service.dto.Customer;
import be.bakmix.eindproject.mail.service.dto.Order;
import be.bakmix.eindproject.mail.service.dto.Orderline;
import be.bakmix.eindproject.mail.service.dto.TrackingMail;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class MailService {
    @Autowired
    private KeycloakRestTemplate keycloakRestTemplate;

    @Value("http://localhost:7772/api/orders/")
    private String urlOrders;

    @Value("http://localhost:7778/api/products/")
    private String urlProducts;

    @Value("http://localhost:7771/api/ingredienttracings/")
    private String urlIngredienttracings;

    @Value("http://localhost:7777/api/ingredients/")
    private String urlIngredients;

    @Value("http://localhost:7773/api/orderlines/getbyorder/")
    private String urlOrderlines;

    @Value("http://localhost:7773/api/orderlines/")
    private String urlOrderlinesById;

    @Value("http://localhost:7779/api/customers/")
    private String urlCustomers;

    @Autowired
    private Configuration configuration;

    @Autowired
    private JavaMailSender javaMailSender;

    public MailService() {
    }

    public Order getById(Long id) {
        Order order = keycloakRestTemplate.getForObject(urlOrders + id, Order.class);
        Customer customer = keycloakRestTemplate.getForObject(urlCustomers + order.getCustomerId(), Customer.class);
        order.setCustomer(customer);
        Orderline[] orderlines = keycloakRestTemplate.getForObject(urlOrderlines + order.getId(), Orderline[].class);
        order.setOrderlines(Arrays.asList(orderlines));
        return order;
    }

    public void sendTrackingEmail(Long id, TrackingMail trackingMail) throws MessagingException, IOException, TemplateException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        Order order = getById(id);
        helper.setSubject("BakMix bestelling " +  order.getId() + " tracking code");
        helper.setTo(order.getCustomer().getEmail());
        trackingMail.setOrderId(order.getId());
        trackingMail.setCustomerName(order.getCustomer().getFirstName() + " " + order.getCustomer().getLastName());
        String emailContent = getTrackingEmailContent(trackingMail);
        helper.setText(emailContent, true);
        javaMailSender.send(mimeMessage);
    }

    public String getTrackingEmailContent(TrackingMail trackingMail) throws IOException, TemplateException {
        StringWriter stringWriter = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("trackingmail", trackingMail);
        configuration.getTemplate("trackingcodemail.ftlh").process(model, stringWriter);
        return stringWriter.getBuffer().toString();
    }
}
