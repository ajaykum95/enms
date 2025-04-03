package com.abha.enms.leadmanager.services;

import com.abha.sharedlibrary.enms.request.LeadRequest;
import com.abha.sharedlibrary.enms.response.LeadResponseData;
import com.abha.sharedlibrary.enms.response.LeadSaveResponse;
import java.util.List;
import java.util.Map;
import org.springframework.http.RequestEntity;
import org.springframework.web.multipart.MultipartFile;

public interface LeadService {
  LeadSaveResponse saveLeadRequest(RequestEntity<LeadRequest> leadRequestEntity);

  void saveLeadsRequest(RequestEntity<List<LeadRequest>> leadRequestEntity);

  void importLeads(Map<String, String> headers, MultipartFile file);

  LeadResponseData fetchAllLeads(int pageNumber, int pageSize, Map<String, String> headers);
}
