package com.parking.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Booking entity representing a parking slot reservation.
 * Tracks user, slot, time range, and booking status.
 */
@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "slot_id", nullable = false)
    private ParkingSlot slot;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Booking() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public ParkingSlot getSlot() { return slot; }
    public void setSlot(ParkingSlot slot) { this.slot = slot; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Manual Builder
    public static BookingBuilder builder() { return new BookingBuilder(); }
    public static class BookingBuilder {
        private User user;
        private ParkingSlot slot;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private BookingStatus status;

        public BookingBuilder user(User u) { this.user = u; return this; }
        public BookingBuilder slot(ParkingSlot s) { this.slot = s; return this; }
        public BookingBuilder startTime(LocalDateTime t) { this.startTime = t; return this; }
        public BookingBuilder endTime(LocalDateTime t) { this.endTime = t; return this; }
        public BookingBuilder status(BookingStatus s) { this.status = s; return this; }
        public Booking build() {
            Booking b = new Booking();
            b.setUser(user);
            b.setSlot(slot);
            b.setStartTime(startTime);
            b.setEndTime(endTime);
            b.setStatus(status);
            return b;
        }
    }
}
