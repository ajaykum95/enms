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

  @ExcelHeader(headerName = "SN", columnIndex = 0)
  private String sn;

  @ExcelHeader(headerName = "Company name", columnIndex = 1)
  private String companyName;

  @ExcelHeader(headerName = "URL", columnIndex = 2)
  private String url;

  @ExcelHeader(headerName = "Company description", columnIndex = 3)
  private String companyDescription;

  @ExcelHeader(headerName = "Lead status", columnIndex = 4)
  private String leadStatus;

  @ExcelHeader(headerName = "Contact name", columnIndex = 5)
  private String contactName;

  @ExcelHeader(headerName = "Contact title", columnIndex = 6)
  private String contactTitle;

  @ExcelHeader(headerName = "Contact URL", columnIndex = 7)
  private String contactUrl;

  @ExcelHeader(headerName = "Contact Type", columnIndex = 8)
  private String contactType;

  @ExcelHeader(headerName = "Contact Value", columnIndex = 9)
  private String contactValue;

  @ExcelHeader(headerName = "Primary", columnIndex = 10)
  private String isPrimary;

  @ExcelHeader(headerName = "Address Type", columnIndex = 11)
  private String addressType;

  @ExcelHeader(headerName = "Address line1", columnIndex = 12)
  private String addressLine1;

  @ExcelHeader(headerName = "Address line2", columnIndex = 13)
  private String addressLine2;

  @ExcelHeader(headerName = "City", columnIndex = 14)
  private String city;

  @ExcelHeader(headerName = "State", columnIndex = 15)
  private String state;

  @ExcelHeader(headerName = "Zipcode", columnIndex = 16)
  private String zipcode;

  @ExcelHeader(headerName = "Country", columnIndex = 17)
  private String country;

  @ExcelHeader(headerName = "Default", columnIndex = 18)
  private String isDefault;

  @ExcelHeader(headerName = "Custom field name", columnIndex = 19)
  private String customFieldName;

  @ExcelHeader(headerName = "Custom field value", columnIndex = 20)
  private String customFieldValue;

  @ExcelHeader(headerName = "source", columnIndex = 21)
  private String source;
}
