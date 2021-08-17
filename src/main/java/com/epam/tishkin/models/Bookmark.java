package com.epam.tishkin.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Bookmark")
public class Bookmark implements Serializable {
    private final static long serialVersionUID = 98745874L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name = "Book_title")
    private String title;
    @Column(name = "Page_number")
    private int page;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "User_login")
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
