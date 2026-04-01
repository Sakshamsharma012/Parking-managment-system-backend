package com.parking.service;

import com.parking.dto.SlotRecommendationResponse;
import com.parking.entity.ParkingSlot;
import com.parking.entity.SlotStatus;
import com.parking.entity.SlotType;
import com.parking.repository.BookingRepository;
import com.parking.repository.ParkingSlotRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Smart recommendation engine that suggests the best available parking slot
 * based on multi-factor scoring. Does NOT modify any existing services.
 *
 * Scoring formula:
 *   score = availabilityWeight(10.0)
 *         - (usageCount * 0.5)
 *         + slotTypeBonus (5.0 if matching vehicleType)
 *         + lowUsageBonus (3.0 if never used)
 */
@Service
public class RecommendationService {

    private final ParkingSlotRepository slotRepository;
    private final BookingRepository bookingRepository;

    // Scoring weights
    private static final double BASE_AVAILABILITY_SCORE = 10.0;
    private static final double USAGE_PENALTY_WEIGHT = 0.5;
    private static final double VEHICLE_TYPE_MATCH_BONUS = 5.0;
    private static final double LOW_USAGE_BONUS = 3.0;
    private static final double FRESH_SLOT_BONUS = 2.0;

    public RecommendationService(
            ParkingSlotRepository slotRepository,
            BookingRepository bookingRepository
    ) {
        this.slotRepository = slotRepository;
        this.bookingRepository = bookingRepository;
    }

    /**
     * Get the best recommended parking slot based on intelligent scoring.
     *
     * @param vehicleType optional vehicle type filter (CAR, BIKE, TRUCK, or null for any)
     * @return recommendation response with slot details, score, and reasons
     */
    public SlotRecommendationResponse recommend(String vehicleType) {
        // 1. Get all available slots
        List<ParkingSlot> availableSlots = slotRepository.findByStatus(SlotStatus.AVAILABLE);

        if (availableSlots.isEmpty()) {
            return null; // No available slots
        }

        // 2. Parse vehicle type (if provided)
        SlotType requestedType = null;
        if (vehicleType != null && !vehicleType.isBlank()) {
            try {
                requestedType = SlotType.valueOf(vehicleType.toUpperCase());
            } catch (IllegalArgumentException ignored) {
                // Invalid type — treat as ANY
            }
        }

        // 3. Score each available slot
        final SlotType finalRequestedType = requestedType;
        List<ScoredSlot> scoredSlots = availableSlots.stream()
                .map(slot -> scoreSlot(slot, finalRequestedType))
                .sorted(Comparator.comparingDouble(ScoredSlot::score).reversed())
                .collect(Collectors.toList());

        // 4. Return the top-scored slot
        ScoredSlot best = scoredSlots.get(0);
        ParkingSlot slot = best.slot();

        Map<String, Object> lotMap = new HashMap<>();
        lotMap.put("id", slot.getParkingLot().getId());
        lotMap.put("name", slot.getParkingLot().getName());
        lotMap.put("location", slot.getParkingLot().getLocation());

        return SlotRecommendationResponse.builder()
                .id(slot.getId())
                .slotNumber(slot.getSlotNumber())
                .status(slot.getStatus().name())
                .slotType(slot.getSlotType() != null ? slot.getSlotType().name() : "ANY")
                .usageCount(slot.getUsageCount())
                .score(Math.round(best.score() * 100.0) / 100.0)
                .parkingLot(lotMap)
                .reasons(best.reasons())
                .build();
    }

    /**
     * Calculate a recommendation score for a single slot.
     */
    private ScoredSlot scoreSlot(ParkingSlot slot, SlotType requestedType) {
        double score = BASE_AVAILABILITY_SCORE;
        List<String> reasons = new ArrayList<>();

        reasons.add("✅ Slot is currently available");

        // --- Usage frequency penalty (less used = better) ---
        int usageCount = slot.getUsageCount();
        // Also check actual booking count from repository
        long actualBookings = bookingRepository.countBySlotId(slot.getId());
        int effectiveUsage = Math.max(usageCount, (int) actualBookings);

        if (effectiveUsage == 0) {
            score += LOW_USAGE_BONUS;
            reasons.add("🌟 Fresh slot — never booked before (+bonus)");
        } else {
            double penalty = effectiveUsage * USAGE_PENALTY_WEIGHT;
            score -= penalty;
            reasons.add(String.format("📊 Used %d time(s) — less busy slots preferred", effectiveUsage));
        }

        // --- Vehicle type compatibility ---
        SlotType slotType = slot.getSlotType() != null ? slot.getSlotType() : SlotType.ANY;
        if (requestedType != null && requestedType != SlotType.ANY) {
            if (slotType == requestedType) {
                score += VEHICLE_TYPE_MATCH_BONUS;
                reasons.add(String.format("🚗 Perfect match for %s vehicle type (+bonus)", requestedType.name()));
            } else if (slotType == SlotType.ANY) {
                score += FRESH_SLOT_BONUS;
                reasons.add("🔄 Universal slot — fits any vehicle type");
            } else {
                reasons.add(String.format("⚠️ Slot is designated for %s (you requested %s)", slotType.name(), requestedType.name()));
            }
        } else {
            if (slotType == SlotType.ANY) {
                score += FRESH_SLOT_BONUS;
                reasons.add("🔄 Universal slot — fits any vehicle type");
            }
        }

        // --- Slot position bonus (lower numbered slots preferred — closer to entrance) ---
        String slotNum = slot.getSlotNumber();
        try {
            int numPart = Integer.parseInt(slotNum.replaceAll("[^0-9]", ""));
            if (numPart <= 3) {
                score += 1.5;
                reasons.add("📍 Near entrance — convenient location (+bonus)");
            }
        } catch (NumberFormatException ignored) {
            // Can't parse — no position bonus
        }

        return new ScoredSlot(slot, score, reasons);
    }

    /**
     * Internal record for holding a slot with its computed score and reasons.
     */
    private record ScoredSlot(ParkingSlot slot, double score, List<String> reasons) {}
}
