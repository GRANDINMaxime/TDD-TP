package com.example.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Member {

    private final Long id;
    private final String membershipCode;
    private final String lastName;
    private final String firstName;
    private final LocalDate dateOfBirth;
    private final Gender gender;
    private final List<Reservation> reservations;
    private final String email;

    public Member(Long id, String membershipCode, String lastName, String firstName, LocalDate dateOfBirth, Gender gender, String email) {
        this.id = id;
        this.membershipCode = membershipCode;
        this.lastName = lastName;
        this.firstName = firstName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.reservations = new ArrayList<>();
        this.email = email;
    }

    // Getter methods for all fields
    public Long getId() {
        return id;
    }

    public String getMembershipCode() {
        return membershipCode;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    // Method to add a reservation
    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }

    // Method to cancel a reservation
    public void cancelReservation(Reservation reservation) {
        reservations.remove(reservation);
    }

    public String getEmail() {
        return email;
    }
}

