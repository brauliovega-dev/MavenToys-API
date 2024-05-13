package com.oreilly.maventoys.model.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

    /**
     * Data Transfer Object for Category details.
     * Used to transfer category data between processes, minimizing the amount of data that needs to be sent,
     * especially in API responses or requests, focusing on the essential fields relevant for business operations.
     */
    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public class CategoryDTO {
        /**
         * Unique identifier for the category.
         */
        private Integer id;


        /**
         * Name of the category. This is used to display the category name in user interfaces and reports.
         */
        private String name;


        /**
         * Status indicating whether the category is currently active or inactive. Active categories are
         * available for product association.
         */
        private Boolean active;

        /**
         * The total sales amount in dollars.
         * This variable keeps track of the cumulative sales
         * amount. It is updated whenever a sale is made.
         */
        private double totalSales;



        //get y set
    }
