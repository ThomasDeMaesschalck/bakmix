package be.bakmix.eindproject.mail.service.dto;

import lombok.Data;


@Data
public class TrackingMail {
    private String code;
    private String link;
    private String customerName;
    private Long orderId;
}
