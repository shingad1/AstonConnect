package com.devin.astonconnect.Model;

public class Post {
    private String  description;
    private Boolean isimagepost;
    private String  postid;
    private String  postimage;
    private String  publisher;
    private String  title;

    public String  getdescription() { return description; }
    public Boolean getisimagepost() { return isimagepost; }
    public String  gettitle()       { return title; }
    public String  getpostid()      { return postid; }
    public String  getpostimage()   { return postimage; }
    public String  getpublisher()   { return publisher; }


    public void setpublisher(String publisher)      { this.publisher = publisher; }
    public void setpostimage(String postimage)      { this.postimage = postimage; }
    public void setisimagepost(Boolean isimagepost) { this.isimagepost = isimagepost; }
    public void settitle(String title)              { this.title = title; }
    public void setdescription(String description)  { this.description = description; }
    public void setpostid(String postid)            { this.postid = postid; }


}
