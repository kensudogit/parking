-- Create parking_spots table
CREATE TABLE parking_spots (
    id BIGSERIAL PRIMARY KEY,
    spot_number VARCHAR(50) UNIQUE NOT NULL,
    spot_type VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE',
    floor_level INTEGER,
    hourly_rate DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create parking_sessions table
CREATE TABLE parking_sessions (
    id BIGSERIAL PRIMARY KEY,
    parking_spot_id BIGINT NOT NULL,
    license_plate VARCHAR(20) NOT NULL,
    entry_time TIMESTAMP NOT NULL,
    exit_time TIMESTAMP,
    total_amount DECIMAL(10,2),
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    payment_status VARCHAR(20) DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (parking_spot_id) REFERENCES parking_spots(id)
);

-- Create indexes for better performance
CREATE INDEX idx_parking_spots_status ON parking_spots(status);
CREATE INDEX idx_parking_spots_spot_type ON parking_spots(spot_type);
CREATE INDEX idx_parking_spots_spot_number ON parking_spots(spot_number);
CREATE INDEX idx_parking_sessions_license_plate ON parking_sessions(license_plate);
CREATE INDEX idx_parking_sessions_status ON parking_sessions(status);
CREATE INDEX idx_parking_sessions_payment_status ON parking_sessions(payment_status);
CREATE INDEX idx_parking_sessions_entry_time ON parking_sessions(entry_time);

-- Insert sample data
INSERT INTO parking_spots (spot_number, spot_type, status, floor_level, hourly_rate) VALUES
('A-001', 'REGULAR', 'AVAILABLE', 1, 5.00),
('A-002', 'REGULAR', 'AVAILABLE', 1, 5.00),
('A-003', 'REGULAR', 'AVAILABLE', 1, 5.00),
('B-001', 'DISABLED', 'AVAILABLE', 1, 3.00),
('B-002', 'DISABLED', 'AVAILABLE', 1, 3.00),
('C-001', 'ELECTRIC_CHARGING', 'AVAILABLE', 1, 7.00),
('C-002', 'ELECTRIC_CHARGING', 'AVAILABLE', 1, 7.00),
('D-001', 'MOTORCYCLE', 'AVAILABLE', 1, 2.00),
('D-002', 'MOTORCYCLE', 'AVAILABLE', 1, 2.00),
('E-001', 'TRUCK', 'AVAILABLE', 1, 10.00); 