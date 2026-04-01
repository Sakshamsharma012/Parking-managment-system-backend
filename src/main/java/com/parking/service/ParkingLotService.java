package com.parking.service;

import com.parking.dto.ParkingLotRequest;
import com.parking.entity.ParkingLot;
import com.parking.exception.ResourceNotFoundException;
import com.parking.repository.ParkingLotRepository;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Service for parking lot CRUD operations.
 */
@Service
public class ParkingLotService {

    private final ParkingLotRepository lotRepository;

    public ParkingLotService(ParkingLotRepository lotRepository) {
        this.lotRepository = lotRepository;
    }

    public List<ParkingLot> getAllLots() {
        return lotRepository.findAll();
    }

    public ParkingLot getLotById(Long id) {
        return lotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Parking lot not found with id: " + id));
    }

    public ParkingLot createLot(ParkingLotRequest request) {
        ParkingLot lot = ParkingLot.builder()
                .name(request.getName())
                .location(request.getLocation())
                .build();
        return lotRepository.save(lot);
    }

    public ParkingLot updateLot(Long id, ParkingLotRequest request) {
        ParkingLot lot = getLotById(id);
        lot.setName(request.getName());
        lot.setLocation(request.getLocation());
        return lotRepository.save(lot);
    }

    public void deleteLot(Long id) {
        if (!lotRepository.existsById(id)) {
            throw new ResourceNotFoundException("Parking lot not found with id: " + id);
        }
        lotRepository.deleteById(id);
    }
}
