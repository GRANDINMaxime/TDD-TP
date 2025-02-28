package com.example.services;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public void sendReminderEmail(String memberEmail) {
        System.out.println("Email envoyé à " + memberEmail);
    }
}