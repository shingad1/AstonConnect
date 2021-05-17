package com.devin.astonconnect.Chat;

/**
 * Model class for the chat
 */
public class Chat {

    private String senderid;
    private String recieverid;
    private String message;
    private Boolean messageseen;

    public Chat(String senderid, String recieverid, String message, Boolean messageseen){
        this.senderid   = senderid;
        this.recieverid = recieverid;
        this.message    = message;
        this.messageseen = messageseen;
    }

    public Chat(){ }

    //Setters and getters
    public String getSenderid()     { return senderid; }
    public String getRecieverid()   { return recieverid; }
    public String getMessage()      { return message; }
    public Boolean getMessageseen() { return messageseen; }

    public void setRecieverid(String recieverid) { this.recieverid = recieverid; }
    public void setMessage   (String message)    { this.message = message; }
    public void setSenderid  (String senderid)   { this.senderid = senderid; }
    public void setMessageseen(Boolean messageseen) { this.messageseen = messageseen; }


}
