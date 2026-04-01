package com.parking.config;

import com.parking.entity.*;
import com.parking.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Loads sample data on application startup for demonstration purposes.
 * Creates an admin user, a regular user, parking lots, and parking slots.
 */
@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ParkingLotRepository lotRepository;
    private final ParkingSlotRepository slotRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(
            UserRepository userRepository,
            ParkingLotRepository lotRepository,
            ParkingSlotRepository slotRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.lotRepository = lotRepository;
        this.slotRepository = slotRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Create admin user
        if (!userRepository.existsByEmail("admin@parking.com")) {
            User admin = User.builder()
                    .name("Admin User")
                    .email("admin@parking.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(admin);
        }

        // Create regular user
        if (!userRepository.existsByEmail("user@parking.com")) {
            User user = User.builder()
                    .name("John Doe")
                    .email("user@parking.com")
                    .password(passwordEncoder.encode("user123"))
                    .role(Role.USER)
                    .build();
            userRepository.save(user);
        }

        // Create parking lots
        if (lotRepository.count() == 0) {
            ParkingLot lot1 = ParkingLot.builder()
                    .name("Downtown Parking")
                    .location("123 Main Street, Downtown")
                    .build();
            lotRepository.save(lot1);

            ParkingLot lot2 = ParkingLot.builder()
                    .name("Airport Parking")
                    .location("456 Airport Road, Terminal 1")
                    .build();
            lotRepository.save(lot2);

            ParkingLot lot3 = ParkingLot.builder()
                    .name("Mall Parking")
                    .location("789 Shopping Ave, Level B1")
                    .build();
            lotRepository.save(lot3);

            // Create slots for Downtown Parking
            for (int i = 1; i <= 10; i++) {
                ParkingSlot slot = ParkingSlot.builder()
                        .slotNumber("A" + String.format("%02d", i))
                        .status(i <= 8 ? SlotStatus.AVAILABLE : SlotStatus.MAINTENANCE)
                        .parkingLot(lot1)
                        .build();
                slotRepository.save(slot);
            }

            // Create slots for Airport Parking
            for (int i = 1; i <= 15; i++) {
                ParkingSlot slot = ParkingSlot.builder()
                        .slotNumber("B" + String.format("%02d", i))
                        .status(SlotStatus.AVAILABLE)
                        .parkingLot(lot2)
                        .build();
                slotRepository.save(slot);
            }

            // Create slots for Mall Parking
            for (int i = 1; i <= 8; i++) {
                ParkingSlot slot = ParkingSlot.builder()
                        .slotNumber("C" + String.format("%02d", i))
                        .status(SlotStatus.AVAILABLE)
                        .parkingLot(lot3)
                        .build();
                slotRepository.save(slot);
            }

            System.out.println("=== Sample Data Loaded ===");
            System.out.println("Admin: admin@parking.com / admin123");
            System.out.println("User:  user@parking.com / user123");
            System.out.println("Lots: 3 | Slots: 33");
            System.out.println("==========================");
        }
    }
}
