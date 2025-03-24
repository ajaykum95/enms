package com.abha.enms.leadmanager.controllers;

import com.abha.enms.leadmanager.services.LeadService;
import com.abha.enms.utils.AppConstant;
import com.abha.enms.utils.RequestValidator;
import com.abha.sharedlibrary.enms.request.LeadRequest;
import com.abha.sharedlibrary.enms.response.LeadResponse;
import com.abha.sharedlibrary.shared.common.response.CommonResponse;
import java.util.List;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/leads")
public class LeadController {

  private final LeadService leadService;

  public LeadController(LeadService leadService) {
    this.leadService = leadService;
  }

  @PostMapping("/save")
  public ResponseEntity<LeadResponse> saveLead(RequestEntity<LeadRequest> leadRequestEntity) {
    RequestValidator.validateLeadRequest(leadRequestEntity);
    return ResponseEntity.ok(leadService.saveLeadRequest(leadRequestEntity));
  }

  @PostMapping("/saveAll")
  public ResponseEntity<CommonResponse> saveLeads(RequestEntity<List<LeadRequest>> leadRequestEntity) {
    RequestValidator.validateLeadsRequest(leadRequestEntity);
    leadService.saveLeadsRequest(leadRequestEntity);
    return ResponseEntity.ok(new CommonResponse(true, AppConstant.SAVE_LEAD_IN_PROGRESS));
  }

  @PostMapping(value = "/import", consumes = "multipart/form-data")
  public ResponseEntity<CommonResponse> importLeads(RequestEntity<MultipartFile> fileRequestEntity) {
    RequestValidator.validateImportLeads(fileRequestEntity);
    leadService.importLeads(fileRequestEntity);
    return ResponseEntity.ok(new CommonResponse(true, AppConstant.IMPORT_LEADS_PROGRESS));
  }
}
