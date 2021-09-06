package com.epam.tishkin.model;

import com.epam.tishkin.server.validator.YearRange;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Entity
@Table(name = "Book")
public class Book implements Serializable {
    private static final long serialVersionUID = 965896523L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    private int id;
    @Column(name = "Title")
    @NotEmpty(message = "Please provide a title")
    private String title;
    @Column(name = "ISBNumber")
    @Pattern(regexp = "[0-9]{13}", message = "The number must consist of 13 digits")
    private String ISBNumber;
    @Column(name = "Publication_Year")
    @YearRange(message = "The year value must be between 1457 and the current year")
    private int publicationYear;
    @Column(name = "Pages_Number")
    @Min(value = 1, message = "The value of the number of pages must be greater than or equal to 1")
    private int pagesNumber;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Author_name")
    @Valid
    private Author author;

    public Book() {
    }

    public Book(String title, String ISBNumber, int year, int pagesNumber, Author author) {
        this.title = title;
        this.ISBNumber = ISBNumber;
        this.publicationYear = year;
        this.pagesNumber = pagesNumber;
        this.author = author;
    }

    public int getId(){
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getISBNumber() {
        return ISBNumber;
    }

    public void setISBNumber(String ISBNumber) {
        this.ISBNumber = ISBNumber;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public int getPagesNumber() {
        return pagesNumber;
    }

    public void setPagesNumber(int pagesNumber) {
        this.pagesNumber = pagesNumber;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Author: " + getAuthor() + " Title: " + title;
    }
}
