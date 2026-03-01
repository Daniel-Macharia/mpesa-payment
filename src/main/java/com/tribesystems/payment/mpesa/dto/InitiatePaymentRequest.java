package com.tribesystems.payment.mpesa.dto;

public record InitiatePaymentRequest(
  String Password,
  String BusinessShortCode,
  String Timestamp,
  String Amount,
  String PartyA,
  String PartyB,
  String TransactionType,
  String PhoneNumber,
  String TransactionDesc,
  String AccountReference,
  String CallBackURL
) {
}
