package com.abha.enms.utils;

import com.abha.enms.leadmanager.models.Contact;
import com.abha.enms.leadmanager.models.ContactDetails;
import com.abha.enms.leadmanager.models.CustomFields;
import com.abha.enms.leadmanager.models.Lead;
import com.abha.enms.leadmanager.models.LeadAddress;
import com.abha.enms.leadmanager.models.LeadImportDetails;
import com.abha.enms.leadmanager.models.LeadImportHistory;
import com.abha.sharedlibrary.enms.enums.ContactType;
import com.abha.sharedlibrary.enms.request.ContactRequest;
import com.abha.sharedlibrary.enms.request.ContactTypeRequest;
import com.abha.sharedlibrary.enms.request.CustomRequest;
import com.abha.sharedlibrary.enms.request.LeadRequest;
import com.abha.sharedlibrary.enms.response.LeadResponse;
import com.abha.sharedlibrary.enms.response.LeadResponseData;
import com.abha.sharedlibrary.shared.common.request.AddressRequest;
import com.abha.sharedlibrary.shared.common.response.PaginationResponse;
import com.abha.sharedlibrary.shared.enums.OperationStatus;
import com.abha.sharedlibrary.shared.enums.Status;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.util.CollectionUtils;

public class ObjectMapperUtil {
    public static Lead mapToSaveLead(LeadRequest leadRequest, String userId, Long subscriberId) {
        Lead lead = Lead.builder()
                .companyName(leadRequest.getName())
                .url(leadRequest.getUrl())
                .companyDesc(leadRequest.getDescription())
                .source(leadRequest.getSource())
                .status(Status.ACTIVE)
                .leadStatus(leadRequest.getLeadStatus())
                .createdBy(userId)
                .subscriberId(subscriberId)
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
                .isPrimary(contactRequest.isPrimary())
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

    public static LeadResponseData mapToLeadResponse(Page<Lead> page) {
        if (Objects.isNull(page)) {
            return LeadResponseData.builder()
                    .leadResponseList(Collections.emptyList())
                    .paginationResponse(PaginationResponse.builder()
                            .pageNumber(0)
                            .pageNumber(0)
                            .totalCount(0)
                            .build())
                    .build();
        }
        return LeadResponseData.builder()
                .leadResponseList(mapToLeadResponse(page.getContent()))
                .paginationResponse(PaginationResponse.builder()
                        .totalCount(page.getTotalElements())
                        .pageNumber(page.getNumber() + 1)
                        .pageSize(page.getTotalPages())
                        .build())
                .build();
    }

    private static List<LeadResponse> mapToLeadResponse(List<Lead> leadList) {
        if (CollectionUtils.isEmpty(leadList)) {
            return new ArrayList<>();
        }
        return leadList.stream().map(ObjectMapperUtil::mapToLeadResponse)
                .collect(Collectors.toList());
    }

    private static LeadResponse mapToLeadResponse(Lead lead) {
        Contact contact = getPrimaryContact(lead.getContacts());
        List<ContactDetails> contactDetails = contact.getContactDetails();
        return LeadResponse.builder()
                .id(lead.getId())
                .companyName(lead.getCompanyName())
                .leadStatus(lead.getLeadStatus())
                .contactName(contact.getName())
                .emailAddress(getContactValue(contactDetails, ContactType.EMAIL))
                .phoneNumber(getContactValue(contactDetails, ContactType.MOBILE))
                .build();
    }

    private static String getContactValue(
            List<ContactDetails> contactDetails, ContactType contactType) {
        if (CollectionUtils.isEmpty(contactDetails)) {
            return null;
        }
        return contactDetails.stream()
                .filter(cd -> contactType.equals(cd.getContactType()))
                .map(ContactDetails::getValue)
                .findFirst().orElse(null);
    }

    private static Contact getPrimaryContact(List<Contact> contacts) {
        if (CollectionUtils.isEmpty(contacts)) {
            return new Contact();
        }
        return contacts.stream()
                .filter(Contact::isPrimary)
                .findFirst()
                .orElse(contacts.get(0));
    }

    public static LeadImportHistory mapToLeadImportHistory(
            List<Lead> leadList, String fileName, String createdBy,
            Map<String, String>  excelErrorResponse, Long subscriberId) {
        if (CollectionUtils.isEmpty(leadList)) {
            return null;
        }
        LeadImportHistory leadImportHistory = LeadImportHistory.builder()
                .operationStatus(OperationStatus.SUCCESS)
                .fileName(fileName)
                .failed(excelErrorResponse.size())
                .success(leadList.size())
                .status(Status.ACTIVE)
                .createdBy(createdBy)
                .subscriberId(subscriberId)
                .build();
        leadImportHistory.setLeadImportDetails(mapToLeadImportDetails(
                leadImportHistory, leadList));
        return leadImportHistory;
    }

  private static List<LeadImportDetails> mapToLeadImportDetails(
          LeadImportHistory leadImportHistory, List<Lead> leadList) {
    return leadList.stream()
            .map(lead -> mapToLeadImportDetails(lead, leadImportHistory))
            .collect(Collectors.toList());
  }

  private static LeadImportDetails mapToLeadImportDetails(
          Lead lead, LeadImportHistory leadImportHistory) {
      return LeadImportDetails.builder()
              .leadId(lead.getId())
              .leadImportHistory(leadImportHistory)
              .status(Status.ACTIVE)
              .build();
  }

  public static LeadImportHistory mapToFailLeadImportHHistory(
          String originalFilename, String createdBy, Long subscriberId) {
      return LeadImportHistory.builder()
              .fileName(originalFilename)
              .operationStatus(OperationStatus.FAIL)
              .status(Status.ACTIVE)
              .createdBy(createdBy)
              .subscriberId(subscriberId)
              .build();
  }
}
