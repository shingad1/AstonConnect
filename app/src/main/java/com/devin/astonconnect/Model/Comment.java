package com.devin.astonconnect.Model;

public class Comment {

    private String comment;
    private String publisher;

    public Comment(String comment, String publisher){
        this.comment = comment;
        this.publisher = publisher;
    }

    public String getcomment()                   { return comment; }
    public void   setcomment(String comment)     { this.comment = comment; }

    public void   setpublisher(String publisher) { this.publisher = publisher; }
    public String getpublisher()                 { return publisher; }
}
