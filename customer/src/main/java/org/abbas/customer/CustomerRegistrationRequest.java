package org.abbas.customer;

public record CustomerRegistrationRequest(
        String firstName,
        String lastName,
        String email) {
}
