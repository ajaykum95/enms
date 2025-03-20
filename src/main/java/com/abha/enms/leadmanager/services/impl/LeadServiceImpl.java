package com.abha.enms.leadmanager.services.impl;

import com.abha.enms.leadmanager.daos.LeadDao;
import com.abha.enms.leadmanager.models.Lead;
import com.abha.enms.leadmanager.services.LeadService;
import com.abha.enms.utils.AppConstant;
import com.abha.enms.utils.ObjectMapperUtil;
import com.abha.sharedlibrary.enms.request.LeadRequest;
import com.abha.sharedlibrary.enms.response.LeadResponse;
import com.abha.sharedlibrary.shared.common.response.CommonResponse;
import java.util.List;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;

@Service
public class LeadServiceImpl implements LeadService {

  private final LeadDao leadDao;

  public LeadServiceImpl(LeadDao leadDao) {
    this.leadDao = leadDao;
  }

  @Override
  public LeadResponse saveWebhookLeadRequest(RequestEntity<LeadRequest> webhookLeadRequestEntity) {
    Lead lead = ObjectMapperUtil.mapToSaveWebhookLead(webhookLeadRequestEntity);
    Lead savedLead = leadDao.saveLead(lead);
    return LeadResponse.builder()
        .leadId(savedLead.getId())
        .success(true)
        .message(AppConstant.SAVE_LEAD_MESSAGE)
        .build();
  }

  @Override
  public CommonResponse saveWebhookLeadsRequest(RequestEntity<List<LeadRequest>> webhookLeadRequestEntity) {
    return null;
  }
}
