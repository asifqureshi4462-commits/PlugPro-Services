package com.plugpro.data.model;

import java.io.Serializable;

public class ServiceCategory implements Serializable {
    private String id;
    private String name;
    private String description;
    private String iconResName;
    private double startingPrice;
    private boolean active;

    public ServiceCategory() {
        this.active = true;
    }

    public ServiceCategory(String id, String name, String description, String iconResName, double startingPrice) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.iconResName = iconResName;
        this.startingPrice = startingPrice;
        this.active = true;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name != null ? name : ""; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description != null ? description : ""; }
    public void setDescription(String description) { this.description = description; }

    public String getIconResName() { return iconResName != null ? iconResName : "ic_services"; }
    public void setIconResName(String iconResName) { this.iconResName = iconResName; }

    public double getStartingPrice() { return startingPrice; }
    public void setStartingPrice(double startingPrice) { this.startingPrice = startingPrice; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
