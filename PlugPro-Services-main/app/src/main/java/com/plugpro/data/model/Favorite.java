package com.plugpro.data.model;

import com.google.firebase.firestore.ServerTimestamp;
import java.io.Serializable;
import java.util.Date;

public class Favorite implements Serializable {
    private String id;
    private String customerId;
    private String providerId;
    @ServerTimestamp
    private Date createdAt;

    public Favorite() {}

    public Favorite(String customerId, String providerId) {
        this.id = customerId + "_" + providerId;
        this.customerId = customerId;
        this.providerId = providerId;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getProviderId() { return providerId; }
    public void setProviderId(String providerId) { this.providerId = providerId; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
