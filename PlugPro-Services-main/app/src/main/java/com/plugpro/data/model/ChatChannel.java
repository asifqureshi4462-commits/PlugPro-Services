package com.plugpro.data.model;

import com.google.firebase.firestore.ServerTimestamp;
import java.io.Serializable;
import java.util.Date;

public class ChatChannel implements Serializable {
    private String id;
    private String customerId;
    private String customerName;
    private String providerId;
    private String providerName;
    private String lastMessage;
    @ServerTimestamp
    private Date lastTimestamp;
    private int unreadCountCustomer;
    private int unreadCountProvider;

    public ChatChannel() {}

    public ChatChannel(String id, String customerId, String customerName, String providerId, String providerName) {
        this.id = id;
        this.customerId = customerId;
        this.customerName = customerName;
        this.providerId = providerId;
        this.providerName = providerName;
        this.lastMessage = "Started conversation";
        this.unreadCountCustomer = 0;
        this.unreadCountProvider = 0;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getCustomerName() { return customerName != null ? customerName : ""; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getProviderId() { return providerId; }
    public void setProviderId(String providerId) { this.providerId = providerId; }

    public String getProviderName() { return providerName != null ? providerName : ""; }
    public void setProviderName(String providerName) { this.providerName = providerName; }

    public String getLastMessage() { return lastMessage != null ? lastMessage : ""; }
    public void setLastMessage(String lastMessage) { this.lastMessage = lastMessage; }

    public Date getLastTimestamp() { return lastTimestamp; }
    public void setLastTimestamp(Date lastTimestamp) { this.lastTimestamp = lastTimestamp; }

    public int getUnreadCountCustomer() { return unreadCountCustomer; }
    public void setUnreadCountCustomer(int unreadCountCustomer) { this.unreadCountCustomer = unreadCountCustomer; }

    public int getUnreadCountProvider() { return unreadCountProvider; }
    public void setUnreadCountProvider(int unreadCountProvider) { this.unreadCountProvider = unreadCountProvider; }
}
