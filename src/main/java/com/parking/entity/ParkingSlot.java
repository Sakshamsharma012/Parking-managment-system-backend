package com.parking.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

/**
 * ParkingSlot entity representing an individual parking space within a lot.
 * Tracks slot number, current status, and associated bookings.
 * Extended with usageCount and slotType for smart recommendation scoring.
 */
@Entity
@Table(name = "parking_slot")
public class ParkingSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String slotNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SlotStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parking_lot_id", nullable = false)
    private ParkingLot parkingLot;

    @OneToMany(mappedBy = "slot", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Booking> bookings;

    // --- New fields for smart recommendation (safe defaults) ---

    /** Tracks how many times this slot has been booked (for recommendation scoring) */
    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private int usageCount = 0;

    /** Vehicle type this slot is designed for (for recommendation filtering) */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(10) DEFAULT 'ANY'")
    private SlotType slotType = SlotType.ANY;

    public ParkingSlot() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSlotNumber() { return slotNumber; }
    public void setSlotNumber(String slotNumber) { this.slotNumber = slotNumber; }
    public SlotStatus getStatus() { return status; }
    public void setStatus(SlotStatus status) { this.status = status; }
    public ParkingLot getParkingLot() { return parkingLot; }
    public void setParkingLot(ParkingLot parkingLot) { this.parkingLot = parkingLot; }
    public List<Booking> getBookings() { return bookings; }
    public void setBookings(List<Booking> bookings) { this.bookings = bookings; }
    public int getUsageCount() { return usageCount; }
    public void setUsageCount(int usageCount) { this.usageCount = usageCount; }
    public SlotType getSlotType() { return slotType; }
    public void setSlotType(SlotType slotType) { this.slotType = slotType; }

    // Manual Builder
    public static ParkingSlotBuilder builder() { return new ParkingSlotBuilder(); }
    public static class ParkingSlotBuilder {
        private String slotNumber;
        private SlotStatus status;
        private ParkingLot parkingLot;
        private int usageCount = 0;
        private SlotType slotType = SlotType.ANY;

        public ParkingSlotBuilder slotNumber(String s) { this.slotNumber = s; return this; }
        public ParkingSlotBuilder status(SlotStatus s) { this.status = s; return this; }
        public ParkingSlotBuilder parkingLot(ParkingLot p) { this.parkingLot = p; return this; }
        public ParkingSlotBuilder usageCount(int u) { this.usageCount = u; return this; }
        public ParkingSlotBuilder slotType(SlotType t) { this.slotType = t; return this; }
        public ParkingSlot build() {
            ParkingSlot ps = new ParkingSlot();
            ps.setSlotNumber(slotNumber);
            ps.setStatus(status);
            ps.setParkingLot(parkingLot);
            ps.setUsageCount(usageCount);
            ps.setSlotType(slotType);
            return ps;
        }
    }
}
