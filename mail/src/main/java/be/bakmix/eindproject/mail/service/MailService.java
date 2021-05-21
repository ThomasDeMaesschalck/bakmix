package be.bakmix.eindproject.mail.service;


import be.bakmix.eindproject.mail.service.dto.*;
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
import java.util.*;

/**
 * Service layer of the Mail microservice
 */
@Service
@AllArgsConstructor
public class MailService {

    /**
     * List of Orders
     */
    private List<Order> orders = new ArrayList<>();

    /**
     * Keycloak rest template, used for pulling in data via REST from other microservices
     */
    @Autowired
    private KeycloakRestTemplate keycloakRestTemplate;

    @Value("http://localhost:7772/api/tracing/")
    private String urlTracedOrders;


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

    /**
     * Default constructor
     */
    public MailService() {
    }

    /**
     * Retrieve an Order via the Order microservice and set the Orderlines via the Orderline microservice
     * @param id The id of the Order that needs to be retrieved
     * @return The Order with its Orderlines
     */
    public Order getById(Long id) {
        Order order = keycloakRestTemplate.getForObject(urlOrders + id, Order.class);
        Customer customer = keycloakRestTemplate.getForObject(urlCustomers + order.getCustomerId(), Customer.class);
        order.setCustomer(customer);
        Orderline[] orderlines = keycloakRestTemplate.getForObject(urlOrderlines + order.getId(), Orderline[].class);
        order.setOrderlines(Arrays.asList(orderlines));
        return order;
    }

    /**
     * Send an e-mail to a customer with a tracking code
     * @param id The id of the Order that is being processed
     * @param trackingMail Content properties of the tracking e-mail
     * @throws MessagingException
     * @throws IOException
     * @throws TemplateException
     */
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

    /**
     * Method to compose the content of the tracking e-mail. Uses trackingcodemail template, located in resources folder.
     * @param trackingMail Properties that need to be inserted in the tracking e-mail template
     * @return A processed String that contains the right details for the tracking e-mail
     * @throws IOException
     * @throws TemplateException
     */
    public String getTrackingEmailContent(TrackingMail trackingMail) throws IOException, TemplateException {
        StringWriter stringWriter = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("trackingmail", trackingMail);
        configuration.getTemplate("trackingcodemail.ftlh").process(model, stringWriter);
        return stringWriter.getBuffer().toString();
    }

    /**
     * Send e-mails to customers subject to a recall action.
     * This method iterates over a List of Orders and retrieves the customer details plus the affected orderlines.
     * @param id The uniqueCode of the recalled Ingredient
     * @return List of orders
     * @throws MessagingException
     * @throws IOException
     * @throws TemplateException
     */
    public List<Order> sendRecallEmail(String id) throws MessagingException, IOException, TemplateException {
        orders.clear();
        Order[] tracedOrders = keycloakRestTemplate.getForObject(urlTracedOrders + id, Order[].class);
        Arrays.stream(tracedOrders).forEach(order -> {orders.add(order);});

        for (Order order: orders)
        {
            RecallMail recallMail = new RecallMail();
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

            helper.setSubject("BakMix bestelling " +  order.getId() + " terugroeping");
            helper.setTo(order.getCustomer().getEmail());
            recallMail.setOrderId(order.getId());
            recallMail.setCustomerName(order.getCustomer().getFirstName() + " " + order.getCustomer().getLastName());
            StringBuilder sb = new StringBuilder();
            for(Orderline orderline : order.getOrderlines())
            {
                sb.append("<li>" + orderline.getProduct().getName() + "</li>");
            }

            recallMail.setAffectedProducts(sb.toString());
            String emailContent = getRecallEmailContent(recallMail);
            helper.setText(emailContent, true);
            javaMailSender.send(mimeMessage);
        }
        return orders;
    }

    /**
     * Method to compose the content of the recall e-mail that gets send to a customer. Uses the recallmail template, found in the resources folder.
     * @param recallMail Properties that need to be inserted in the template.
     * @return A String consisting of the processed template.
     * @throws IOException
     * @throws TemplateException
     */
    public String getRecallEmailContent(RecallMail recallMail) throws IOException, TemplateException {
        StringWriter stringWriter = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("recallmail", recallMail);
        configuration.getTemplate("recallmail.ftlh").process(model, stringWriter);
        return stringWriter.getBuffer().toString();
    }
}
