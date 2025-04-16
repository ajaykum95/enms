package com.abha.enms.integration.noms;


import com.abha.sharedlibrary.noms.request.SendNotificationRequest;

public interface NotificationService {
  Long sendNotification(SendNotificationRequest sendNotificationRequest);
}
