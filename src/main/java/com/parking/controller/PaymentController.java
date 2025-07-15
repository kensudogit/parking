package com.parking.controller;

import com.parking.dto.PaymentRequest;
import com.parking.dto.PaymentResponse;
import com.parking.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class PaymentController {
    
    private final PaymentService paymentService;
    
    @PostMapping("/process")
    public ResponseEntity<PaymentResponse> processPayment(@Valid @RequestBody PaymentRequest request) {
        try {
            PaymentResponse response = paymentService.processPayment(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            log.error("Invalid payment request: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException e) {
            log.error("Payment state error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            log.error("Payment processing error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/session/{sessionId}")
    public ResponseEntity<PaymentResponse> getPaymentBySessionId(@PathVariable Long sessionId) {
        try {
            PaymentResponse response = paymentService.getPaymentBySessionId(sessionId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Payment not found for session: {}", sessionId);
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/license/{licensePlate}")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByLicensePlate(@PathVariable String licensePlate) {
        try {
            List<PaymentResponse> responses = paymentService.getPaymentsByLicensePlate(licensePlate);
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            log.error("Error retrieving payments for license plate: {}", licensePlate);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping("/{paymentId}/refund")
    public ResponseEntity<PaymentResponse> refundPayment(@PathVariable Long paymentId) {
        try {
            PaymentResponse response = paymentService.refundPayment(paymentId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Payment not found: {}", paymentId);
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            log.error("Cannot refund payment: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Refund processing error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getPaymentStatistics() {
        try {
            Map<String, Object> stats = paymentService.getPaymentStatistics();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("Error retrieving payment statistics: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/methods")
    public ResponseEntity<Map<String, String>> getSupportedPaymentMethods() {
        Map<String, String> methods = Map.of(
            "CREDIT_CARD", "Credit Card Payment",
            "DEBIT_CARD", "Debit Card Payment", 
            "CASH", "Cash Payment",
            "ELECTRONIC_WALLET", "Electronic Wallet (LINE Pay, PayPay, etc.)",
            "MOBILE_PAYMENT", "Mobile Payment (Apple Pay, Google Pay, etc.)",
            "QR_CODE", "QR Code Payment"
        );
        return ResponseEntity.ok(methods);
    }
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> getPaymentServiceHealth() {
        Map<String, String> health = Map.of(
            "status", "UP",
            "service", "Payment Service",
            "version", "1.0.0"
        );
        return ResponseEntity.ok(health);
    }
} 