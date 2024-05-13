package com.oreilly.maventoys.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Set;

    /**
     * Entity representing a product in the application. Products are the items available for sale,
     * each with specific pricing, cost, and categorization details. Products are managed within
     * categories to organize the inventory effectively and are associated with inventory records
     * and invoices to track stock levels and sales transactions.
     */
    @Entity
    @Getter
    @Setter
    @Table(name = "products")

    public class Product {

        /**
         * Unique identifier for the product. This ID is automatically generated
         * and used to uniquely identify a product within the database.
         */
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        /**
         * Name of the product, used for identification and display purposes in the application's
         * user interface and reports.
         */
        private String name;

        /**
         * Cost of the product to the business, used for financial calculations such as
         * profit margin analysis.
         */
        private double cost;

        /**
         * Retail price of the product, at which it is sold to customers.
         */
        private double price;

        /**
         * The category to which the product belongs. This many-to-one relationship associates
         * the product with a specific category, facilitating organization and browsing of products.
         */
        @ManyToOne()
        @JoinColumn(name = "category_id", referencedColumnName = "id")
        private Category category;


        /**
         * The date the product was added to the catalog. This information can be used for inventory
         * management, reporting, and analyzing product lifecycle.
         */
        @Column(name = "creation_date")
        private Date creationDate;

        /**
         * Flag indicating whether the product is currently active and available for sale.
         * Inactive products are not displayed in the catalog and are not available for new sales transactions.
         */
        private boolean active;

        /**
         * Inventory records associated with the product. This one-to-many relationship tracks
         * the stock levels of the product across different locations or statuses within the inventory.
         */
        @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
        private Set<Inventory> inventory;

        /**
         * Invoices associated with the product. This one-to-many relationship links the product
         * to sales transactions in which it has been sold, providing a history of sales.
         */
        @JsonIgnore
        @OneToMany(mappedBy = "product")
        private List<Invoice> invoices;

    }
