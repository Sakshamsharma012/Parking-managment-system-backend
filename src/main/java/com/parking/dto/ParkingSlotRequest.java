package com.parking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * DTO for creating/updating a parking slot.
 */
public class ParkingSlotRequest {

    @NotBlank(message = "Slot number is required")
    private String slotNumber;

    @NotNull(message = "Parking lot ID is required")
    private Long parkingLotId;

    private String status; // AVAILABLE, BOOKED, MAINTENANCE

    public ParkingSlotRequest() {}
    public ParkingSlotRequest(String slotNumber, Long parkingLotId, String status) {
        this.slotNumber = slotNumber;
        this.parkingLotId = parkingLotId;
        this.status = status;
    }

    public String getSlotNumber() { return slotNumber; }
    public void setSlotNumber(String slotNumber) { this.slotNumber = slotNumber; }
    public Long getParkingLotId() { return parkingLotId; }
    public void setParkingLotId(Long parkingLotId) { this.parkingLotId = parkingLotId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
