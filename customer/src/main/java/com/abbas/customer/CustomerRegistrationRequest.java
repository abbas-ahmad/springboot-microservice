package com.abbas.customer;

public record CustomerRegistrationRequest(
        String firstName,
        String lastName,
        String email) {
}
