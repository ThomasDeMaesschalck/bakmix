package be.bakmix.eindproject.mail.service.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class Order {
    private Long id;
    private Long customerId;
    private LocalDate date;
    private Long status;
    private BigDecimal discount;
    private  BigDecimal shippingCost;
    private BigDecimal subTotal;
    private BigDecimal vatTotal;
    private BigDecimal total;

    private Customer customer;
    private List<Orderline> orderlines = new ArrayList<>();
}