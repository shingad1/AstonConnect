package com.devin.astonconnect.Model;

public class ActivityItem {

    private String userid;
    private String fullname;
    private String details;
    private String postid;
    private boolean ispost;

    public ActivityItem(String userid, String details, String postid, boolean ispost) {
        this.userid = userid;
        this.details = details;
        this.postid = postid;
        this.ispost = ispost;
    }

    public ActivityItem(){ }

    public String getuserid  () { return userid;  }
    public String getpostid  () { return postid;  }
    public String getdetails () { return details; }
    public boolean getispost () { return ispost;  }

    public void setuserid  (String userid)    { this.userid = userid;    }
    public void setdetails (String details)   { this.details = details;  }
    public void setpostid  (String postid)    { this.postid = postid;    }
    public void setispost  (boolean ispost)   { this.ispost = ispost;    }
}
