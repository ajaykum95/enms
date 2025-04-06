package com.abha.enms.integration.noms;

import com.abha.sharedlibrary.enms.request.SendNotificationRequest;

public interface NotificationService {
  Long sendNotification(SendNotificationRequest sendNotificationRequest);
}
