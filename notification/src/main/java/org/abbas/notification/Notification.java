package org.abbas.notification;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Notification {

    @Id
    private Integer toCustomerId;
    private String toCustomerEmail;
    private String sender;
    private String message;
    private LocalDateTime sentAt;

}
