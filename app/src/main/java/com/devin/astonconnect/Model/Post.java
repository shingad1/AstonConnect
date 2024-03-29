package com.devin.astonconnect.Model;

public class Post {
    private String  description;
    private Boolean isimagepost;
    private String  postid;
    private String  postimage;
    private String  publisher;
    private String  title;
    private String  posttype;


    public Post(String description, Boolean isimagepost, String postid, String postimage, String publisher, String title) {
        this.description = description;
        this.isimagepost = isimagepost;
        this.postid = postid;
        this.postimage = postimage;
        this.publisher = publisher;
        this.title = title;
    }

    public Post() {}




    public String getPosttype()     { return posttype; }
    public String  getDescription() { return description; }
    public Boolean getIsImagePost() { return isimagepost; }
    public String  getTitle()       { return title; }
    public String  getPostId()      { return postid; }
    public String  getPostImage()   { return postimage; }
    public String  getPublisher()   { return publisher; }

    public void setPosttype(String posttype)        { this.posttype = posttype; }
    public void setPublisher(String publisher)      { this.publisher = publisher; }
    public void setPostImage(String postimage)      { this.postimage = postimage; }
    public void setIsImagePost(Boolean isimagepost) { this.isimagepost = isimagepost; }
    public void setTitle(String title)              { this.title = title; }
    public void setDescription(String description)  { this.description = description; }
    public void setPostId(String postid)            { this.postid = postid; }
}
