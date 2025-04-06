package com.abha.enms.leadmanager.services.impl;

import com.abha.enms.integration.noms.NotificationService;
import com.abha.enms.leadmanager.daos.LeadDao;
import com.abha.enms.leadmanager.daos.LeadImportDao;
import com.abha.enms.leadmanager.dtos.ExcelLeadDto;
import com.abha.enms.leadmanager.models.Contact;
import com.abha.enms.leadmanager.models.ContactDetails;
import com.abha.enms.leadmanager.models.CustomFields;
import com.abha.enms.leadmanager.models.Lead;
import com.abha.enms.leadmanager.models.LeadAddress;
import com.abha.enms.leadmanager.models.LeadImportHistory;
import com.abha.enms.leadmanager.services.LeadService;
import com.abha.enms.utils.AppConstant;
import com.abha.enms.utils.CommonUtil;
import com.abha.enms.utils.ObjectMapperUtil;
import com.abha.enms.utils.RequestValidator;
import com.abha.sharedlibrary.enms.enums.ContactType;
import com.abha.sharedlibrary.enms.request.LeadRequest;
import com.abha.sharedlibrary.enms.request.LeadSearchFilter;
import com.abha.sharedlibrary.enms.request.SendNotificationRequest;
import com.abha.sharedlibrary.enms.response.LeadResponseData;
import com.abha.sharedlibrary.enms.response.LeadSaveResponse;
import com.abha.sharedlibrary.shared.common.ExcelFileUtil;
import com.abha.sharedlibrary.shared.common.request.PaginationRequest;
import com.abha.sharedlibrary.shared.constants.HeaderConstant;
import com.abha.sharedlibrary.shared.enums.AddressType;
import com.abha.sharedlibrary.shared.enums.SortOrder;
import com.abha.sharedlibrary.shared.enums.Status;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.RequestEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class LeadServiceImpl implements LeadService {

  private final LeadDao leadDao;
  private final Map<String, String>  excelErrorResponse;
  private final Map<String, Lead> excelLeadMap;
  private final NotificationService notificationService;
  private final LeadImportDao leadImportDao;

  @Value("${pagemap.maxPageSize}")
  private int maxPageSize;

  @Autowired
  public LeadServiceImpl(LeadDao leadDao, NotificationService notificationService, LeadImportDao leadImportDao) {
    this.leadDao = leadDao;
    this.notificationService = notificationService;
      this.leadImportDao = leadImportDao;
      this.excelErrorResponse = new HashMap<>();
    this.excelLeadMap = new HashMap<>();
  }

  @Transactional
  @Override
  public LeadSaveResponse saveLeadRequest(RequestEntity<LeadRequest> leadRequestEntity) {
    String userId = CommonUtil.getHeaderData(leadRequestEntity, HeaderConstant.USER_ID);
    Long subscriberId = Long.parseLong(CommonUtil.getHeaderData(leadRequestEntity,
        HeaderConstant.SUBSCRIBER_ID));
    Lead lead = ObjectMapperUtil.mapToSaveLead(leadRequestEntity.getBody(), userId, subscriberId);
    lead.setDuplicateOf(getDuplicateOfId(lead));
    Lead savedLead = leadDao.saveLead(lead);
    return LeadSaveResponse.builder()
        .leadId(savedLead.getId())
        .success(true)
        .message(AppConstant.SAVE_LEAD_MESSAGE)
        .build();
  }

  private Long getDuplicateOfId(Lead lead) {
    List<String> contactValues = lead.getContacts().stream()
        .map(Contact::getContactDetails)
        .flatMap(List::stream)
        .map(ContactDetails::getValue)
        .toList();
    return leadDao.fetchDuplicateLeadByContactDetailsIn(contactValues);
  }

  @Async
  @Override
  public void saveLeadsRequest(RequestEntity<List<LeadRequest>> leadRequestEntity) {
    List<Lead> leadList = mapToLeads(leadRequestEntity);
    leadDao.saveAllLead(leadList);
  }

  @Async
  @Override
  public void importLeads(Map<String, String> headers, MultipartFile file) {
    String createdBy = CommonUtil.getHeaderData(headers, HeaderConstant.USER_ID);
    Long subscriberId = Long.parseLong(CommonUtil.getHeaderData(headers, HeaderConstant.SUBSCRIBER_ID));
    try {
      List<ExcelLeadDto> excelLeadDtos = ExcelFileUtil.readExcel(
          file.getInputStream(), ExcelLeadDto.class);
      if (CollectionUtils.isEmpty(excelLeadDtos)) {
        log.warn("No leads found in the uploaded file.");
        excelErrorResponse.put("0", "No Data Found");
        return;
      }
      for (ExcelLeadDto excelLeadDto : excelLeadDtos) {
        mapLeadExcelObject(excelLeadDto, createdBy, subscriberId);
      }
      processExcelLeadData(file.getOriginalFilename(), createdBy, subscriberId);
    } catch (Exception e) {
      log.error("Error processing the file: {}", e.getMessage(), e);
      saveLeadImportHistory(file.getOriginalFilename(), createdBy, subscriberId);
    } finally {
      notificationService.sendNotification(buildSendNotificationRequest(headers));
      excelErrorResponse.clear();
      excelLeadMap.clear();
    }
  }

  private void saveLeadImportHistory(String originalFilename, String createdBy, Long subscriberId) {
    LeadImportHistory leadImportHistory = ObjectMapperUtil.mapToFailLeadImportHHistory(
            originalFilename, createdBy, subscriberId);
    leadImportDao.saveLeadImport(leadImportHistory);
  }

  @Override
  public LeadResponseData fetchAllLeads(RequestEntity<LeadSearchFilter> leadSearchFilterRequestEntity) {
    Long subscriberId = Long.parseLong(CommonUtil.getHeaderData(leadSearchFilterRequestEntity.getHeaders(),
            HeaderConstant.SUBSCRIBER_ID));
    LeadSearchFilter leadSearchFilter = leadSearchFilterRequestEntity.getBody();
    Pageable pageable = getSearchLeadPageable(leadSearchFilter);
    Page<Lead> page = leadDao.getAllLeadsBySubscriberIdAndStatusNot(
        subscriberId, Status.DELETED, pageable);
    return ObjectMapperUtil.mapToLeadResponse(page);
  }

  @Override
  public List<LeadImportHistory> fetchAllImportHistory(Map<String, String> headers) {
    long subscriberId = Long.parseLong(CommonUtil.getHeaderData(headers, HeaderConstant.SUBSCRIBER_ID));
    return leadImportDao.getAllLeadImportHistory(subscriberId);
  }

  private Pageable getSearchLeadPageable(LeadSearchFilter leadSearchFilter) {
    PaginationRequest paginationRequest = Optional.ofNullable(leadSearchFilter.getPaginationRequest())
        .orElse(new PaginationRequest(0, 10, "id", SortOrder.DESC));

    Sort sort = (SortOrder.ASC.equals(paginationRequest.getSortOrder()))
        ? Sort.by(Sort.Order.asc(paginationRequest.getOrderByColumn()))
        : Sort.by(Sort.Order.desc(paginationRequest.getOrderByColumn()));

    return PageRequest.of(paginationRequest.getPageNumber() -1,
            maxPageSize < paginationRequest.getPageSize()
                    ? maxPageSize : paginationRequest.getPageSize(), sort);
  }

  private SendNotificationRequest buildSendNotificationRequest(Map<String, String> headers) {
    return SendNotificationRequest.builder().build();
  }

  @Transactional
  private void processExcelLeadData(
          String fileName, String createdBy, Long subscriberId) {
    if (MapUtils.isEmpty(excelLeadMap)) {
      return;
    }
    List<Lead> leadList = new ArrayList<>();
    for (Map.Entry<String, Lead> entry : excelLeadMap.entrySet()) {
      try {
        RequestValidator.validateExcelLeadRequest(entry.getValue());
        Lead lead = entry.getValue();
        lead.setDuplicateOf(getDuplicateOfId(lead));
        leadList.add(lead);
      } catch (Exception e) {
        log.error("An error occurred while importing excel lead, Error : {}",
            ExceptionUtils.getStackTrace(e));
        excelErrorResponse.put(entry.getKey(), e.getMessage());
      }
    }
    if (!CollectionUtils.isEmpty(leadList)) {
      leadDao.saveAllLead(leadList);
    }
    LeadImportHistory leadImportHistory = ObjectMapperUtil.mapToLeadImportHistory(
            leadList, fileName, createdBy, excelErrorResponse, subscriberId);
    if (Objects.nonNull(leadImportHistory)) {
      leadImportDao.saveLeadImport(leadImportHistory);
    }
  }

  private void mapLeadExcelObject(ExcelLeadDto excelLeadDto, String createdBy, Long subscriberId) {
    Lead lead = excelLeadMap.compute(excelLeadDto.getSn(), (key, existingLead) -> {
      if (existingLead == null) {
        return createLead(excelLeadDto, createdBy, subscriberId);
      }
      updateLead(existingLead, excelLeadDto);
      return existingLead;
    });
    addContactIfPresent(excelLeadDto, lead, createdBy);
    addLeadAddressIfPresent(excelLeadDto, lead, createdBy);
    addCustomFieldsIfPresent(excelLeadDto, lead, createdBy);
  }

  private void updateLead(Lead existingLead, ExcelLeadDto excelLeadDto) {
    if (StringUtils.isEmpty(existingLead.getCompanyName())) {
      existingLead.setCompanyName(excelLeadDto.getCompanyName());
    }
    if (StringUtils.isEmpty(existingLead.getUrl())) {
      existingLead.setUrl(excelLeadDto.getUrl());
    }
    if (StringUtils.isEmpty(existingLead.getCompanyDesc())) {
      existingLead.setCompanyDesc(excelLeadDto.getCompanyDescription());
    }
    if (StringUtils.isEmpty(existingLead.getLeadStatus())) {
      existingLead.setLeadStatus(excelLeadDto.getLeadStatus());
    }
    if (StringUtils.isEmpty(existingLead.getSource())) {
      existingLead.setSource(excelLeadDto.getSource());
    }
  }

  private Lead createLead(ExcelLeadDto dto, String createdBy, Long subscriberId) {
    return Lead.builder()
        .companyName(dto.getCompanyName())
        .url(dto.getUrl())
        .companyDesc(dto.getCompanyDescription())
        .leadStatus(dto.getLeadStatus())
        .source(dto.getSource())
        .status(Status.ACTIVE)
        .createdBy(createdBy)
        .contacts(new ArrayList<>())
        .leadAddresses(new ArrayList<>())
        .customFields(new ArrayList<>())
        .subscriberId(subscriberId)
        .build();
  }

  private void addContactIfPresent(ExcelLeadDto dto, Lead lead, String createdBy) {
    Optional.ofNullable(mapToContact(dto, lead, createdBy))
        .ifPresent(contact -> lead.getContacts().add(contact));
  }

  private void addLeadAddressIfPresent(ExcelLeadDto dto, Lead lead, String createdBy) {
    Optional.ofNullable(mapToLeadAddress(dto, lead, createdBy))
        .ifPresent(address -> lead.getLeadAddresses().add(address));
  }

  private void addCustomFieldsIfPresent(ExcelLeadDto dto, Lead lead, String createdBy) {
    Optional.ofNullable(mapToCustomFields(dto, lead, createdBy))
        .ifPresent(field -> lead.getCustomFields().add(field));
  }

  private CustomFields mapToCustomFields(ExcelLeadDto dto, Lead lead, String createdBy) {
    if (StringUtils.isEmpty(dto.getCustomFieldName())
        || StringUtils.isEmpty(dto.getCustomFieldValue()) || isCustomFieldPresent(lead, dto)) {
      return null;
    }
    return CustomFields.builder()
        .fieldName(dto.getCustomFieldName())
        .fieldValue(dto.getCustomFieldValue())
        .status(Status.ACTIVE)
        .createdBy(createdBy)
        .lead(lead)
        .build();
  }

  private LeadAddress mapToLeadAddress(ExcelLeadDto dto, Lead lead, String createdBy) {
    if (isAddressEmpty(dto) || isAddressPresent(lead, dto)) {
      return null;
    }

    return LeadAddress.builder()
        .addressType(AddressType.valueOf(dto.getAddressType()))
        .addressLine1(dto.getAddressLine1())
        .addressLine2(dto.getAddressLine2())
        .city(dto.getCity())
        .state(dto.getState())
        .zipcode(dto.getZipcode())
        .country(dto.getCountry())
        .isPrimary(AppConstant.YES.equalsIgnoreCase(dto.getIsPrimaryAddress()))
        .status(Status.ACTIVE)
        .createdBy(createdBy)
        .lead(lead)
        .build();
  }

  private boolean isAddressEmpty(ExcelLeadDto dto) {
    return StringUtils.isEmpty(dto.getAddressType()) ||
        Stream.of(dto.getAddressLine1(), dto.getAddressLine2(), dto.getCity(),
                dto.getState(), dto.getZipcode(), dto.getCountry())
            .allMatch(StringUtils::isEmpty);
  }

  private Contact mapToContact(ExcelLeadDto dto, Lead lead, String createdBy) {
    if (Stream.of(dto.getContactName(), dto.getContactType(), dto.getContactValue())
        .anyMatch(StringUtils::isEmpty) || isContactDetailPresent(lead, dto)) {
      return null;
    }
    Contact contact = Contact.builder()
        .name(dto.getContactName())
        .title(dto.getContactTitle())
        .url(dto.getUrl())
        .lead(lead)
        .status(Status.ACTIVE)
        .createdBy(createdBy)
        .isPrimary(getIsPrimaryContact(lead.getContacts(), dto))
        .build();
    contact.setContactDetails(Collections.singletonList(mapToContactDetails(dto, contact)));
    return contact;
  }

  private boolean getIsPrimaryContact(List<Contact> contacts, ExcelLeadDto dto) {
    if (CollectionUtils.isEmpty(contacts)) {
      return AppConstant.YES.equalsIgnoreCase(dto.getIsPrimaryContact());
    }
    boolean anyMatch = contacts.stream().anyMatch(Contact::isPrimary);
    return !anyMatch && AppConstant.YES.equalsIgnoreCase(dto.getIsPrimaryContact());
  }

  private boolean isContactDetailPresent(Lead lead, ExcelLeadDto dto) {
    return Optional.ofNullable(lead.getContacts())
        .orElse(Collections.emptyList())
        .stream()
        .flatMap(contact -> Optional.ofNullable(contact.getContactDetails())
            .orElse(Collections.emptyList()).stream())
        .anyMatch(contactDetail ->
            contactDetail.getContactType().name().equalsIgnoreCase(dto.getContactType()) &&
                contactDetail.getValue().equalsIgnoreCase(dto.getContactValue()));
  }


  private boolean isAddressPresent(Lead lead, ExcelLeadDto dto) {
    return Optional.ofNullable(lead.getLeadAddresses())
        .orElse(Collections.emptyList())
        .stream()
        .anyMatch(leadAddress ->
            Objects.equals(leadAddress.getAddressType(), AddressType.valueOf(dto.getAddressType())) &&
                Objects.equals(leadAddress.getAddressLine1(), dto.getAddressLine1()) &&
                Objects.equals(leadAddress.getAddressLine2(), dto.getAddressLine2()) &&
                Objects.equals(leadAddress.getCity(), dto.getCity()) &&
                Objects.equals(leadAddress.getState(), dto.getState()) &&
                Objects.equals(leadAddress.getZipcode(), dto.getZipcode()) &&
                Objects.equals(leadAddress.getCountry(), dto.getCountry())
        );
  }


  private boolean isCustomFieldPresent(Lead lead, ExcelLeadDto dto) {
    return Optional.ofNullable(lead.getCustomFields())
        .orElse(Collections.emptyList())
        .stream()
        .anyMatch(customField ->
            Objects.equals(customField.getFieldName(), dto.getCustomFieldName()) &&
                Objects.equals(customField.getFieldValue(), dto.getCustomFieldValue())
        );
  }

  private ContactDetails mapToContactDetails(ExcelLeadDto dto, Contact contact) {
    return ContactDetails.builder()
        .contactType(ContactType.valueOf(dto.getContactType()))
        .value(dto.getContactValue())
        .status(Status.ACTIVE)
        .contact(contact)
        .build();
  }

  private List<Lead> mapToLeads(RequestEntity<List<LeadRequest>> leadRequestEntity) {
    String userId = CommonUtil.getHeaderData(leadRequestEntity, HeaderConstant.USER_ID);
    long subscriberId = Long.parseLong(CommonUtil.getHeaderData(
        leadRequestEntity, HeaderConstant.SUBSCRIBER_ID));
    return leadRequestEntity.getBody().stream()
        .map(leadRequest -> {
          Lead lead = ObjectMapperUtil.mapToSaveLead(leadRequest, userId, subscriberId);
          lead.setDuplicateOf(getDuplicateOfId(lead));
          return lead;
        }).collect(Collectors.toList());
  }
}
