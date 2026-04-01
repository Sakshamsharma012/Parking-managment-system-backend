package com.parking.dto;

import lombok.*;
import java.util.List;
import java.util.Map;

/**
 * DTO for the smart slot recommendation response.
 * Contains the recommended slot details, its computed score,
 * and human-readable reasons explaining the recommendation.
 */
public class SlotRecommendationResponse {
    private Long id;
    private String slotNumber;
    private String status;
    private String slotType;
    private int usageCount;
    private double score;
    private Map<String, Object> parkingLot;
    private List<String> reasons;

    public SlotRecommendationResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSlotNumber() { return slotNumber; }
    public void setSlotNumber(String slotNumber) { this.slotNumber = slotNumber; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getSlotType() { return slotType; }
    public void setSlotType(String slotType) { this.slotType = slotType; }
    public int getUsageCount() { return usageCount; }
    public void setUsageCount(int usageCount) { this.usageCount = usageCount; }
    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }
    public Map<String, Object> getParkingLot() { return parkingLot; }
    public void setParkingLot(Map<String, Object> parkingLot) { this.parkingLot = parkingLot; }
    public List<String> getReasons() { return reasons; }
    public void setReasons(List<String> reasons) { this.reasons = reasons; }

    // Manual Builder
    public static SlotRecommendationResponseBuilder builder() { return new SlotRecommendationResponseBuilder(); }
    public static class SlotRecommendationResponseBuilder {
        private Long id;
        private String slotNumber;
        private String status;
        private String slotType;
        private int usageCount;
        private double score;
        private Map<String, Object> parkingLot;
        private List<String> reasons;

        public SlotRecommendationResponseBuilder id(Long id) { this.id = id; return this; }
        public SlotRecommendationResponseBuilder slotNumber(String s) { this.slotNumber = s; return this; }
        public SlotRecommendationResponseBuilder status(String s) { this.status = s; return this; }
        public SlotRecommendationResponseBuilder slotType(String s) { this.slotType = s; return this; }
        public SlotRecommendationResponseBuilder usageCount(int u) { this.usageCount = u; return this; }
        public SlotRecommendationResponseBuilder score(double s) { this.score = s; return this; }
        public SlotRecommendationResponseBuilder parkingLot(Map<String, Object> p) { this.parkingLot = p; return this; }
        public SlotRecommendationResponseBuilder reasons(List<String> r) { this.reasons = r; return this; }
        public SlotRecommendationResponse build() {
            SlotRecommendationResponse srr = new SlotRecommendationResponse();
            srr.setId(id);
            srr.setSlotNumber(slotNumber);
            srr.setStatus(status);
            srr.setSlotType(slotType);
            srr.setUsageCount(usageCount);
            srr.setScore(score);
            srr.setParkingLot(parkingLot);
            srr.setReasons(reasons);
            return srr;
        }
    }
}
