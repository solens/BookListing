package com.example.android.booklisting;

import java.util.List;

/**
 * Created by Solen on 6/19/2017.
 */

public class Book {
    String title;
    String author;

    public Book(String title, String author) {
        this.title = title;
        this.author = author.replace("[","").replace("]","").replace("\"","").replace(",",", ");
    }

    public Book(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
