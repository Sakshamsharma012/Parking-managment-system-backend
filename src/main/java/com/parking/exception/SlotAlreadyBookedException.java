package com.parking.exception;

/**
 * Exception thrown when attempting to book an already booked slot.
 */
public class SlotAlreadyBookedException extends RuntimeException {
    public SlotAlreadyBookedException(String message) {
        super(message);
    }
}
