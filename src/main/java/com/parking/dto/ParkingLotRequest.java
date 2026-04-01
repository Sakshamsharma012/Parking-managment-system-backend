package com.parking.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * DTO for creating/updating a parking lot.
 */
public class ParkingLotRequest {

    @NotBlank(message = "Lot name is required")
    private String name;

    @NotBlank(message = "Location is required")
    private String location;

    public ParkingLotRequest() {}
    public ParkingLotRequest(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
}
