package com.abha.enms.leadmanager.services;

import com.abha.sharedlibrary.enms.request.LeadRequest;
import com.abha.sharedlibrary.enms.response.LeadResponse;
import com.abha.sharedlibrary.shared.common.response.CommonResponse;
import java.util.List;
import org.springframework.http.RequestEntity;

public interface LeadService {
  LeadResponse saveLeadRequest(RequestEntity<LeadRequest> leadRequestEntity);

  CommonResponse saveLeadsRequest(RequestEntity<List<LeadRequest>> leadRequestEntity);
}
