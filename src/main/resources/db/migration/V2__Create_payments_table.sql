-- Create payments table
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

-- Create indexes for better performance
CREATE INDEX idx_payments_parking_session_id ON payments(parking_session_id);
CREATE INDEX idx_payments_status ON payments(status);
CREATE INDEX idx_payments_payment_method ON payments(payment_method);
CREATE INDEX idx_payments_transaction_id ON payments(transaction_id);
CREATE INDEX idx_payments_created_at ON payments(created_at);

-- Add unique constraint to ensure one payment per session
CREATE UNIQUE INDEX idx_payments_session_unique ON payments(parking_session_id) WHERE status = 'COMPLETED'; 