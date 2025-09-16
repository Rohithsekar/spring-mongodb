package com.rohi.spring_mongo.e_commerce.model.embedded;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment implements Serializable{
    private String method; // credit_card, debit_card, paypal, bank_transfer
    private String cardLast4;
    private String transactionId;
    private java.time.LocalDateTime paymentDate;
}