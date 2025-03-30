package com.abha.enms.utils;

import com.abha.enms.leadmanager.models.Contact;
import com.abha.enms.leadmanager.models.ContactDetails;
import com.abha.enms.leadmanager.models.CustomFields;
import com.abha.enms.leadmanager.models.Lead;
import com.abha.enms.leadmanager.models.LeadAddress;
import com.abha.sharedlibrary.enms.request.ContactRequest;
import com.abha.sharedlibrary.enms.request.ContactTypeRequest;
import com.abha.sharedlibrary.enms.request.CustomRequest;
import com.abha.sharedlibrary.enms.request.LeadRequest;
import com.abha.sharedlibrary.shared.common.request.AddressRequest;
import com.abha.sharedlibrary.shared.enums.Status;
import java.util.ArrayList;
import java.util.List;
import org.springframework.util.CollectionUtils;

public class ObjectMapperUtil {
  public static Lead mapToSaveLead(LeadRequest leadRequest, String userId) {
    Lead lead = Lead.builder()
        .companyName(leadRequest.getName())
        .url(leadRequest.getUrl())
        .companyDesc(leadRequest.getDescription())
        .source(leadRequest.getSource())
        .status(Status.ACTIVE)
        .createdBy(userId)
        .build();
    mapToLeadContacts(lead, leadRequest, userId);
    mapToLeadAddress(lead, leadRequest, userId);
    mapToLeadCustomFields(lead, leadRequest, userId);
    return lead;
  }

  private static void mapToLeadCustomFields(Lead lead, LeadRequest leadRequest, String userId) {
    List<CustomRequest> customRequests = leadRequest.getCustomRequests();
    if (CollectionUtils.isEmpty(customRequests)) {
      return;
    }
    List<CustomFields> customFields = customRequests.stream()
        .map(customRequest -> mapToCustomLead(lead, customRequest, userId))
        .toList();
    lead.setCustomFields(customFields);
  }

  private static CustomFields mapToCustomLead(Lead lead, CustomRequest customRequest, String userId) {
    return CustomFields.builder()
        .fieldName(customRequest.getFieldName())
        .fieldValue(customRequest.getFieldValue())
        .status(Status.ACTIVE)
        .createdBy(userId)
        .lead(lead)
        .build();
  }

  private static void mapToLeadAddress(Lead lead, LeadRequest leadRequest, String userId) {
    List<AddressRequest> addresses = leadRequest.getAddresses();
    if (CollectionUtils.isEmpty(addresses)) {
      return;
    }
    List<LeadAddress> leadAddresses = addresses.stream()
        .map(address -> mapToSaveLeadAddress(lead, address, userId))
        .toList();
    lead.setLeadAddresses(leadAddresses);
  }

  private static LeadAddress mapToSaveLeadAddress(
      Lead lead, AddressRequest addressRequest, String userId) {
    return LeadAddress.builder()
        .addressType(addressRequest.getAddressType())
        .isPrimary(addressRequest.isPrimary())
        .addressLine1(addressRequest.getAddressLine1())
        .addressLine2(addressRequest.getAddressLine2())
        .city(addressRequest.getCity())
        .state(addressRequest.getState())
        .zipcode(addressRequest.getZipcode())
        .country(addressRequest.getCountry())
        .lead(lead)
        .status(Status.ACTIVE)
        .createdBy(userId)
        .build();
  }

  private static void mapToLeadContacts(Lead lead, LeadRequest leadRequest, String userId) {
    List<ContactRequest> contactRequests = leadRequest.getContacts();
    if (CollectionUtils.isEmpty(contactRequests)) {
      return;
    }
    List<Contact> contactList = contactRequests.stream()
        .map(contactRequest -> mapToContact(contactRequest, userId, lead))
        .toList();
    lead.setContacts(contactList);
  }

  private static Contact mapToContact(ContactRequest contactRequest, String userId, Lead lead) {
    Contact contact = Contact.builder()
        .name(contactRequest.getName())
        .title(contactRequest.getTitle())
        .status(Status.ACTIVE)
        .createdBy(userId)
        .lead(lead)
        .build();
    contact.setContactDetails(mapToContactDetails(contact, contactRequest));
    return contact;
  }

  private static List<ContactDetails> mapToContactDetails(Contact contact, ContactRequest contactRequest) {
    List<ContactTypeRequest> contactDetails = contactRequest.getContactDetails();
    if (CollectionUtils.isEmpty(contactDetails)) {
      return new ArrayList<>();
    }
    return contactDetails.stream()
        .map(cd -> mapToContactDetail(cd, contact))
        .toList();
  }

  private static ContactDetails mapToContactDetail(
      ContactTypeRequest contactTypeRequest, Contact contact) {
    return ContactDetails.builder()
        .contactType(contactTypeRequest.getContactType())
        .value(contactTypeRequest.getValue())
        .status(Status.ACTIVE)
        .contact(contact)
        .build();
  }
}
