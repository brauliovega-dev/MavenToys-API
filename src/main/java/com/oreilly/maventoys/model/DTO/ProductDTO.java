package com.oreilly.maventoys.model.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

    /**
     * Data Transfer Object for Product information.
     * Facilitates the exchange of product data across different layers of the application,
     * encapsulating product details essential for inventory management and sales operations.
     */
    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public class ProductDTO {


        /**
         * Unique identifier for the product.
         */
        private Integer id;


        /**
         * Name of the product, used for display and identification purposes in the application.
         */
        private String name;


        /**
         * Cost price of the product, indicating the expense to the business for acquiring the product.
         */
        private Double cost;


        /**
         * Selling price of the product, at which it is offered to customers.
         */
        private Double price;


        /**
         * Identifier for the category to which the product belongs, linking the product to its
         * respective category for organizational purposes.
         */
        private Integer categoryId;


        /**
         * Flag indicating whether the product is currently active and available for sale.
         */
        private Boolean active;


        /**
         * Date when the product was added to the inventory or catalog, useful for tracking new additions.
         */
        private LocalDate creationDate;

        /**
         * Stock when the product was added to the inventory or catalog, useful for tracking new additions.
         */
        private Integer stockOnHand;
        //get y set
    }
