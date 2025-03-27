package com.abha.enms.leadmanager.dtos;

import com.abha.sharedlibrary.shared.annotations.ExcelHeader;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ExcelLeadDto {
  @ExcelHeader(headerName = "Company name", columnIndex = 0)
  private String companyName;

  @ExcelHeader(headerName = "URL", columnIndex = 1)
  private String url;

  @ExcelHeader(headerName = "Company description", columnIndex = 2)
  private String companyDescription;

  @ExcelHeader(headerName = "Lead status", columnIndex = 3)
  private String leadStatus;

  @ExcelHeader(headerName = "Contact name", columnIndex = 4)
  private String contactName;

  @ExcelHeader(headerName = "Contact title", columnIndex = 5)
  private String contactTitle;

  @ExcelHeader(headerName = "Contact URL", columnIndex = 6)
  private String contactUrl;

  @ExcelHeader(headerName = "Contact Type", columnIndex = 7)
  private String contactType;

  @ExcelHeader(headerName = "Contact Value", columnIndex = 8)
  private String contactValue;

  @ExcelHeader(headerName = "Address Type", columnIndex = 9)
  private String addressType;

  @ExcelHeader(headerName = "Address line1", columnIndex = 10)
  private String addressLine1;

  @ExcelHeader(headerName = "Address line2", columnIndex = 11)
  private String addressLine2;

  @ExcelHeader(headerName = "City", columnIndex = 12)
  private String city;

  @ExcelHeader(headerName = "State", columnIndex = 13)
  private String state;

  @ExcelHeader(headerName = "Zipcode", columnIndex = 14)
  private String zipcode;

  @ExcelHeader(headerName = "Country", columnIndex = 15)
  private String country;

  @ExcelHeader(headerName = "Is Default", columnIndex = 16)
  private String isDefault;

  @ExcelHeader(headerName = "Custom field name", columnIndex = 17)
  private String customFieldName;

  @ExcelHeader(headerName = "Custom field value", columnIndex = 18)
  private String customFieldValue;

  @ExcelHeader(headerName = "source", columnIndex = 19)
  private String source;
}
