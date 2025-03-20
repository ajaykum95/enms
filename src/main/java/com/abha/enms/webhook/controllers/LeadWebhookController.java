package com.abha.enms.webhook.controllers;

import com.abha.enms.leadmanager.services.LeadService;
import com.abha.enms.utils.RequestValidator;
import com.abha.sharedlibrary.enms.request.LeadRequest;
import com.abha.sharedlibrary.enms.response.LeadResponse;
import com.abha.sharedlibrary.shared.common.response.CommonResponse;
import java.util.List;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/webhook/leads")
public class LeadWebhookController {

  private final LeadService leadService;

  public LeadWebhookController(LeadService leadService) {
    this.leadService = leadService;
  }

  @PostMapping("/save")
  public ResponseEntity<LeadResponse> saveWebhookLead(
      RequestEntity<LeadRequest> webhookLeadRequestEntity) {
    RequestValidator.validateWebhookLeadRequest(webhookLeadRequestEntity);
    return ResponseEntity.ok(leadService.saveWebhookLeadRequest(webhookLeadRequestEntity));
  }

  @PostMapping("/saveAll")
  public ResponseEntity<CommonResponse> saveWebhookLeads(
      RequestEntity<List<LeadRequest>> webhookLeadRequestEntity) {
    RequestValidator.validateWebhookLeadsRequest(webhookLeadRequestEntity);
    return ResponseEntity.ok(leadService.saveWebhookLeadsRequest(webhookLeadRequestEntity));
  }
}
