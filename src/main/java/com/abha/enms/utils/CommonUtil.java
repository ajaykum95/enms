package com.abha.enms.utils;

import com.abha.sharedlibrary.shared.constants.HeaderConstant;
import org.springframework.http.RequestEntity;

public class CommonUtil {
  public static <T> String getUserId(RequestEntity<T> requestEntity) {
    return requestEntity.getHeaders().get(HeaderConstant.USER_ID)
        .stream().findFirst().orElse("0");
  }
}
