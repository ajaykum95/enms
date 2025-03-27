package com.abha.enms.leadmanager.services;

import com.abha.sharedlibrary.enms.request.LeadRequest;
import com.abha.sharedlibrary.enms.response.LeadResponse;
import java.util.List;
import java.util.Map;
import org.springframework.http.RequestEntity;
import org.springframework.web.multipart.MultipartFile;

public interface LeadService {
  LeadResponse saveLeadRequest(RequestEntity<LeadRequest> leadRequestEntity);

  void saveLeadsRequest(RequestEntity<List<LeadRequest>> leadRequestEntity);

  void importLeads(Map<String, String> headers, MultipartFile file);
}
