package com.plugpro.data.model;

import com.google.firebase.firestore.ServerTimestamp;
import java.io.Serializable;
import java.util.Date;

public class Review implements Serializable {
    private String id;
    private String bookingId;
    private String customerId;
    private String customerName;
    private String customerPhoto;
    private String providerId;
    private float rating;
    private String comment;
    @ServerTimestamp
    private Date createdAt;

    public Review() {}

    public Review(String bookingId, String customerId, String customerName, String customerPhoto,
                  String providerId, float rating, String comment) {
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerPhoto = customerPhoto;
        this.providerId = providerId;
        this.rating = rating;
        this.comment = comment;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getCustomerName() { return customerName != null ? customerName : ""; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerPhoto() { return customerPhoto != null ? customerPhoto : ""; }
    public void setCustomerPhoto(String customerPhoto) { this.customerPhoto = customerPhoto; }

    public String getProviderId() { return providerId; }
    public void setProviderId(String providerId) { this.providerId = providerId; }

    public float getRating() { return rating; }
    public void setRating(float rating) { this.rating = rating; }

    public String getComment() { return comment != null ? comment : ""; }
    public void setComment(String comment) { this.comment = comment; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
