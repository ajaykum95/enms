package com.abha.enms.utils;

import com.abha.enms.exceptions.EnmsExceptions;
import com.abha.sharedlibrary.shared.constants.HeaderConstant;
import java.util.Map;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;

import static com.abha.sharedlibrary.shared.common.ExceptionUtil.buildException;

public class CommonUtil {
  public static <T> String getHeaderData(RequestEntity<T> requestEntity, String headerKey) {
    return getHeaderData(requestEntity.getHeaders(), headerKey);
  }

  public static String getHeaderData(HttpHeaders headers, String headerKey) {
    return headers.get(headerKey).stream().findFirst()
        .orElseThrow(() -> buildException(EnmsExceptions.HEADER_DATA_NOT_FOUND, headerKey));
  }

  public static String getHeaderData(Map<String, String> headers, String headerKey) {
    String headerValue = headers.get(headerKey);
    if (StringUtils.isEmpty(headerValue)) {
      throw buildException(EnmsExceptions.HEADER_DATA_NOT_FOUND, headerKey);
    }
    return headerValue;
  }
}
