package org.abbas.clients.notification;

public record NotificationRequest(
        Integer toCustomerId,
        String toCustomerEmail,
        String message

) {
}
