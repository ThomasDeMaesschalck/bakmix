package be.bakmix.eindproject.mail.service.dto;

import lombok.Data;


/**
 * Tracking Mail DTO properties
 */
@Data
public class TrackingMail {
    private String code;
    private String link;
    private String customerName;
    private Long orderId;
}
