package com.parking.repository;

import com.parking.entity.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for ParkingLot entity operations.
 */
public interface ParkingLotRepository extends JpaRepository<ParkingLot, Long> {
}
