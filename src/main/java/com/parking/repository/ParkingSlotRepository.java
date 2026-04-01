package com.parking.repository;

import com.parking.entity.ParkingSlot;
import com.parking.entity.SlotStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repository for ParkingSlot entity operations.
 */
public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {
    List<ParkingSlot> findByStatus(SlotStatus status);
    List<ParkingSlot> findByParkingLotId(Long parkingLotId);
    long countByStatus(SlotStatus status);
}
