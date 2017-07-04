package com.example.shivani.bookshelf;

/**
 * Created by shivani on 7/1/2017.
 */

public class Book {
    private String title;
    private String authorOfBook[];
    public Book(String t, String s[])
    {
        title=t;
        authorOfBook=s;
    }
    public String getTitle()
    {
        return title;
    }
    public String[] getSubtitle()
    {
        return authorOfBook;
    }
}
