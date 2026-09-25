package com.plugpro.data.model;

import com.google.firebase.firestore.ServerTimestamp;
import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String role; // "customer", "provider", "admin"
    private String profileImageUrl;
    @ServerTimestamp
    private Date createdAt;
    @ServerTimestamp
    private Date updatedAt;

    // Required empty constructor for Firestore serialization
    public User() {}

    public User(String id, String name, String email, String phone, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.profileImageUrl = "";
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name != null ? name : ""; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email != null ? email : ""; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone != null ? phone : ""; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getRole() { return role != null ? role : "customer"; }
    public void setRole(String role) { this.role = role; }

    public String getProfileImageUrl() { return profileImageUrl != null ? profileImageUrl : ""; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }

    public boolean isProvider() {
        return "provider".equalsIgnoreCase(role);
    }

    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(role);
    }
}
