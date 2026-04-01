package com.parking.service;

import com.parking.dto.ChatRequest;
import com.parking.dto.ChatResponse;
import com.parking.dto.SlotRecommendationResponse;
import com.parking.entity.Booking;
import com.parking.entity.ParkingSlot;
import com.parking.entity.SlotStatus;
import com.parking.entity.User;
import com.parking.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * AI Chatbot Service to process user queries using simple keyword matching.
 * It queries existing services without modifying their core logic.
 */
@Service
public class ChatService {

    private final ParkingSlotService slotService;
    private final BookingService bookingService;
    private final RecommendationService recommendationService;
    private final UserRepository userRepository;

    public ChatService(
            ParkingSlotService slotService,
            BookingService bookingService,
            RecommendationService recommendationService,
            UserRepository userRepository
    ) {
        this.slotService = slotService;
        this.bookingService = bookingService;
        this.recommendationService = recommendationService;
        this.userRepository = userRepository;
    }

    /**
     * Process an incoming chat message and return an appropriate string response.
     */
    public ChatResponse processChat(ChatRequest request) {
        if (request == null || request.getMessage() == null) {
            return new ChatResponse("I didn't quite catch that. How can I help you?");
        }

        String message = request.getMessage().toLowerCase().trim();

        // Keyword: "available" or "availability"
        if (message.contains("available") || message.contains("availability")) {
            return handleAvailableSlots();
        }

        // Keyword: "booked"
        if (message.contains("booked slots")) {
            return handleBookedSlots();
        }

        // Keyword: "my booking" or "status"
        if (message.contains("my booking") || message.contains("status")) {
            return handleMyBookings();
        }

        // Keyword: "recommend"
        if (message.contains("recommend") || message.contains("best slot")) {
            return handleRecommendSlot();
        }

        // Default Fallback
        return new ChatResponse(
            "I'm your AI Parking Assistant! You can ask me things like:\n" +
            "• \"How many available slots are there?\"\n" +
            "• \"Check my booking status\"\n" +
            "• \"Can you recommend a slot?\"\n" +
            "• \"How many booked slots?\""
        );
    }

    private ChatResponse handleAvailableSlots() {
        List<ParkingSlot> available = slotService.getAvailableSlots();
        if (available.isEmpty()) {
            return new ChatResponse("Sorry, there are currently no available parking slots.");
        }
        return new ChatResponse("Good news! There are currently " + available.size() + " available parking slots.");
    }

    private ChatResponse handleBookedSlots() {
        List<ParkingSlot> allSlots = slotService.getAllSlots();
        long bookedCount = allSlots.stream()
            .filter(slot -> slot.getStatus() == SlotStatus.BOOKED)
            .count();
        return new ChatResponse("There are currently " + bookedCount + " slots booked right now.");
    }

    private ChatResponse handleMyBookings() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return new ChatResponse("I couldn't identify your account to fetch bookings.");
        }

        List<Booking> myBookings = bookingService.getUserBookings(email);
        List<Booking> activeBookings = myBookings.stream()
            .filter(b -> b.getStatus() == com.parking.entity.BookingStatus.ACTIVE)
            .toList();

        if (activeBookings.isEmpty()) {
            return new ChatResponse("You don't have any active bookings at the moment.");
        }

        StringBuilder sb = new StringBuilder("You have " + activeBookings.size() + " active booking(s):\n");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM/dd HH:mm");
        for (Booking b : activeBookings) {
            sb.append("• Slot ").append(b.getSlot().getSlotNumber())
              .append(" (").append(b.getStartTime().format(dtf)).append(" to ")
              .append(b.getEndTime().format(dtf)).append(")\n");
        }
        return new ChatResponse(sb.toString().trim());
    }

    private ChatResponse handleRecommendSlot() {
        SlotRecommendationResponse bestSlot = recommendationService.recommend("ANY");
        if (bestSlot == null) {
            return new ChatResponse("Unfortunately, there are no slots available to recommend right now.");
        }
        return new ChatResponse(
            "I recommend booking **Slot " + bestSlot.getSlotNumber() + "**!\n" +
            "It has an AI score of " + bestSlot.getScore() + " pts. Go to the 'Parking Slots' tab to book it."
        );
    }
}
