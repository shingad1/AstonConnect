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

    public String  getUserId()  { return userid;  }
    public String  getPostId()  { return postid;  }
    public String  getDetails() { return details; }
    public boolean getIsPost()  { return ispost;  }

    public void setUserId  (String userid)    { this.userid = userid;    }
    public void setDetails (String details)   { this.details = details;  }
    public void setPostId  (String postid)    { this.postid = postid;    }
    public void setIsPost  (boolean ispost)   { this.ispost = ispost;    }
}
