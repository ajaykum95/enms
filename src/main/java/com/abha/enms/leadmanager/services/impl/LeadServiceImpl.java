package com.abha.enms.leadmanager.services.impl;

import com.abha.enms.leadmanager.daos.LeadDao;
import com.abha.enms.leadmanager.dtos.ExcelLeadDto;
import com.abha.enms.leadmanager.models.Contact;
import com.abha.enms.leadmanager.models.ContactDetails;
import com.abha.enms.leadmanager.models.Lead;
import com.abha.enms.leadmanager.services.LeadService;
import com.abha.enms.utils.AppConstant;
import com.abha.enms.utils.CommonUtil;
import com.abha.enms.utils.ObjectMapperUtil;
import com.abha.sharedlibrary.enms.request.LeadRequest;
import com.abha.sharedlibrary.enms.response.LeadResponse;
import com.abha.sharedlibrary.shared.common.ExcelFileUtil;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class LeadServiceImpl implements LeadService {

  private final LeadDao leadDao;

  @Autowired
  public LeadServiceImpl(LeadDao leadDao) {
    this.leadDao = leadDao;
  }

  @Transactional
  @Override
  public LeadResponse saveLeadRequest(RequestEntity<LeadRequest> leadRequestEntity) {
    String userId = CommonUtil.getUserId(leadRequestEntity);
    Lead lead = ObjectMapperUtil.mapToSaveLead(leadRequestEntity.getBody(), userId);
    lead.setDuplicateOf(getDuplicateOfId(lead));
    Lead savedLead = leadDao.saveLead(lead);
    return LeadResponse.builder()
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

//  @Async
  @Override
  public void importLeads(Map<String, String> headers, MultipartFile file) {
    try {
      List<ExcelLeadDto> excelLeadDtos = ExcelFileUtil.readExcel(
          file.getInputStream(), ExcelLeadDto.class);
      if (!CollectionUtils.isEmpty(excelLeadDtos)) {
        //TODO
      }
    } catch (Exception e) {
      log.error("An error occurred while processing the file, Error : {}",
          ExceptionUtils.getStackTrace(e));
    }
  }

  private List<Lead> mapToLeads(RequestEntity<List<LeadRequest>> leadRequestEntity) {
    String userId = CommonUtil.getUserId(leadRequestEntity);
    return leadRequestEntity.getBody().stream()
        .map(leadRequest -> {
          Lead lead = ObjectMapperUtil.mapToSaveLead(leadRequest, userId);
          lead.setDuplicateOf(getDuplicateOfId(lead));
          return lead;
        }).collect(Collectors.toList());
  }
}
