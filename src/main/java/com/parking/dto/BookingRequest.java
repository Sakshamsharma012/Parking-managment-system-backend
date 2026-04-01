package com.parking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;

/**
 * DTO for creating a booking request.
 */
public class BookingRequest {

    private Long slotId;
    private Long userId;

    @NotNull(message = "Hours is required")
    private Integer hours;

    public BookingRequest() {}
    public BookingRequest(Long slotId, Long userId, Integer hours) {
        this.slotId = slotId;
        this.userId = userId;
        this.hours = hours;
    }

    public Long getSlotId() { return slotId; }
    public void setSlotId(Long slotId) { this.slotId = slotId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Integer getHours() { return hours; }
    public void setHours(Integer hours) { this.hours = hours; }

    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    private LocalDateTime endTime;

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
}
