package com.usimedia.chitchat.model;

import com.google.common.base.Objects;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Cheick on 03/06/16.
 */
public class ChatContact implements Serializable {

    private String name;
    private String statusMessage;
    private String email;
    private Date lastSeen;


    public ChatContact() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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


    @Override
    public String toString() {
        return "ChatContact{" +
                "name='" + name + '\'' +
                ", statusMessage='" + statusMessage + '\'' +
                ", email='" + email + '\'' +
                ", lastSeen=" + lastSeen +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatContact that = (ChatContact) o;
        return Objects.equal(name, that.name) &&
                Objects.equal(statusMessage, that.statusMessage) &&
                Objects.equal(email, that.email) &&
                Objects.equal(lastSeen, that.lastSeen);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, statusMessage, email, lastSeen);
    }
}
