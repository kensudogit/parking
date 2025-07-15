package com.parking.dto;

import com.parking.entity.Payment;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    
    private Long paymentId;
    private Long sessionId;
    private String licensePlate;
    private BigDecimal amount;
    private Payment.PaymentMethod paymentMethod;
    private Payment.PaymentStatus status;
    private String transactionId;
    private String cardLastFour;
    private String cardBrand;
    private String receiptUrl;
    private String failureReason;
    private LocalDateTime processedAt;
    private LocalDateTime createdAt;
    
    // QR code for payment (if applicable)
    private String qrCodeUrl;
    
    // Payment gateway specific data
    private String gatewayResponse;
    private String gatewayTransactionId;
} 