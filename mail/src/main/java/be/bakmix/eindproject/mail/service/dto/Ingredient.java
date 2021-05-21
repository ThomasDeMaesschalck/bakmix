package be.bakmix.eindproject.mail.service.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * Ingredient DTO
 */
@Data
public class Ingredient {
    private Long id;
    private String uniqueCode;
    private String brand;
    private String type;
    private LocalDate purchaseDate;
    private String purchaseLocation;
    private String lotNumber;
    private String volume;
    private Boolean available;
    private Long ingredienttracingId;
    private Boolean recalled;
    private LocalDate expiry;
}
