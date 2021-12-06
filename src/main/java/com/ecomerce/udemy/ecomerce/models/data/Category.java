package com.ecomerce.udemy.ecomerce.models.data;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "categories")
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Size(min = 2, max = 45, message = "Name must be between 2 and 45 characters long")
    private String name;
    // @NotNull
    // @Length(max = 45)
    private String slug;
    // @NotNull
    private int sorting;
    @ToString.Exclude
    @OneToMany(cascade = { CascadeType.REMOVE, CascadeType.REFRESH, CascadeType.MERGE }, mappedBy = "category")
    private Collection<Product> products;

}
