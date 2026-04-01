package com.parking.repository;

import com.parking.entity.Booking;
import com.parking.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for Booking entity operations.
 */
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Booking> findByStatus(BookingStatus status);

    long countByStatus(BookingStatus status);

    /** Count total bookings for a specific slot (used by recommendation engine) */
    long countBySlotId(Long slotId);

    /**
     * Check for overlapping active bookings on the same slot.
     * Used to prevent double-booking.
     */
    @Query("SELECT COUNT(b) > 0 FROM Booking b WHERE b.slot.id = :slotId " +
           "AND b.status = 'ACTIVE' " +
           "AND b.startTime < :endTime AND b.endTime > :startTime")
    boolean existsOverlappingBooking(
        @Param("slotId") Long slotId,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime
    );
}
