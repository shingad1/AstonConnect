package com.devin.astonconnect.Model;

public class User {

    private String id;
    private String username;
    private String fullname;
    private String imageurl;
    private String bio;
    private String modules;
    private String email;

    public User(String id, String username, String fullname, String imageurl, String bio) {
        this.id = id;
        this.username = username;
        this.fullname = fullname;
        this.imageurl = imageurl;
        this.bio = bio;
    }

    public User(){

    }

    public String getId()                       { return id; }
    public String getUsername()                 { return username; }
    public String getFullname()                 { return fullname; }
    public String getImageurl()                 { return imageurl; }
    public String getBio()                      { return bio; }
    public String getModules()                  { return modules; }
    public String getEmail()                    { return email; }

    public void setId(String id)                { this.id = id; }
    public void setUsername(String username)    { this.username = username; }
    public void setFullname(String fullname)    { this.fullname = fullname; }
    public void setImageurl(String imageurl)    { this.imageurl = imageurl; }
    public void setBio(String bio)              { this.bio = bio; }
    public void setModules(String modules)      { this.modules = modules; }
    public void setEmail(String email)          { this.email = email; }
}
