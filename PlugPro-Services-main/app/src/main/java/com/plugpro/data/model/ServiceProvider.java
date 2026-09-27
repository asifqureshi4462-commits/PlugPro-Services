package com.plugpro.data.model;

import com.google.firebase.firestore.ServerTimestamp;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ServiceProvider implements Serializable {
    private String id;
    private String userId;
    private String name;
    private String profession;
    private String categoryId;
    private int experienceYears;
    private double rating;
    private int reviewCount;
    private int completedJobs;
    private double hourlyRate;
    private String about;
    private String serviceArea;
    private String verifiedStatus; // "pending", "verified", "rejected"
    private String profileImageUrl;
    private boolean isAvailable;
    private List<String> availableDays;
    private List<String> availableTimeSlots;
    private String phone;
    private String email;
    @ServerTimestamp
    private Date createdAt;
    @ServerTimestamp
    private Date updatedAt;

    public ServiceProvider() {
        this.availableDays = new ArrayList<>();
        this.availableTimeSlots = new ArrayList<>();
        this.verifiedStatus = "pending";
        this.isAvailable = true;
        this.rating = 5.0;
        this.reviewCount = 0;
        this.completedJobs = 0;
    }

    public ServiceProvider(String id, String userId, String name, String profession, String categoryId,
                           int experienceYears, double hourlyRate, String about, String serviceArea,
                           String phone, String email) {
        this();
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.profession = profession;
        this.categoryId = categoryId;
        this.experienceYears = experienceYears;
        this.hourlyRate = hourlyRate;
        this.about = about;
        this.serviceArea = serviceArea;
        this.phone = phone;
        this.email = email;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getName() { return name != null ? name : ""; }
    public void setName(String name) { this.name = name; }

    public String getProfession() { return profession != null ? profession : ""; }
    public void setProfession(String profession) { this.profession = profession; }

    public String getCategoryId() { return categoryId != null ? categoryId : ""; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }

    public int getExperienceYears() { return experienceYears; }
    public void setExperienceYears(int experienceYears) { this.experienceYears = experienceYears; }

    public double getRating() { return rating > 0 ? rating : 5.0; }
    public void setRating(double rating) { this.rating = rating; }

    public int getReviewCount() { return reviewCount; }
    public void setReviewCount(int reviewCount) { this.reviewCount = reviewCount; }

    public int getCompletedJobs() { return completedJobs; }
    public void setCompletedJobs(int completedJobs) { this.completedJobs = completedJobs; }

    public double getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(double hourlyRate) { this.hourlyRate = hourlyRate; }

    public String getAbout() { return about != null ? about : ""; }
    public void setAbout(String about) { this.about = about; }

    public String getServiceArea() { return serviceArea != null ? serviceArea : "Metro Area"; }
    public void setServiceArea(String serviceArea) { this.serviceArea = serviceArea; }

    public String getVerifiedStatus() { return verifiedStatus != null ? verifiedStatus : "pending"; }
    public void setVerifiedStatus(String verifiedStatus) { this.verifiedStatus = verifiedStatus; }

    public boolean isVerified() {
        return "verified".equalsIgnoreCase(verifiedStatus);
    }

    public String getProfileImageUrl() { return profileImageUrl != null ? profileImageUrl : ""; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    public List<String> getAvailableDays() {
        return availableDays != null ? availableDays : new ArrayList<>();
    }
    public void setAvailableDays(List<String> availableDays) { this.availableDays = availableDays; }

    public List<String> getAvailableTimeSlots() {
        return availableTimeSlots != null ? availableTimeSlots : new ArrayList<>();
    }
    public void setAvailableTimeSlots(List<String> availableTimeSlots) { this.availableTimeSlots = availableTimeSlots; }

    public String getPhone() { return phone != null ? phone : ""; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email != null ? email : ""; }
    public void setEmail(String email) { this.email = email; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}
