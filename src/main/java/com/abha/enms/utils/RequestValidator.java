package com.abha.enms.utils;

import com.abha.enms.exceptions.EnmsExceptions;
import com.abha.sharedlibrary.enms.enums.ContactType;
import com.abha.sharedlibrary.enms.request.ContactRequest;
import com.abha.sharedlibrary.enms.request.ContactTypeRequest;
import com.abha.sharedlibrary.enms.request.LeadRequest;
import com.abha.sharedlibrary.shared.common.request.AddressRequest;
import com.abha.sharedlibrary.shared.validator.EmailValidator;
import com.abha.sharedlibrary.shared.validator.PhoneValidator;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.RequestEntity;
import org.springframework.util.CollectionUtils;

import static com.abha.sharedlibrary.shared.common.ExceptionUtil.buildException;

public class RequestValidator {
  public static void validateWebhookLeadRequest(
      RequestEntity<LeadRequest> webhookLeadRequestEntity) {
    if (Objects.isNull(webhookLeadRequestEntity)) {
      throw buildException(EnmsExceptions.WEBHOOK_REQUEST_MISSING);
    }
    LeadRequest webhookLeadRequest = webhookLeadRequestEntity.getBody();
    validateWebhookLeadRequest(webhookLeadRequest);
  }

  private static void validateWebhookLeadRequest(LeadRequest leadRequest) {
    if (StringUtils.isEmpty(leadRequest.getSource())) {
      throw buildException(EnmsExceptions.REQUEST_SOURCE_MISSING);
    }
    if (StringUtils.isEmpty(leadRequest.getSource())) {
      throw buildException(EnmsExceptions.LEAD_NAME_MISSING);
    }
    if (CollectionUtils.isEmpty(leadRequest.getContacts())) {
      throw buildException(EnmsExceptions.LEAD_CONTACTS_MISSING);
    }
    validateContacts(leadRequest);
    validateAddresses(leadRequest);
  }

  private static void validateAddresses(LeadRequest leadRequest) {
    List<AddressRequest> addresses = leadRequest.getAddresses();
    if (CollectionUtils.isEmpty(addresses)) {
      return;
    }
    long defaultCount = addresses.stream()
        .filter(AddressRequest::isDefault)
        .count();
    if (defaultCount == 0) {
      throw buildException(EnmsExceptions.MIN_DEFAULT_ADDRESS_REQUIRED);
    } else if (defaultCount > 1) {
      throw buildException(EnmsExceptions.MAX_DEFAULT_ADDRESS_EXCEED);
    }
  }

  private static void validateContacts(LeadRequest leadRequest) {
    List<ContactRequest> contacts = leadRequest.getContacts();
    if (CollectionUtils.isEmpty(contacts)) {
      throw buildException(EnmsExceptions.CONTACT_DETAILS_MISSING);
    }
    contacts.forEach(contactRequest -> {
      List<ContactTypeRequest> contactDetails = contactRequest.getContactDetails();
      if (CollectionUtils.isEmpty(contactDetails)) {
        throw buildException(EnmsExceptions.CONTACT_DETAILS_MISSING);
      }
      if (StringUtils.isEmpty(contactRequest.getName())) {
        throw buildException(EnmsExceptions.CONTACT_NAME_MISSING);
      }
      contactDetails.forEach(cd -> {
        if (ContactType.EMAIL.equals(cd.getContactType())
            && !EmailValidator.isValidEmail(cd.getValue())) {
          throw buildException(EnmsExceptions.INVALID_EMAIL_ADDRESS);
        }
        if (ContactType.MOBILE.equals(cd.getContactType())
            && !PhoneValidator.isValidPhone(cd.getValue())) {
          throw buildException(EnmsExceptions.INVALID_PHONE_NUMBER);
        }
      });
    });
  }

  public static void validateWebhookLeadsRequest(
      RequestEntity<List<LeadRequest>> webhookLeadRequestEntity) {
    if (Objects.isNull(webhookLeadRequestEntity)) {
      throw buildException(EnmsExceptions.WEBHOOK_REQUEST_MISSING);
    }
    List<LeadRequest> webhookLeadRequestList = webhookLeadRequestEntity.getBody();
    if (webhookLeadRequestList.size() > 500) {
      throw buildException(EnmsExceptions.MAX_LEAD_SIZE_EXCEED, 500, webhookLeadRequestList.size());
    }
    webhookLeadRequestList.forEach(RequestValidator::validateWebhookLeadRequest);
  }
}
