package com.plugpro.data.model;

import com.google.firebase.firestore.ServerTimestamp;
import java.io.Serializable;
import java.util.Date;

public class ChatMessage implements Serializable {
    private String id;
    private String chatId;
    private String senderId;
    private String senderName;
    private String receiverId;
    private String message;
    @ServerTimestamp
    private Date timestamp;
    private boolean isRead;

    public ChatMessage() {
        this.isRead = false;
    }

    public ChatMessage(String chatId, String senderId, String senderName, String receiverId, String message) {
        this();
        this.chatId = chatId;
        this.senderId = senderId;
        this.senderName = senderName;
        this.receiverId = receiverId;
        this.message = message;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getChatId() { return chatId; }
    public void setChatId(String chatId) { this.chatId = chatId; }

    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }

    public String getSenderName() { return senderName != null ? senderName : ""; }
    public void setSenderName(String senderName) { this.senderName = senderName; }

    public String getReceiverId() { return receiverId; }
    public void setReceiverId(String receiverId) { this.receiverId = receiverId; }

    public String getMessage() { return message != null ? message : ""; }
    public void setMessage(String message) { this.message = message; }

    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
}
