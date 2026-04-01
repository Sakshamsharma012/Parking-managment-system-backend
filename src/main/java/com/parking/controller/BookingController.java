package com.parking.controller;

import com.parking.dto.BookingRequest;
import com.parking.entity.Booking;
import com.parking.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Map;

/**
 * REST controller for booking operations.
 * All endpoints require authentication.
 */
@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "http://localhost:5173")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /** Create a new booking */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createBooking(
            @Valid @RequestBody BookingRequest request,
            Authentication authentication
    ) {
        Booking booking = bookingService.createBooking(authentication.getName(), request);
        return ResponseEntity.ok(mapBooking(booking));
    }

    /** Cancel an active booking */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> cancelBooking(
            @PathVariable Long id,
            Authentication authentication
    ) {
        Booking booking = bookingService.cancelBooking(id, authentication.getName());
        return ResponseEntity.ok(mapBooking(booking));
    }

    /** Get current user's booking history */
    @GetMapping("/my")
    public ResponseEntity<List<Map<String, Object>>> getMyBookings(Authentication authentication) {
        return ResponseEntity.ok(
                bookingService.getUserBookings(authentication.getName()).stream()
                        .map(this::mapBooking).collect(Collectors.toList())
        );
    }

    /** Map booking entity to response (avoid circular references) */
    private Map<String, Object> mapBooking(Booking booking) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", booking.getId());
        map.put("startTime", booking.getStartTime().toString());
        map.put("endTime", booking.getEndTime().toString());
        map.put("status", booking.getStatus().name());
        map.put("createdAt", booking.getCreatedAt().toString());

        Map<String, Object> slotMap = new HashMap<>();
        slotMap.put("id", booking.getSlot().getId());
        slotMap.put("slotNumber", booking.getSlot().getSlotNumber());
        slotMap.put("status", booking.getSlot().getStatus().name());
        if (booking.getSlot().getParkingLot() != null) {
            Map<String, Object> lotMap = new HashMap<>();
            lotMap.put("id", booking.getSlot().getParkingLot().getId());
            lotMap.put("name", booking.getSlot().getParkingLot().getName());
            lotMap.put("location", booking.getSlot().getParkingLot().getLocation());
            slotMap.put("parkingLot", lotMap);
        }
        map.put("slot", slotMap);

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", booking.getUser().getId());
        userMap.put("name", booking.getUser().getName());
        userMap.put("email", booking.getUser().getEmail());
        map.put("user", userMap);

        return map;
    }
}
