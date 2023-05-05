package org.abbas.customer;

import lombok.AllArgsConstructor;
import org.abbas.clients.fraud.FraudCheckResponse;
import org.abbas.clients.fraud.FraudClient;
import org.abbas.clients.notification.NotificationClient;
import org.abbas.clients.notification.NotificationRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class CustomerService{

    private final CustomerRepository customerRepository;

    private final RestTemplate restTemplate;

    private final FraudClient fraudClient;

    private final NotificationClient notificationClient;

    public void registerCustomer(CustomerRegistrationRequest request) {
        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();

        customerRepository.saveAndFlush(customer);

        /* using rest template
         *   FraudCheckResponse fraudCheckResponse = restTemplate.getForObject(
         *        "http://FRAUD/api/v1/fraud-check/{customerId}",
         *        FraudCheckResponse.class,
         *        customer.getId());
         */

        FraudCheckResponse fraudCheckResponse = fraudClient.isFraudster(customer.getId());

        assert fraudCheckResponse != null;
        if(fraudCheckResponse.isFraudster()){
            throw new IllegalStateException("fraudster user");
        }

        // Send Notification
        notificationClient.sendNotification(
                new NotificationRequest(
                        customer.getId(),
                        customer.getEmail(),
                        String.format("Hi %s, Welcome to this Site.",
                                customer.getFirstName())
                )
        );
    }
}
