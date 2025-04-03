package com.abha.enms.leadmanager.controllers;

import com.abha.enms.leadmanager.services.LeadService;
import com.abha.enms.utils.AppConstant;
import com.abha.enms.utils.RequestValidator;
import com.abha.sharedlibrary.enms.request.LeadRequest;
import com.abha.sharedlibrary.enms.request.LeadSearchFilter;
import com.abha.sharedlibrary.enms.response.LeadResponseData;
import com.abha.sharedlibrary.enms.response.LeadSaveResponse;
import com.abha.sharedlibrary.shared.common.response.CommonResponse;
import java.util.List;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
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

  @PostMapping
  public ResponseEntity<LeadResponseData> getAllLeads(
      RequestEntity<LeadSearchFilter> leadSearchFilterRequestEntity) {
    RequestValidator.validateLeadSearchRequest(leadSearchFilterRequestEntity);
    return ResponseEntity.ok(leadService.fetchAllLeads(leadSearchFilterRequestEntity));
  }

  @PostMapping("/save")
  public ResponseEntity<LeadSaveResponse> saveLead(RequestEntity<LeadRequest> leadRequestEntity) {
    RequestValidator.validateLeadRequest(leadRequestEntity);
    return ResponseEntity.ok(leadService.saveLeadRequest(leadRequestEntity));
  }

  @PostMapping("/saveAll")
  public ResponseEntity<CommonResponse> saveLeads(
      RequestEntity<List<LeadRequest>> leadRequestEntity) {
    RequestValidator.validateLeadsRequest(leadRequestEntity);
    leadService.saveLeadsRequest(leadRequestEntity);
    return ResponseEntity.ok(new CommonResponse(true, AppConstant.SAVE_LEAD_IN_PROGRESS));
  }

  @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<CommonResponse> importLeads(
      @RequestHeader Map<String, String> headers, @RequestParam("file") MultipartFile file) {
    RequestValidator.validateImportLeads(file);
    leadService.importLeads(headers, file);
    return ResponseEntity.ok(new CommonResponse(true, AppConstant.IMPORT_LEADS_PROGRESS));
  }
}
