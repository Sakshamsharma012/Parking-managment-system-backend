package com.parking.controller;

import com.parking.dto.SlotRecommendationResponse;
import com.parking.entity.ParkingSlot;
import com.parking.service.ParkingSlotService;
import com.parking.service.RecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Map;

/**
 * REST controller for parking slot viewing (user-facing).
 * All endpoints require authentication.
 */
@RestController
@RequestMapping("/api/slots")
public class ParkingSlotController {

    private final ParkingSlotService slotService;
    private final RecommendationService recommendationService;

    public ParkingSlotController(ParkingSlotService slotService, RecommendationService recommendationService) {
        this.slotService = slotService;
        this.recommendationService = recommendationService;
    }

    /** Get all parking slots with lot info */
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllSlots() {
        return ResponseEntity.ok(slotService.getAllSlots().stream()
                .map(this::mapSlot).collect(Collectors.toList()));
    }

    /** Get only available parking slots */
    @GetMapping("/available")
    public ResponseEntity<List<Map<String, Object>>> getAvailableSlots() {
        return ResponseEntity.ok(slotService.getAvailableSlots().stream()
                .map(this::mapSlot).collect(Collectors.toList()));
    }

    /** Get slots by parking lot ID */
    @GetMapping("/lot/{lotId}")
    public ResponseEntity<List<Map<String, Object>>> getSlotsByLot(@PathVariable Long lotId) {
        return ResponseEntity.ok(slotService.getSlotsByLot(lotId).stream()
                .map(this::mapSlot).collect(Collectors.toList()));
    }

    /**
     * Smart Slot Recommendation — suggests the best available slot
     * based on intelligent scoring (usage frequency, vehicle type, position).
     */
    @GetMapping("/recommend")
    public ResponseEntity<SlotRecommendationResponse> recommendSlot(
            @RequestParam(required = false) String vehicleType
    ) {
        SlotRecommendationResponse recommendation = recommendationService.recommend(vehicleType);
        if (recommendation == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(recommendation);
    }

    /** Map slot entity to response (avoid circular references) */
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
}

