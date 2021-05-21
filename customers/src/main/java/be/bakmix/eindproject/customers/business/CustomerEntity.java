package be.bakmix.eindproject.customers.business;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Entity class for the Customer object.
 * Customer objects are persisted in the database in the Customers table.
 */
@Entity
@Table(name="Customers")
@Data
public class CustomerEntity {

    /**
     * Id is a generated identification value for each Customer object
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * First name of the customer
     */
    private String firstName;

    /**
     * Last name of the customer
     */
    private String lastName;

    /**
     * Address  of the customer
     */
    private String address;

    /**
     * Postal code of the customer
     */
    private String zipCode;

    /**
     * City of the customer
     */
    private String city;

    /**
     * Customer phone number
     */
    private String phone;

    /**
     * Customer e-mail address
     */
    private String email;
}
