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

    public String getid()                       { return id; }
    public void setid(String id)                { this.id = id; }
    public String getusername()                 { return username; }
    public void setusername(String username)    { this.username = username; }
    public String getfullname()                 { return fullname; }
    public void setfullname(String fullname)    { this.fullname = fullname; }
    public String getimageurl()                 { return imageurl; }
    public void setimageurl(String imageurl)    { this.imageurl = imageurl; }
    public String getbio()                      { return bio; }
    public void setbio(String bio)              { this.bio = bio; }
    public String getmodules()                  { return modules; }
    public void setmodules(String modules)      { this.modules = modules; }
    public String getemail()                    { return email; }
    public void setemail(String email)          { this.email = email; }
}
