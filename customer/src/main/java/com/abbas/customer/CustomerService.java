package com.abbas.customer;

import com.abbas.amq.RabbitMQMessageProducer;
import lombok.AllArgsConstructor;
import com.abbas.clients.fraud.FraudCheckResponse;
import com.abbas.clients.fraud.FraudClient;
import com.abbas.clients.notification.NotificationClient;
import com.abbas.clients.notification.NotificationRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class CustomerService{

    private final CustomerRepository customerRepository;

    private final RestTemplate restTemplate;

    private final FraudClient fraudClient;

    private final NotificationClient notificationClient;

    private final RabbitMQMessageProducer rabbitMQMessageProducer;

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


        if(fraudCheckResponse.isFraudster()){
            throw new IllegalStateException("fraudster user");
        }

        NotificationRequest notificationRequest = new NotificationRequest(
                customer.getId(),
                customer.getEmail(),
                String.format("Hi %s, Welcome to this Site.",
                        customer.getFirstName())
        );

        rabbitMQMessageProducer.publish(
                notificationRequest,
                "internal.exchange",
                "internal.notification.routing-key"
                );

    }
}
