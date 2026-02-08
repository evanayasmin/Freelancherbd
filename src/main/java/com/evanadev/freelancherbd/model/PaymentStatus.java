package com.evanadev.freelancherbd.model;

public enum PaymentStatus {

    PENDING,        // Payment created but not yet processed
    PROCESSING,     // Sent to payment gateway, awaiting confirmation
    COMPLETED,      // Successfully paid to freelancer
    FAILED,         // Payment attempt failed
    REFUNDED,       // Money refunded to employer
    CANCELLED,      // Payment cancelled before processing
    DISPUTED        // Payment under dispute
}
