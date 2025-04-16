package com.abha.enms.integration.noms;

import com.abha.sharedlibrary.noms.request.SendNotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService{
  @Override
  public Long sendNotification(SendNotificationRequest sendNotificationRequest) {
    log.error("Notification sent successfully!");
    return 0L;
  }
}
