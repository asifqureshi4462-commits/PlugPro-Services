package com.plugpro.data.model;

import com.google.firebase.firestore.ServerTimestamp;
import java.io.Serializable;
import java.util.Date;

public class Booking implements Serializable {
    public static final String STATUS_PENDING = "Pending";
    public static final String STATUS_CONFIRMED = "Confirmed";
    public static final String STATUS_ACCEPTED = "Accepted";
    public static final String STATUS_ON_THE_WAY = "On The Way";
    public static final String STATUS_STARTED = "Started";
    public static final String STATUS_COMPLETED = "Completed";
    public static final String STATUS_CANCELLED = "Cancelled";
    public static final String STATUS_REJECTED = "Rejected";

    private String id;
    private String customerId;
    private String customerName;
    private String customerPhone;
    private String providerId;
    private String providerName;
    private String providerProfession;
    private String providerPhone;
    private String serviceId;
    private String serviceName;
    private String date;
    private String timeSlot;
    private String address;
    private String problemDescription;
    private String status;
    private double serviceFee;
    private double additionalCharges;
    private double totalAmount;
    private String paymentStatus; // "Pending", "Completed"
    private String paymentMethod; // "Cash on Service", "Online"
    @ServerTimestamp
    private Date createdAt;
    @ServerTimestamp
    private Date updatedAt;

    public Booking() {
        this.status = STATUS_PENDING;
        this.paymentStatus = "Pending";
        this.paymentMethod = "Cash on Service";
        this.additionalCharges = 49.0; // standard platform convenience fee
    }

    public Booking(String customerId, String customerName, String customerPhone,
                   String providerId, String providerName, String providerProfession, String providerPhone,
                   String serviceId, String serviceName, String date, String timeSlot,
                   String address, String problemDescription, double serviceFee) {
        this();
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.providerId = providerId;
        this.providerName = providerName;
        this.providerProfession = providerProfession;
        this.providerPhone = providerPhone;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.date = date;
        this.timeSlot = timeSlot;
        this.address = address;
        this.problemDescription = problemDescription;
        this.serviceFee = serviceFee;
        this.totalAmount = serviceFee + this.additionalCharges;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getCustomerName() { return customerName != null ? customerName : ""; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerPhone() { return customerPhone != null ? customerPhone : ""; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }

    public String getProviderId() { return providerId; }
    public void setProviderId(String providerId) { this.providerId = providerId; }

    public String getProviderName() { return providerName != null ? providerName : ""; }
    public void setProviderName(String providerName) { this.providerName = providerName; }

    public String getProviderProfession() { return providerProfession != null ? providerProfession : ""; }
    public void setProviderProfession(String providerProfession) { this.providerProfession = providerProfession; }

    public String getProviderPhone() { return providerPhone != null ? providerPhone : ""; }
    public void setProviderPhone(String providerPhone) { this.providerPhone = providerPhone; }

    public String getServiceId() { return serviceId; }
    public void setServiceId(String serviceId) { this.serviceId = serviceId; }

    public String getServiceName() { return serviceName != null ? serviceName : ""; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }

    public String getDate() { return date != null ? date : ""; }
    public void setDate(String date) { this.date = date; }

    public String getTimeSlot() { return timeSlot != null ? timeSlot : ""; }
    public void setTimeSlot(String timeSlot) { this.timeSlot = timeSlot; }

    public String getAddress() { return address != null ? address : ""; }
    public void setAddress(String address) { this.address = address; }

    public String getProblemDescription() { return problemDescription != null ? problemDescription : ""; }
    public void setProblemDescription(String problemDescription) { this.problemDescription = problemDescription; }

    public String getStatus() { return status != null ? status : STATUS_PENDING; }
    public void setStatus(String status) { this.status = status; }

    public double getServiceFee() { return serviceFee; }
    public void setServiceFee(double serviceFee) { this.serviceFee = serviceFee; }

    public double getAdditionalCharges() { return additionalCharges; }
    public void setAdditionalCharges(double additionalCharges) { this.additionalCharges = additionalCharges; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getPaymentStatus() { return paymentStatus != null ? paymentStatus : "Pending"; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public String getPaymentMethod() { return paymentMethod != null ? paymentMethod : "Cash on Service"; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}
