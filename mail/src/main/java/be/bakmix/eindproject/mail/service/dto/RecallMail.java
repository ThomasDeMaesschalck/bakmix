package be.bakmix.eindproject.mail.service.dto;

import lombok.Data;

@Data
public class RecallMail {
    private Long orderId;
    private String customerName;
    private String affectedProducts;
}
