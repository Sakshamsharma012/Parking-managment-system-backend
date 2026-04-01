package com.parking.dto;

import lombok.*;

/**
 * DTO for dashboard analytics data.
 */
public class DashboardStats {
    private long totalSlots;
    private long availableSlots;
    private long bookedSlots;
    private long maintenanceSlots;
    private long totalBookings;
    private long activeBookings;
    private long totalUsers;
    private long totalLots;

    public DashboardStats() {}

    public long getTotalSlots() { return totalSlots; }
    public void setTotalSlots(long totalSlots) { this.totalSlots = totalSlots; }
    public long getAvailableSlots() { return availableSlots; }
    public void setAvailableSlots(long availableSlots) { this.availableSlots = availableSlots; }
    public long getBookedSlots() { return bookedSlots; }
    public void setBookedSlots(long bookedSlots) { this.bookedSlots = bookedSlots; }
    public long getMaintenanceSlots() { return maintenanceSlots; }
    public void setMaintenanceSlots(long maintenanceSlots) { this.maintenanceSlots = maintenanceSlots; }
    public long getTotalBookings() { return totalBookings; }
    public void setTotalBookings(long totalBookings) { this.totalBookings = totalBookings; }
    public long getActiveBookings() { return activeBookings; }
    public void setActiveBookings(long activeBookings) { this.activeBookings = activeBookings; }
    public long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }
    public long getTotalLots() { return totalLots; }
    public void setTotalLots(long totalLots) { this.totalLots = totalLots; }

    // Manual Builder
    public static DashboardStatsBuilder builder() { return new DashboardStatsBuilder(); }
    public static class DashboardStatsBuilder {
        private long totalSlots;
        private long availableSlots;
        private long bookedSlots;
        private long maintenanceSlots;
        private long totalBookings;
        private long activeBookings;
        private long totalUsers;
        private long totalLots;

        public DashboardStatsBuilder totalSlots(long n) { this.totalSlots = n; return this; }
        public DashboardStatsBuilder availableSlots(long n) { this.availableSlots = n; return this; }
        public DashboardStatsBuilder bookedSlots(long n) { this.bookedSlots = n; return this; }
        public DashboardStatsBuilder maintenanceSlots(long n) { this.maintenanceSlots = n; return this; }
        public DashboardStatsBuilder totalBookings(long n) { this.totalBookings = n; return this; }
        public DashboardStatsBuilder activeBookings(long n) { this.activeBookings = n; return this; }
        public DashboardStatsBuilder totalUsers(long n) { this.totalUsers = n; return this; }
        public DashboardStatsBuilder totalLots(long n) { this.totalLots = n; return this; }
        public DashboardStats build() {
            DashboardStats ds = new DashboardStats();
            ds.setTotalSlots(totalSlots);
            ds.setAvailableSlots(availableSlots);
            ds.setBookedSlots(bookedSlots);
            ds.setMaintenanceSlots(maintenanceSlots);
            ds.setTotalBookings(totalBookings);
            ds.setActiveBookings(activeBookings);
            ds.setTotalUsers(totalUsers);
            ds.setTotalLots(totalLots);
            return ds;
        }
    }
}
