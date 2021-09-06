package com.epam.tishkin.model;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Entity
@Table(name = "Bookmark")
public class Bookmark implements Serializable {
    private final static long serialVersionUID = 98745874L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name = "Book_title")
    @NotEmpty(message = "Please provide a book title to bookmark")
    private String title;
    @Column(name = "Page_number")
    @Min(value = 1, message = "The value of the number of pages must be greater than or equal to 1")
    private int page;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "User_login")
    @Valid
    private User user;

    public Bookmark() {

    }

    public Bookmark(String title, int page) {
        this.title = title;
        this.page = page;
    }

    public Bookmark(String title, int page, User user) {
        this.title = title;
        this.page = page;
        this.user = user;
    }

    public int getId(){
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Book title: " + title + ", page with bookmark: " + page;
    }
}
