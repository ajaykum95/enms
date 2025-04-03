package com.abha.enms.exceptions;

import com.abha.sharedlibrary.exceptions.BusinessExceptionDefintion;
import com.abha.sharedlibrary.exceptions.ErrorCategory;
import com.abha.sharedlibrary.exceptions.ExceptionTypes;

/**
 * This Enum defines the Exceptions for IoMS.
 */
public enum EnmsExceptions implements BusinessExceptionDefintion {
  DB_ERROR(5000, ErrorCategory.ERROR, ExceptionTypes.DB_ERROR, "%s"),
  LEAD_REQUEST_MISSING(5001, ErrorCategory.ERROR, ExceptionTypes.VALIDATION_ERROR,
      "Lead request is missing!"),
  REQUEST_SOURCE_MISSING(5002, ErrorCategory.ERROR, ExceptionTypes.VALIDATION_ERROR,
      "The source name is missing!"),
  LEAD_NAME_MISSING(5003, ErrorCategory.ERROR, ExceptionTypes.VALIDATION_ERROR,
      "Company name is missing!"),
  CONTACT_DETAILS_MISSING(5004, ErrorCategory.ERROR, ExceptionTypes.VALIDATION_ERROR,
      "At least one contact detail is required!"),
  EMAIL_OR_PHONE_REQUIRED(5005, ErrorCategory.ERROR, ExceptionTypes.VALIDATION_ERROR,
      "Either an email or a phone number is mandatory!"),
  INVALID_PHONE_NUMBER(5006, ErrorCategory.ERROR, ExceptionTypes.VALIDATION_ERROR,
      "Invalid phone number!"),
  INVALID_EMAIL_ADDRESS(5007, ErrorCategory.ERROR, ExceptionTypes.VALIDATION_ERROR,
      "Invalid email address!"),
  LEAD_CONTACTS_MISSING(5008, ErrorCategory.ERROR, ExceptionTypes.VALIDATION_ERROR,
      "Lead contacts are missing!"),
  MAX_LEAD_SIZE_EXCEED(5009, ErrorCategory.ERROR, ExceptionTypes.VALIDATION_ERROR,
      "Maximum lead size exceeded! Allowed: %s, Provided: %s."),
  CONTACT_NAME_MISSING(5010, ErrorCategory.ERROR, ExceptionTypes.VALIDATION_ERROR,
      "Contact name is missing!"),
  MAX_DEFAULT_ADDRESS_EXCEED(5011, ErrorCategory.ERROR, ExceptionTypes.VALIDATION_ERROR,
      "Only one address can be set as primary!"),
  MIN_DEFAULT_ADDRESS_REQUIRED(5012, ErrorCategory.ERROR, ExceptionTypes.VALIDATION_ERROR,
      "At least one address must be set as primary!"),
  LEAD_STATUS_MISSING(5013, ErrorCategory.ERROR, ExceptionTypes.VALIDATION_ERROR,
      "Lead status is missing!"),
  IMPORT_LEADS_REQ_MISSING(5014, ErrorCategory.ERROR, ExceptionTypes.VALIDATION_ERROR,
      "Import leads request is missing!"),
  FILE_MISSING(5015, ErrorCategory.ERROR, ExceptionTypes.VALIDATION_ERROR,
      "File is missing!"),
  INVALID_FILE_FORMAT(5016, ErrorCategory.ERROR, ExceptionTypes.VALIDATION_ERROR,
      "Invalid file format. Only Excel files with the .xlsx extension are allowed!"),
  CUSTOM_FIELD_MISSING(5017, ErrorCategory.ERROR, ExceptionTypes.VALIDATION_ERROR,
      "Custom field is missing!"),
  CUSTOM_FIELD_VALUE_MISSING(5018, ErrorCategory.ERROR, ExceptionTypes.VALIDATION_ERROR,
      "Custom field value is missing!"),
  MAX_DEFAULT_CONTACT_EXCEED(5011, ErrorCategory.ERROR, ExceptionTypes.VALIDATION_ERROR,
      "Only one contact can be set as primary!"),
  MIN_DEFAULT_CONTACT_REQUIRED(5012, ErrorCategory.ERROR, ExceptionTypes.VALIDATION_ERROR,
      "At least one contact must be set as primary!"),
  HEADER_DATA_NOT_FOUND(5013, ErrorCategory.ERROR, ExceptionTypes.VALIDATION_ERROR,
      "%s not present in headers!");

  private final int businessErrorCode;
  private final ErrorCategory errorCategory;
  private final ExceptionTypes exceptionType;
  private final String errorMessage;

  EnmsExceptions(int businessErrorCode, ErrorCategory errorCategory,
                 ExceptionTypes exceptionType, String errorMessage) {
    this.businessErrorCode = businessErrorCode;
    this.errorCategory = errorCategory;
    this.exceptionType = exceptionType;
    this.errorMessage = errorMessage;
  }

  @Override
  public int getBusinessErrorCode() {
    return this.businessErrorCode;
  }

  @Override
  public ErrorCategory getErrorCategory() {
    return this.errorCategory;
  }

  @Override
  public ExceptionTypes getExceptionType() {
    return this.exceptionType;
  }

  @Override
  public String getErrorMessage() {
    return this.errorMessage;
  }
}
