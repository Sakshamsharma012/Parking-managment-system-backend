package com.parking.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

/**
 * ParkingLot entity representing a physical parking facility.
 * Each lot contains multiple parking slots.
 */
@Entity
@Table(name = "parking_lot")
public class ParkingLot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    @OneToMany(mappedBy = "parkingLot", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ParkingSlot> slots;

    public ParkingLot() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public List<ParkingSlot> getSlots() { return slots; }
    public void setSlots(List<ParkingSlot> slots) { this.slots = slots; }

    // Manual Builder
    public static ParkingLotBuilder builder() { return new ParkingLotBuilder(); }
    public static class ParkingLotBuilder {
        private String name;
        private String location;

        public ParkingLotBuilder name(String n) { this.name = n; return this; }
        public ParkingLotBuilder location(String l) { this.location = l; return this; }
        public ParkingLot build() {
            ParkingLot pl = new ParkingLot();
            pl.setName(name);
            pl.setLocation(location);
            return pl;
        }
    }
}
