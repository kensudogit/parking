package com.parking.service;

import com.parking.entity.Payment;
import com.parking.entity.ParkingSession;
import com.parking.dto.PaymentRequest;
import com.parking.dto.PaymentResponse;
import com.parking.repository.PaymentRepository;
import com.parking.repository.ParkingSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

/**
 * 決済サービスクラス
 * 駐車場の決済処理を管理する
 * 
 * @author Parking System
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PaymentService {
    
    /** 決済リポジトリ */
    private final PaymentRepository paymentRepository;
    /** 駐車セッションリポジトリ */
    private final ParkingSessionRepository parkingSessionRepository;
    
    /**
     * 決済を処理する
     * 
     * @param request 決済リクエスト
     * @return 決済レスポンス
     * @throws IllegalArgumentException 駐車セッションが見つからない場合
     * @throws IllegalStateException 決済が既に完了している場合
     */
    public PaymentResponse processPayment(PaymentRequest request) {
        // Validate parking session
        ParkingSession session = parkingSessionRepository.findById(request.getSessionId())
            .orElseThrow(() -> new IllegalArgumentException("Parking session not found: " + request.getSessionId()));
        
        // Check if payment already exists
        Optional<Payment> existingPayment = paymentRepository.findByParkingSessionId(request.getSessionId());
        if (existingPayment.isPresent() && existingPayment.get().getStatus() == Payment.PaymentStatus.COMPLETED) {
            throw new IllegalStateException("Payment already completed for session: " + request.getSessionId());
        }
        
        // Create payment record
        Payment payment = new Payment();
        payment.setParkingSession(session);
        payment.setAmount(request.getAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setStatus(Payment.PaymentStatus.PROCESSING);
        payment.setTransactionId(generateTransactionId());
        
        // Process payment based on method
        try {
            switch (request.getPaymentMethod()) {
                case CREDIT_CARD:
                case DEBIT_CARD:
                    payment = processCardPayment(payment, request);
                    break;
                case MOBILE_PAYMENT:
                    payment = processMobilePayment(payment, request);
                    break;
                case QR_CODE:
                    payment = processQrCodePayment(payment, request);
                    break;
                case ELECTRONIC_WALLET:
                    payment = processElectronicWalletPayment(payment, request);
                    break;
                case CASH:
                    payment = processCashPayment(payment, request);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported payment method: " + request.getPaymentMethod());
            }
            
            // Update parking session payment status
            session.setPaymentStatus(ParkingSession.PaymentStatus.PAID);
            parkingSessionRepository.save(session);
            
            return convertToPaymentResponse(payment);
            
        } catch (Exception e) {
            log.error("Payment processing failed: {}", e.getMessage());
            payment.setStatus(Payment.PaymentStatus.FAILED);
            payment.setFailureReason(e.getMessage());
            paymentRepository.save(payment);
            throw new RuntimeException("Payment processing failed: " + e.getMessage());
        }
    }
    
    private Payment processCardPayment(Payment payment, PaymentRequest request) {
        // Simulate card payment processing
        log.info("Processing card payment for session: {}", payment.getParkingSession().getId());
        
        // Validate card details
        if (request.getCardNumber() == null || request.getCardNumber().length() < 13) {
            throw new IllegalArgumentException("Invalid card number");
        }
        
        // Simulate payment gateway call
        boolean paymentSuccess = simulatePaymentGatewayCall();
        
        if (paymentSuccess) {
            payment.setStatus(Payment.PaymentStatus.COMPLETED);
            payment.setProcessedAt(LocalDateTime.now());
            payment.setCardLastFour(request.getCardNumber().substring(request.getCardNumber().length() - 4));
            payment.setCardBrand(detectCardBrand(request.getCardNumber()));
            payment.setReceiptUrl(generateReceiptUrl(payment.getTransactionId()));
        } else {
            payment.setStatus(Payment.PaymentStatus.FAILED);
            payment.setFailureReason("Card payment declined");
        }
        
        return paymentRepository.save(payment);
    }
    
    private Payment processMobilePayment(Payment payment, PaymentRequest request) {
        log.info("Processing mobile payment for session: {}", payment.getParkingSession().getId());
        
        if (request.getPhoneNumber() == null || request.getWalletType() == null) {
            throw new IllegalArgumentException("Phone number and wallet type are required for mobile payment");
        }
        
        // Simulate mobile payment processing
        boolean paymentSuccess = simulatePaymentGatewayCall();
        
        if (paymentSuccess) {
            payment.setStatus(Payment.PaymentStatus.COMPLETED);
            payment.setProcessedAt(LocalDateTime.now());
            payment.setReceiptUrl(generateReceiptUrl(payment.getTransactionId()));
        } else {
            payment.setStatus(Payment.PaymentStatus.FAILED);
            payment.setFailureReason("Mobile payment failed");
        }
        
        return paymentRepository.save(payment);
    }
    
    private Payment processQrCodePayment(Payment payment, PaymentRequest request) {
        log.info("Processing QR code payment for session: {}", payment.getParkingSession().getId());
        
        if (request.getQrCodeData() == null) {
            throw new IllegalArgumentException("QR code data is required");
        }
        
        // Generate QR code URL for payment
        String qrCodeUrl = generateQrCodeUrl(payment.getTransactionId(), payment.getAmount());
        
        // For QR payments, we typically wait for external confirmation
        // For demo purposes, we'll simulate immediate success
        boolean paymentSuccess = simulatePaymentGatewayCall();
        
        if (paymentSuccess) {
            payment.setStatus(Payment.PaymentStatus.COMPLETED);
            payment.setProcessedAt(LocalDateTime.now());
            payment.setReceiptUrl(generateReceiptUrl(payment.getTransactionId()));
        } else {
            payment.setStatus(Payment.PaymentStatus.FAILED);
            payment.setFailureReason("QR payment failed");
        }
        
        return paymentRepository.save(payment);
    }
    
    private Payment processElectronicWalletPayment(Payment payment, PaymentRequest request) {
        log.info("Processing electronic wallet payment for session: {}", payment.getParkingSession().getId());
        
        if (request.getWalletId() == null || request.getWalletProvider() == null) {
            throw new IllegalArgumentException("Wallet ID and provider are required");
        }
        
        // Simulate electronic wallet payment
        boolean paymentSuccess = simulatePaymentGatewayCall();
        
        if (paymentSuccess) {
            payment.setStatus(Payment.PaymentStatus.COMPLETED);
            payment.setProcessedAt(LocalDateTime.now());
            payment.setReceiptUrl(generateReceiptUrl(payment.getTransactionId()));
        } else {
            payment.setStatus(Payment.PaymentStatus.FAILED);
            payment.setFailureReason("Electronic wallet payment failed");
        }
        
        return paymentRepository.save(payment);
    }
    
    private Payment processCashPayment(Payment payment, PaymentRequest request) {
        log.info("Processing cash payment for session: {}", payment.getParkingSession().getId());
        
        // For cash payments, we assume immediate completion
        payment.setStatus(Payment.PaymentStatus.COMPLETED);
        payment.setProcessedAt(LocalDateTime.now());
        payment.setReceiptUrl(generateReceiptUrl(payment.getTransactionId()));
        
        return paymentRepository.save(payment);
    }
    
    public PaymentResponse getPaymentBySessionId(Long sessionId) {
        Payment payment = paymentRepository.findByParkingSessionId(sessionId)
            .orElseThrow(() -> new IllegalArgumentException("Payment not found for session: " + sessionId));
        
        return convertToPaymentResponse(payment);
    }
    
    public List<PaymentResponse> getPaymentsByLicensePlate(String licensePlate) {
        List<Payment> payments = paymentRepository.findByLicensePlate(licensePlate);
        return payments.stream()
            .map(this::convertToPaymentResponse)
            .toList();
    }
    
    public PaymentResponse refundPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new IllegalArgumentException("Payment not found: " + paymentId));
        
        if (payment.getStatus() != Payment.PaymentStatus.COMPLETED) {
            throw new IllegalStateException("Only completed payments can be refunded");
        }
        
        // Simulate refund processing
        payment.setStatus(Payment.PaymentStatus.REFUNDED);
        payment.setProcessedAt(LocalDateTime.now());
        
        // Update parking session payment status
        ParkingSession session = payment.getParkingSession();
        session.setPaymentStatus(ParkingSession.PaymentStatus.PENDING);
        parkingSessionRepository.save(session);
        
        return convertToPaymentResponse(paymentRepository.save(payment));
    }
    
    public Map<String, Object> getPaymentStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalPayments", paymentRepository.count());
        stats.put("completedPayments", paymentRepository.countByStatus(Payment.PaymentStatus.COMPLETED));
        stats.put("failedPayments", paymentRepository.countByStatus(Payment.PaymentStatus.FAILED));
        stats.put("pendingPayments", paymentRepository.countByStatus(Payment.PaymentStatus.PENDING));
        
        // Revenue statistics
        Double totalRevenue = paymentRepository.getTotalRevenueSince(LocalDateTime.now().minusDays(30));
        stats.put("monthlyRevenue", totalRevenue != null ? totalRevenue : 0.0);
        
        // Payment method statistics
        List<Object[]> methodStats = paymentRepository.getPaymentMethodStats();
        Map<String, Long> methodCounts = new HashMap<>();
        for (Object[] stat : methodStats) {
            methodCounts.put(stat[0].toString(), (Long) stat[1]);
        }
        stats.put("paymentMethodStats", methodCounts);
        
        return stats;
    }
    
    private String generateTransactionId() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    private String generateReceiptUrl(String transactionId) {
        return "https://parking-system.com/receipts/" + transactionId + ".pdf";
    }
    
    private String generateQrCodeUrl(String transactionId, BigDecimal amount) {
        return "https://parking-system.com/qr/" + transactionId + "?amount=" + amount;
    }
    
    private boolean simulatePaymentGatewayCall() {
        // Simulate payment gateway processing with 95% success rate
        return Math.random() > 0.05;
    }
    
    private String detectCardBrand(String cardNumber) {
        if (cardNumber.startsWith("4")) {
            return "Visa";
        } else if (cardNumber.startsWith("5")) {
            return "Mastercard";
        } else if (cardNumber.startsWith("3")) {
            return "American Express";
        } else {
            return "Unknown";
        }
    }
    
    private PaymentResponse convertToPaymentResponse(Payment payment) {
        PaymentResponse response = new PaymentResponse();
        response.setPaymentId(payment.getId());
        response.setSessionId(payment.getParkingSession().getId());
        response.setLicensePlate(payment.getParkingSession().getLicensePlate());
        response.setAmount(payment.getAmount());
        response.setPaymentMethod(payment.getPaymentMethod());
        response.setStatus(payment.getStatus());
        response.setTransactionId(payment.getTransactionId());
        response.setCardLastFour(payment.getCardLastFour());
        response.setCardBrand(payment.getCardBrand());
        response.setReceiptUrl(payment.getReceiptUrl());
        response.setFailureReason(payment.getFailureReason());
        response.setProcessedAt(payment.getProcessedAt());
        response.setCreatedAt(payment.getCreatedAt());
        
        // Set QR code URL for QR payments
        if (payment.getPaymentMethod() == Payment.PaymentMethod.QR_CODE) {
            response.setQrCodeUrl(generateQrCodeUrl(payment.getTransactionId(), payment.getAmount()));
        }
        
        return response;
    }
} 