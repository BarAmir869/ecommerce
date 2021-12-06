package com.ecomerce.udemy.ecomerce.models.data;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Range;

import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "products")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Size(min = 2, max = 45, message = "Name must be between 2 and 45 characters long")
    private String name;
    private String slug;
    @Column(columnDefinition = "text")
    @Size(min = 5, message = "Name must be at least 5 characters long")
    private String description;
    private String image;

    @Column(name = "price", columnDefinition = "Decimal(8,2)")
    // @Pattern(regexp = "^[0-9]+([.][0-9]{1,2})?", message = "Expected
    // format:10,9.99, 15.90")
    @Digits(integer = 5, fraction = 2, message = "Expected format:10,9.99, 15.90")
    @Range(min = 0, message = "Price can't be negative")
    private double price;
    @Range(min = 0, message = "Amount can't be negative")
    private int amount;
    @ManyToOne(optional = false, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH })
    @JoinColumn(name = "category_id")
    @ToString.Exclude
    private Category category;
    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
