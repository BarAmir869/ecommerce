package com.ecomerce.udemy.ecomerce.models.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Entity
@Table(name = "pages")
@Data
public class Page {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Size(min = 2, message = "Title must be at least 2 characters long")
    @NotNull
    @Length(max = 45)
    private String title;
    @NotNull
    @Length(max = 45)
    private String slug;
    @Size(min = 5, message = "Content must be at least 5 characters long")
    @NotNull
    @Column(columnDefinition = "text")
    private String content;
    @NotNull
    @Digits(integer = 3, fraction = 0)
    private int sorting;
}
