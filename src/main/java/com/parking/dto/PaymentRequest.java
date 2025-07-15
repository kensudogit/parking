package com.parking.dto;

import com.parking.entity.Payment;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    
    @NotNull(message = "Session ID is required")
    private Long sessionId;
    
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;
    
    @NotNull(message = "Payment method is required")
    private Payment.PaymentMethod paymentMethod;
    
    // Credit/Debit card details (optional for other payment methods)
    private String cardNumber;
    private String cardHolderName;
    private String cardExpiryMonth;
    private String cardExpiryYear;
    private String cardCvv;
    
    // Mobile payment details
    private String phoneNumber;
    private String walletType; // PayPal, Apple Pay, Google Pay, etc.
    
    // QR code payment
    private String qrCodeData;
    
    // Electronic wallet
    private String walletId;
    private String walletProvider; // LINE Pay, PayPay, etc.
} 