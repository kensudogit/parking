# Parking Payment System Documentation

## Overview

The parking payment system provides comprehensive payment processing capabilities for the parking management application. It supports multiple payment methods, secure transaction processing, refund management, and detailed analytics.

## Features

### Payment Methods Supported

1. **Credit Card Payments**
   - Visa, Mastercard, American Express
   - Card validation and security
   - Last 4 digits stored for receipts
   - Card brand detection

2. **Debit Card Payments**
   - Same processing as credit cards
   - Real-time authorization

3. **Mobile Payment**
   - Apple Pay integration
   - Google Pay support
   - Samsung Pay compatibility
   - Phone number validation

4. **QR Code Payments**
   - Generate QR codes for payment
   - Support for various QR standards
   - Mobile app integration

5. **Electronic Wallet**
   - LINE Pay integration
   - PayPay support
   - Rakuten Pay compatibility
   - d Payment support

6. **Cash Payments**
   - Manual payment processing
   - Immediate completion
   - Receipt generation

### Payment Status Tracking

- **PENDING**: Payment is pending processing
- **PROCESSING**: Payment is being processed
- **COMPLETED**: Payment has been successfully completed
- **FAILED**: Payment processing failed
- **REFUNDED**: Payment has been refunded
- **CANCELLED**: Payment was cancelled

## Architecture

### Backend Components

#### 1. Payment Entity (`Payment.java`)
```java
@Entity
@Table(name = "payments")
public class Payment {
    // Core payment information
    private Long id;
    private ParkingSession parkingSession;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;
    
    // Transaction details
    private String transactionId;
    private String cardLastFour;
    private String cardBrand;
    private String receiptUrl;
    private String failureReason;
    
    // Timestamps
    private LocalDateTime processedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```

#### 2. Payment Service (`PaymentService.java`)
- Payment processing logic
- Method-specific processing
- Error handling and validation
- Transaction management
- Refund processing
- Statistics generation

#### 3. Payment Controller (`PaymentController.java`)
- REST API endpoints
- Request validation
- Response formatting
- Error handling

#### 4. Payment Repository (`PaymentRepository.java`)
- Database operations
- Custom queries
- Statistics aggregation

### Frontend Components

#### 1. Payment Form (`PaymentForm.js`)
- Multi-method payment interface
- Real-time validation
- Responsive design
- Error handling

#### 2. Demo Interface (`App.js`)
- Payment system demonstration
- API endpoint showcase
- Interactive testing

## API Endpoints

### Payment Processing

#### POST `/api/payments/process`
Process a payment for a parking session.

**Request Body:**
```json
{
  "sessionId": 1,
  "amount": 15.00,
  "paymentMethod": "CREDIT_CARD",
  "cardNumber": "4111111111111111",
  "cardHolderName": "John Doe",
  "cardExpiryMonth": "12",
  "cardExpiryYear": "2025",
  "cardCvv": "123"
}
```

**Response:**
```json
{
  "paymentId": 1,
  "sessionId": 1,
  "licensePlate": "ABC-123",
  "amount": 15.00,
  "paymentMethod": "CREDIT_CARD",
  "status": "COMPLETED",
  "transactionId": "TXN-ABC12345",
  "cardLastFour": "1111",
  "cardBrand": "Visa",
  "receiptUrl": "https://parking-system.com/receipts/TXN-ABC12345.pdf",
  "processedAt": "2024-01-15T10:30:00",
  "createdAt": "2024-01-15T10:30:00"
}
```

### Payment Retrieval

#### GET `/api/payments/session/{sessionId}`
Get payment details by session ID.

#### GET `/api/payments/license/{licensePlate}`
Get all payments for a license plate.

### Refund Processing

#### POST `/api/payments/{paymentId}/refund`
Process a refund for a completed payment.

### Statistics

#### GET `/api/payments/statistics`
Get comprehensive payment statistics.

**Response:**
```json
{
  "totalPayments": 150,
  "completedPayments": 140,
  "failedPayments": 5,
  "pendingPayments": 5,
  "monthlyRevenue": 2250.00,
  "paymentMethodStats": {
    "CREDIT_CARD": 60,
    "MOBILE_PAYMENT": 30,
    "ELECTRONIC_WALLET": 25,
    "CASH": 20,
    "QR_CODE": 10,
    "DEBIT_CARD": 5
  }
}
```

### Health Check

#### GET `/api/payments/health`
Check payment service health status.

### Supported Methods

#### GET `/api/payments/methods`
Get list of supported payment methods.

## Database Schema

### Payments Table
```sql
CREATE TABLE payments (
    id BIGSERIAL PRIMARY KEY,
    parking_session_id BIGINT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    payment_method VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    transaction_id VARCHAR(50) UNIQUE,
    card_last_four VARCHAR(4),
    card_brand VARCHAR(20),
    receipt_url VARCHAR(500),
    failure_reason TEXT,
    processed_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (parking_session_id) REFERENCES parking_sessions(id)
);
```

### Indexes
- `idx_payments_parking_session_id`: For session lookups
- `idx_payments_status`: For status filtering
- `idx_payments_payment_method`: For method filtering
- `idx_payments_transaction_id`: For transaction lookups
- `idx_payments_created_at`: For date range queries
- `idx_payments_session_unique`: Ensures one completed payment per session

## Security Features

### Data Protection
- Card numbers are not stored in the database
- Only last 4 digits are stored for receipt purposes
- All sensitive data is encrypted in transit
- Transaction IDs are generated for audit trails

### Validation
- Card number format validation
- Expiry date validation
- CVV validation
- Amount validation
- Session existence validation

### Error Handling
- Comprehensive error messages
- Payment failure logging
- Retry mechanisms for transient failures
- Graceful degradation

## Integration Points

### Payment Gateways
The system is designed to integrate with real payment gateways:

1. **Stripe Integration**
   - Credit/debit card processing
   - Apple Pay/Google Pay
   - Webhook support

2. **Square Integration**
   - Point-of-sale integration
   - Mobile payment support

3. **LINE Pay Integration**
   - Japanese market support
   - QR code payments

4. **PayPay Integration**
   - Japanese electronic wallet
   - QR code support

### QR Code Standards
- Support for various QR code formats
- Integration with mobile payment apps
- Custom QR code generation

## Testing

### Unit Tests
- Payment service methods
- Validation logic
- Error handling

### Integration Tests
- API endpoint testing
- Database operations
- Payment flow testing

### Manual Testing
- Frontend payment form
- Different payment methods
- Error scenarios

## Deployment

### Prerequisites
- Java 17+
- PostgreSQL 12+
- Node.js 16+ (for frontend)

### Environment Variables
```bash
# Database
DB_URL=jdbc:postgresql://localhost:5432/parking_db
DB_USERNAME=parking_user
DB_PASSWORD=parking_password

# Payment Gateway (for production)
STRIPE_SECRET_KEY=sk_test_...
SQUARE_ACCESS_TOKEN=...
LINE_PAY_CHANNEL_ID=...
```

### Build and Run
```bash
# Backend
./gradlew build
./gradlew bootRun

# Frontend
cd frontend
npm install
npm start
```

## Monitoring and Analytics

### Payment Metrics
- Total revenue tracking
- Payment method distribution
- Success/failure rates
- Average transaction value

### Error Monitoring
- Failed payment logging
- Gateway error tracking
- Performance metrics

### Business Intelligence
- Revenue trends
- Payment method preferences
- Peak usage times
- Customer behavior analysis

## Future Enhancements

### Planned Features
1. **Recurring Payments**: Monthly parking subscriptions
2. **Split Payments**: Multiple payment methods for one session
3. **Payment Plans**: Installment payment options
4. **Loyalty Program**: Points and discounts
5. **International Payments**: Multi-currency support

### Technical Improvements
1. **Real-time Processing**: WebSocket integration
2. **Advanced Analytics**: Machine learning insights
3. **Mobile App**: Native payment integration
4. **Blockchain**: Cryptocurrency payments
5. **AI Fraud Detection**: Advanced security

## Support and Maintenance

### Troubleshooting
- Common payment issues
- Gateway integration problems
- Database performance issues

### Maintenance Tasks
- Regular security updates
- Database optimization
- Payment gateway updates
- Performance monitoring

## License

This payment system is part of the Parking Management System and is licensed under the MIT License. 