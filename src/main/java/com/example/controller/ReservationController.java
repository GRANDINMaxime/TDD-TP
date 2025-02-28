package com.example.controller;

import com.example.models.*;
import com.example.services.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/check-availability")
    public ResponseEntity<Boolean> checkAvailability(
            @RequestParam String isbn,
            @RequestParam String date) {
        LocalDate reservationDate = LocalDate.parse(date);
        boolean available = reservationService.checkAvailability(isbn, reservationDate);
        return ResponseEntity.ok(available);
    }

    @PostMapping
    public ResponseEntity<?> makeReservation(
            @RequestParam String isbn,
            @RequestParam String date,
            @RequestParam String memberId) {
        LocalDate reservationDate = LocalDate.parse(date);
        try {
            Reservation reservation = reservationService.makeReservation(isbn, reservationDate, memberId);
            return reservation != null ? ResponseEntity.ok(reservation) : ResponseEntity.badRequest().body("Book is not available.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelReservation(@PathVariable int id) {
        boolean cancelled = reservationService.cancelReservation(id);
        return cancelled ? ResponseEntity.ok("Reservation canceled.") : ResponseEntity.badRequest().body("Cancellation failed.");
    }
}
