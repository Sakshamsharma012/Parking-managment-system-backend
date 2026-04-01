package com.parking.service;

import com.parking.dto.DashboardStats;
import com.parking.entity.BookingStatus;
import com.parking.entity.SlotStatus;
import com.parking.repository.*;
import org.springframework.stereotype.Service;

/**
 * Service for generating dashboard analytics.
 */
@Service
public class DashboardService {

    private final ParkingSlotRepository slotRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ParkingLotRepository lotRepository;

    public DashboardService(
            ParkingSlotRepository slotRepository,
            BookingRepository bookingRepository,
            UserRepository userRepository,
            ParkingLotRepository lotRepository
    ) {
        this.slotRepository = slotRepository;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.lotRepository = lotRepository;
    }

    /** Get aggregated statistics for the dashboard */
    public DashboardStats getStats() {
        return DashboardStats.builder()
                .totalSlots(slotRepository.count())
                .availableSlots(slotRepository.countByStatus(SlotStatus.AVAILABLE))
                .bookedSlots(slotRepository.countByStatus(SlotStatus.BOOKED))
                .maintenanceSlots(slotRepository.countByStatus(SlotStatus.MAINTENANCE))
                .totalBookings(bookingRepository.count())
                .activeBookings(bookingRepository.countByStatus(BookingStatus.ACTIVE))
                .totalUsers(userRepository.count())
                .totalLots(lotRepository.count())
                .build();
    }
}
