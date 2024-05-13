package com.oreilly.maventoys.model.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

    /**
     * Data Transfer Object for inventory records.
     * Streamlines inventory data communication by focusing on key inventory details,
     * useful in inventory management and product stock level tracking.
     */
    @Getter
    @Setter
    @JsonInclude()
    public class InventoryDTO {


        /**
         * Unique identifier for the inventory record.
         */
        private Integer id;


        /**
         * Identifier for the product associated with this inventory record. Links inventory data
         * to specific products.
         */
        private Integer productId;


        /**
         * The current quantity of the product available in stock. This figure is crucial for inventory
         * management, order fulfillment, and restocking decisions.
         */
        private Integer stockOnHand;


    }
