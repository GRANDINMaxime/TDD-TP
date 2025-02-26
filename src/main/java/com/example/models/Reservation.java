package com.example.models;

import java.time.LocalDate;

public class Reservation {

    private Long id;
    private Book book;
    private Member member;
    private LocalDate reservationDate;
    private LocalDate dueDate;
    private ReservationStatus status;

    public Reservation(Long id, Book book, Member member, LocalDate reservationDate, LocalDate dueDate, ReservationStatus status) {
        this.id = id;
        this.book = book;
        this.member = member;
        this.reservationDate = reservationDate;
        this.dueDate = dueDate;
        this.status = status;
    }

    // Getter methods for all fields
    public Long getId() {
        return id;
    }

    public Book getBook() {
        return book;
    }

    public Member getMember() {
        return member;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }


    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }
}

