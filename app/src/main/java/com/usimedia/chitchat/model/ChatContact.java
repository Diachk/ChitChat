package com.usimedia.chitchat.model;

import java.util.Date;

/**
 * Created by Cheick on 03/06/16.
 */
public class ChatContact {

    private String name;
    private String statusMessage;
    private Date lastSeen;

    public ChatContact() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public Date getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(Date lastSeen) {
        this.lastSeen = lastSeen;
    }
}
