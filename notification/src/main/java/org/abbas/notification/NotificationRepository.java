package org.abbas.notification;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer>{
}
