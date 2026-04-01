package com.parking.service;

import com.parking.dto.ParkingSlotRequest;
import com.parking.entity.ParkingLot;
import com.parking.entity.ParkingSlot;
import com.parking.entity.SlotStatus;
import com.parking.exception.ResourceNotFoundException;
import com.parking.repository.ParkingLotRepository;
import com.parking.repository.ParkingSlotRepository;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Service for parking slot management including availability tracking.
 */
@Service
public class ParkingSlotService {

    private final ParkingSlotRepository slotRepository;
    private final ParkingLotRepository lotRepository;

    public ParkingSlotService(ParkingSlotRepository slotRepository, ParkingLotRepository lotRepository) {
        this.slotRepository = slotRepository;
        this.lotRepository = lotRepository;
    }

    public List<ParkingSlot> getAllSlots() {
        return slotRepository.findAll();
    }

    public List<ParkingSlot> getAvailableSlots() {
        return slotRepository.findByStatus(SlotStatus.AVAILABLE);
    }

    public List<ParkingSlot> getSlotsByLot(Long lotId) {
        return slotRepository.findByParkingLotId(lotId);
    }

    public ParkingSlot getSlotById(Long id) {
        return slotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Parking slot not found with id: " + id));
    }

    public ParkingSlot createSlot(ParkingSlotRequest request) {
        ParkingLot lot = lotRepository.findById(request.getParkingLotId())
                .orElseThrow(() -> new ResourceNotFoundException("Parking lot not found"));

        ParkingSlot slot = ParkingSlot.builder()
                .slotNumber(request.getSlotNumber())
                .status(SlotStatus.AVAILABLE)
                .parkingLot(lot)
                .build();
        return slotRepository.save(slot);
    }

    public ParkingSlot updateSlot(Long id, ParkingSlotRequest request) {
        ParkingSlot slot = getSlotById(id);
        slot.setSlotNumber(request.getSlotNumber());
        if (request.getStatus() != null) {
            slot.setStatus(SlotStatus.valueOf(request.getStatus()));
        }
        if (request.getParkingLotId() != null) {
            ParkingLot lot = lotRepository.findById(request.getParkingLotId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parking lot not found"));
            slot.setParkingLot(lot);
        }
        return slotRepository.save(slot);
    }

    public void deleteSlot(Long id) {
        if (!slotRepository.existsById(id)) {
            throw new ResourceNotFoundException("Parking slot not found with id: " + id);
        }
        slotRepository.deleteById(id);
    }

    /** Update slot status (used internally when bookings change) */
    public void updateSlotStatus(Long id, SlotStatus status) {
        ParkingSlot slot = getSlotById(id);
        slot.setStatus(status);
        slotRepository.save(slot);
    }
}
