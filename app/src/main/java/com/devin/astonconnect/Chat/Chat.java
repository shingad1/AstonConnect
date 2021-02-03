package com.devin.astonconnect.Chat;

public class Chat {

    private String senderid;
    private String recieverid;
    private String message;

    public Chat(String senderid, String recieverid, String message){
        this.senderid   = senderid;
        this.recieverid = recieverid;
        this.message    = message;
    }

    public Chat(){ }


    //Setters and getters
    public String getSenderid()     { return senderid; }
    public String getRecieverid()   { return recieverid; }
    public String getMessage()      { return message; }

    public void setRecieverid(String recieverid) { this.recieverid = recieverid; }
    public void setMessage   (String message)    { this.message = message; }
    public void setSenderid  (String senderid)   { this.senderid = senderid; }


}
