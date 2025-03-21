package com.abha.enms.leadmanager.services.impl;

import com.abha.enms.leadmanager.daos.LeadDao;
import com.abha.enms.leadmanager.models.Contact;
import com.abha.enms.leadmanager.models.ContactDetails;
import com.abha.enms.leadmanager.models.Lead;
import com.abha.enms.leadmanager.services.LeadService;
import com.abha.enms.utils.AppConstant;
import com.abha.enms.utils.CommonUtil;
import com.abha.enms.utils.ObjectMapperUtil;
import com.abha.sharedlibrary.enms.request.LeadRequest;
import com.abha.sharedlibrary.enms.response.LeadResponse;
import com.abha.sharedlibrary.shared.common.Utils;
import com.abha.sharedlibrary.shared.common.response.CommonResponse;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;

@Service
public class LeadServiceImpl implements LeadService {

  private final LeadDao leadDao;

  @Autowired
  public LeadServiceImpl(LeadDao leadDao) {
    this.leadDao = leadDao;
  }

  @Transactional
  @Override
  public LeadResponse saveWebhookLeadRequest(RequestEntity<LeadRequest> webhookLeadRequestEntity) {
    String userId = CommonUtil.getUserId(webhookLeadRequestEntity);
    Lead lead = ObjectMapperUtil.mapToSaveWebhookLead(
        webhookLeadRequestEntity.getBody(), userId);
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

  @Override
  public CommonResponse saveWebhookLeadsRequest(RequestEntity<List<LeadRequest>> webhookLeadRequestEntity) {
    List<Lead> leadList = mapToLeads(webhookLeadRequestEntity);
    leadDao.saveAllLead(leadList);
    return new CommonResponse(true, AppConstant.SAVE_LEAD_MESSAGE);
  }

  private List<Lead> mapToLeads(RequestEntity<List<LeadRequest>> webhookLeadRequestEntity) {
    String userId = CommonUtil.getUserId(webhookLeadRequestEntity);
    return webhookLeadRequestEntity.getBody().stream()
        .map(leadRequest -> {
          Lead lead = ObjectMapperUtil.mapToSaveWebhookLead(leadRequest, userId);
          lead.setDuplicateOf(getDuplicateOfId(lead));
          return lead;
        }).collect(Collectors.toList());
  }
}
