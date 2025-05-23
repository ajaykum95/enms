package com.abha.enms.leadmanager.services;

import com.abha.enms.leadmanager.models.LeadImportHistory;
import com.abha.sharedlibrary.enms.request.LeadRequest;
import com.abha.sharedlibrary.enms.request.LeadSearchFilter;
import com.abha.sharedlibrary.enms.request.LeadStatusRequest;
import com.abha.sharedlibrary.enms.response.LeadResponseData;
import com.abha.sharedlibrary.enms.response.LeadSaveResponse;
import com.abha.sharedlibrary.shared.common.response.CommonResponse;
import java.util.List;
import java.util.Map;
import org.springframework.http.RequestEntity;
import org.springframework.web.multipart.MultipartFile;

public interface LeadService {
  LeadSaveResponse saveLeadRequest(RequestEntity<LeadRequest> leadRequestEntity);

  void saveLeadsRequest(RequestEntity<List<LeadRequest>> leadRequestEntity);

  void importLeads(Map<String, String> headers, MultipartFile file);

  LeadResponseData fetchAllLeads(RequestEntity<LeadSearchFilter> leadSearchFilterRequestEntity);

  List<LeadImportHistory> fetchAllImportHistory(Map<String, String> headers);

  CommonResponse updateLeadStatus(RequestEntity<LeadStatusRequest> leadStatusRequestEntity);
}
