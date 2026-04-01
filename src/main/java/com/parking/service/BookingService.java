package com.parking.service;

import com.parking.dto.BookingRequest;
import com.parking.entity.*;
import com.parking.exception.ResourceNotFoundException;
import com.parking.exception.SlotAlreadyBookedException;
import com.parking.repository.BookingRepository;
import com.parking.repository.ParkingSlotRepository;
import com.parking.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Service for booking operations with double-booking prevention
 * and automatic slot status management.
 */
@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ParkingSlotRepository slotRepository;
    private final ParkingSlotService slotService;

    public BookingService(
            BookingRepository bookingRepository,
            UserRepository userRepository,
            ParkingSlotRepository slotRepository,
            ParkingSlotService slotService
    ) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.slotRepository = slotRepository;
        this.slotService = slotService;
    }

    /**
     * Create a new booking with double-booking prevention.
     * Automatically updates slot status to BOOKED.
     */
    @Transactional
    public Booking createBooking(String userEmail, BookingRequest request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        ParkingSlot slot = slotRepository.findById(request.getSlotId())
                .orElseThrow(() -> new ResourceNotFoundException("Parking slot not found"));

        // Validate time range
        if (request.getEndTime().isBefore(request.getStartTime())) {
            throw new IllegalArgumentException("End time must be after start time");
        }

        // Check for overlapping bookings (prevent double booking)
        boolean hasOverlap = bookingRepository.existsOverlappingBooking(
                request.getSlotId(), request.getStartTime(), request.getEndTime()
        );
        if (hasOverlap) {
            throw new SlotAlreadyBookedException("This slot is already booked for the selected time period");
        }

        // Create booking
        Booking booking = Booking.builder()
                .user(user)
                .slot(slot)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .status(BookingStatus.ACTIVE)
                .build();

        booking = bookingRepository.save(booking);

        // Update slot status to BOOKED
        slotService.updateSlotStatus(slot.getId(), SlotStatus.BOOKED);

        return booking;
    }

    /**
     * Cancel an active booking. Restores slot to AVAILABLE status.
     */
    @Transactional
    public Booking cancelBooking(Long bookingId, String userEmail) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        // Verify ownership (unless admin)
        if (!booking.getUser().getEmail().equals(userEmail)) {
            throw new IllegalArgumentException("You can only cancel your own bookings");
        }

        if (booking.getStatus() != BookingStatus.ACTIVE) {
            throw new IllegalArgumentException("Only active bookings can be cancelled");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        // Restore slot to AVAILABLE
        slotService.updateSlotStatus(booking.getSlot().getId(), SlotStatus.AVAILABLE);

        return booking;
    }

    /** Get booking history for a specific user */
    public List<Booking> getUserBookings(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return bookingRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
    }

    /** Get all bookings (admin) */
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
}
