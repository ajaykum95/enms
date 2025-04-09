package com.abha.enms.utils;

import static com.abha.sharedlibrary.shared.common.ExceptionUtil.buildException;

import com.abha.enms.exceptions.EnmsExceptions;
import com.abha.enms.leadmanager.models.Contact;
import com.abha.enms.leadmanager.models.ContactDetails;
import com.abha.enms.leadmanager.models.CustomFields;
import com.abha.enms.leadmanager.models.Lead;
import com.abha.enms.leadmanager.models.LeadAddress;
import com.abha.sharedlibrary.enms.enums.ContactType;
import com.abha.sharedlibrary.enms.request.ContactRequest;
import com.abha.sharedlibrary.enms.request.ContactTypeRequest;
import com.abha.sharedlibrary.enms.request.CustomRequest;
import com.abha.sharedlibrary.enms.request.LeadRequest;
import com.abha.sharedlibrary.enms.request.LeadSearchFilter;
import com.abha.sharedlibrary.shared.common.request.AddressRequest;
import com.abha.sharedlibrary.shared.common.request.PaginationRequest;
import com.abha.sharedlibrary.shared.validator.EmailValidator;
import com.abha.sharedlibrary.shared.validator.PhoneValidator;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.RequestEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

public class RequestValidator {
  public static void validateLeadRequest(RequestEntity<LeadRequest> leadRequestEntity) {
    if (Objects.isNull(leadRequestEntity) || Objects.isNull(leadRequestEntity.getBody())) {
      throw buildException(EnmsExceptions.LEAD_REQUEST_MISSING);
    }
    LeadRequest leadRequest = leadRequestEntity.getBody();
    validateLeadRequest(leadRequest);
  }

  private static void validateLeadRequest(LeadRequest leadRequest) {
    if (StringUtils.isEmpty(leadRequest.getSource())) {
      throw buildException(EnmsExceptions.REQUEST_SOURCE_MISSING);
    }
    if (StringUtils.isEmpty(leadRequest.getName())) {
      throw buildException(EnmsExceptions.LEAD_NAME_MISSING);
    }
    if (CollectionUtils.isEmpty(leadRequest.getContacts())) {
      throw buildException(EnmsExceptions.LEAD_CONTACTS_MISSING);
    }
    if (StringUtils.isEmpty(leadRequest.getLeadStatus())) {
      throw buildException(EnmsExceptions.LEAD_STATUS_MISSING);
    }
    validateContacts(leadRequest);
    validateAddresses(leadRequest);
    validateCustomFields(leadRequest);
  }

  private static void validateCustomFields(LeadRequest leadRequest) {
    List<CustomRequest> customRequests = leadRequest.getCustomRequests();
    if (CollectionUtils.isEmpty(customRequests)) {
      return;
    }
    customRequests.forEach(customRequest -> {
      if (StringUtils.isEmpty(customRequest.getFieldName())) {
        throw buildException(EnmsExceptions.CUSTOM_FIELD_MISSING);
      }
      if (StringUtils.isEmpty(customRequest.getFieldValue())) {
        throw buildException(EnmsExceptions.CUSTOM_FIELD_VALUE_MISSING);
      }
    });
  }

  private static void validateAddresses(LeadRequest leadRequest) {
    List<AddressRequest> addresses = leadRequest.getAddresses();
    if (CollectionUtils.isEmpty(addresses)) {
      return;
    }
    long defaultCount = addresses.stream()
        .filter(AddressRequest::isPrimary)
        .count();
    if (defaultCount == 0) {
      throw buildException(EnmsExceptions.MIN_DEFAULT_ADDRESS_REQUIRED);
    } else if (defaultCount > 1) {
      throw buildException(EnmsExceptions.MAX_DEFAULT_ADDRESS_EXCEED);
    }
  }

  private static void validateExcelAddresses(Lead lead) {
    List<LeadAddress> leadAddresses = lead.getLeadAddresses();
    if (CollectionUtils.isEmpty(leadAddresses)) {
      return;
    }
    long defaultCount = leadAddresses.stream()
        .filter(LeadAddress::isPrimary)
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
    long defaultCount = contacts.stream()
        .filter(ContactRequest::isPrimary)
        .count();
    if (defaultCount == 0) {
      throw buildException(EnmsExceptions.MIN_DEFAULT_CONTACT_REQUIRED);
    } else if (defaultCount > 1) {
      throw buildException(EnmsExceptions.MAX_DEFAULT_CONTACT_EXCEED);
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

  public static void validateLeadsRequest(RequestEntity<List<LeadRequest>> leadRequestEntity) {
    if (Objects.isNull(leadRequestEntity) || Objects.isNull(leadRequestEntity.getBody())) {
      throw buildException(EnmsExceptions.LEAD_REQUEST_MISSING);
    }
    List<LeadRequest> leadRequestList = leadRequestEntity.getBody();
    if (leadRequestList.size() > 999) {
      throw buildException(EnmsExceptions.MAX_LEAD_SIZE_EXCEED, 999, leadRequestList.size());
    }
    leadRequestList.forEach(RequestValidator::validateLeadRequest);
  }

  public static void validateImportLeads(MultipartFile file) {
    if (file.isEmpty()) {
      throw buildException(EnmsExceptions.FILE_MISSING);
    }
    String fileName = file.getOriginalFilename();
    if (!fileName.toLowerCase().endsWith(AppConstant.XLSX)
        || !AppConstant.XLSX_CONTENT_TYPE.equalsIgnoreCase(file.getContentType())) {
      throw buildException(EnmsExceptions.INVALID_FILE_FORMAT);
    }
  }

  public static void validateExcelLeadRequest(Lead lead) {
    if (Objects.isNull(lead)) {
      throw buildException(EnmsExceptions.LEAD_REQUEST_MISSING);
    }
    if (StringUtils.isEmpty(lead.getCompanyName())) {
      throw buildException(EnmsExceptions.LEAD_NAME_MISSING);
    }
    if (CollectionUtils.isEmpty(lead.getContacts())) {
      throw buildException(EnmsExceptions.LEAD_CONTACTS_MISSING);
    }
    if (StringUtils.isEmpty(lead.getLeadStatus())) {
      throw buildException(EnmsExceptions.LEAD_STATUS_MISSING);
    }
    validateExcelContacts(lead);
    validateExcelAddresses(lead);
    validateExcelCustomFields(lead);
  }

  private static void validateExcelContacts(Lead lead) {
    List<Contact> contacts = lead.getContacts();
    if (CollectionUtils.isEmpty(contacts)) {
      throw buildException(EnmsExceptions.CONTACT_DETAILS_MISSING);
    }
    long defaultCount = contacts.stream()
        .filter(Contact::isPrimary)
        .count();
    if (defaultCount == 0) {
      throw buildException(EnmsExceptions.MIN_DEFAULT_CONTACT_REQUIRED);
    } else if (defaultCount > 1) {
      throw buildException(EnmsExceptions.MAX_DEFAULT_CONTACT_EXCEED);
    }
    contacts.forEach(contact -> {
      List<ContactDetails> contactDetails = contact.getContactDetails();
      if (CollectionUtils.isEmpty(contactDetails)) {
        throw buildException(EnmsExceptions.CONTACT_DETAILS_MISSING);
      }
      if (StringUtils.isEmpty(contact.getName())) {
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

  private static void validateExcelCustomFields(Lead lead) {
    List<CustomFields> customFields = lead.getCustomFields();
    if (CollectionUtils.isEmpty(customFields)) {
      return;
    }
    customFields.forEach(customRequest -> {
      if (StringUtils.isEmpty(customRequest.getFieldName())) {
        throw buildException(EnmsExceptions.CUSTOM_FIELD_MISSING);
      }
      if (StringUtils.isEmpty(customRequest.getFieldValue())) {
        throw buildException(EnmsExceptions.CUSTOM_FIELD_VALUE_MISSING);
      }
    });
  }

  public static void validateLeadSearchRequest(
          RequestEntity<LeadSearchFilter> leadSearchFilterRequestEntity) {
    if (Objects.isNull(leadSearchFilterRequestEntity)
        || Objects.isNull(leadSearchFilterRequestEntity.getBody())) {
      return;
    }
    LeadSearchFilter leadSearchFilter = leadSearchFilterRequestEntity.getBody();
    PaginationRequest paginationRequest = leadSearchFilter.getPaginationRequest();
    if (Objects.nonNull(paginationRequest)) {
      if (paginationRequest.getPageNumber() <=0 ) {
        throw buildException(EnmsExceptions.INVALID_PAGE_NUMBER);
      }
      if (paginationRequest.getPageSize() == 0) {
        throw buildException(EnmsExceptions.PAGE_SIZE_INVALID);
      }
      if (StringUtils.isEmpty(paginationRequest.getOrderByColumn())) {
        throw buildException(EnmsExceptions.ORDER_BY_COLUMN_MISSING);
      }
      if (Objects.isNull(paginationRequest.getSortOrder())) {
        throw buildException(EnmsExceptions.SORT_ORDER_MISSING);
      }
    }
  }
}
