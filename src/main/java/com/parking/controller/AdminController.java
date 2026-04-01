package com.parking.controller;

import com.parking.dto.*;
import com.parking.entity.*;
import com.parking.repository.UserRepository;
import com.parking.service.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * REST controller for admin operations.
 * All endpoints require ADMIN role.
 */
@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:5173")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final ParkingLotService lotService;
    private final ParkingSlotService slotService;
    private final BookingService bookingService;
    private final DashboardService dashboardService;
    private final UserRepository userRepository;

    public AdminController(
            ParkingLotService lotService,
            ParkingSlotService slotService,
            BookingService bookingService,
            DashboardService dashboardService,
            UserRepository userRepository
    ) {
        this.lotService = lotService;
        this.slotService = slotService;
        this.bookingService = bookingService;
        this.dashboardService = dashboardService;
        this.userRepository = userRepository;
    }

    // === Dashboard ===

    /** Get dashboard statistics */
    @GetMapping("/stats")
    public ResponseEntity<DashboardStats> getStats() {
        return ResponseEntity.ok(dashboardService.getStats());
    }

    // === Parking Lots ===

    @GetMapping("/lots")
    public ResponseEntity<List<Map<String, Object>>> getAllLots() {
        return ResponseEntity.ok(lotService.getAllLots().stream()
                .map(this::mapLot).collect(Collectors.toList()));
    }

    @PostMapping("/lots")
    public ResponseEntity<Map<String, Object>> createLot(@Valid @RequestBody ParkingLotRequest request) {
        return ResponseEntity.ok(mapLot(lotService.createLot(request)));
    }

    @PutMapping("/lots/{id}")
    public ResponseEntity<Map<String, Object>> updateLot(
            @PathVariable Long id, @Valid @RequestBody ParkingLotRequest request) {
        return ResponseEntity.ok(mapLot(lotService.updateLot(id, request)));
    }

    @DeleteMapping("/lots/{id}")
    public ResponseEntity<Map<String, String>> deleteLot(@PathVariable Long id) {
        lotService.deleteLot(id);
        return ResponseEntity.ok(Map.of("message", "Parking lot deleted successfully"));
    }

    // === Parking Slots ===

    @GetMapping("/slots")
    public ResponseEntity<List<Map<String, Object>>> getAllSlots() {
        return ResponseEntity.ok(slotService.getAllSlots().stream()
                .map(this::mapSlot).collect(Collectors.toList()));
    }

    @PostMapping("/slots")
    public ResponseEntity<Map<String, Object>> createSlot(@Valid @RequestBody ParkingSlotRequest request) {
        return ResponseEntity.ok(mapSlot(slotService.createSlot(request)));
    }

    @PutMapping("/slots/{id}")
    public ResponseEntity<Map<String, Object>> updateSlot(
            @PathVariable Long id, @Valid @RequestBody ParkingSlotRequest request) {
        return ResponseEntity.ok(mapSlot(slotService.updateSlot(id, request)));
    }

    @DeleteMapping("/slots/{id}")
    public ResponseEntity<Map<String, String>> deleteSlot(@PathVariable Long id) {
        slotService.deleteSlot(id);
        return ResponseEntity.ok(Map.of("message", "Parking slot deleted successfully"));
    }

    // === Bookings ===

    @GetMapping("/bookings")
    public ResponseEntity<List<Map<String, Object>>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings().stream()
                .map(this::mapBooking).collect(Collectors.toList()));
    }

    // === Users ===

    @GetMapping("/users")
    public ResponseEntity<List<Map<String, Object>>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll().stream()
                .map(this::mapUser).collect(Collectors.toList()));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
    }

    // === Mapping Helpers ===

    private Map<String, Object> mapLot(ParkingLot lot) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", lot.getId());
        map.put("name", lot.getName());
        map.put("location", lot.getLocation());
        map.put("totalSlots", lot.getSlots() != null ? lot.getSlots().size() : 0);
        return map;
    }

    private Map<String, Object> mapSlot(ParkingSlot slot) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", slot.getId());
        map.put("slotNumber", slot.getSlotNumber());
        map.put("status", slot.getStatus().name());
        Map<String, Object> lotMap = new HashMap<>();
        lotMap.put("id", slot.getParkingLot().getId());
        lotMap.put("name", slot.getParkingLot().getName());
        lotMap.put("location", slot.getParkingLot().getLocation());
        map.put("parkingLot", lotMap);
        return map;
    }

    private Map<String, Object> mapBooking(Booking booking) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", booking.getId());
        map.put("startTime", booking.getStartTime().toString());
        map.put("endTime", booking.getEndTime().toString());
        map.put("status", booking.getStatus().name());
        map.put("createdAt", booking.getCreatedAt().toString());
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", booking.getUser().getId());
        userMap.put("name", booking.getUser().getName());
        userMap.put("email", booking.getUser().getEmail());
        map.put("user", userMap);
        Map<String, Object> slotMap = new HashMap<>();
        slotMap.put("id", booking.getSlot().getId());
        slotMap.put("slotNumber", booking.getSlot().getSlotNumber());
        map.put("slot", slotMap);
        return map;
    }

    private Map<String, Object> mapUser(User user) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", user.getId());
        map.put("name", user.getName());
        map.put("email", user.getEmail());
        map.put("role", user.getRole().name());
        return map;
    }
}
