package be.bakmix.eindproject.mail.service.dto;

import lombok.Data;

/**
 * Recall Email DTO properties
 */
@Data
public class RecallMail {
    private Long orderId;
    private String customerName;
    private String affectedProducts;
}
