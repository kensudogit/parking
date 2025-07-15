import React, { useState } from 'react';
import './App.css';
import PaymentForm from './components/PaymentForm';

function App() {
  const [showPaymentForm, setShowPaymentForm] = useState(false);
  const [sessionId, setSessionId] = useState(1);
  const [amount, setAmount] = useState(15.00);
  const [paymentResult, setPaymentResult] = useState(null);

  const handlePaymentComplete = (result) => {
    setPaymentResult(result);
    console.log('Payment completed:', result);
  };

  const handleStartPayment = () => {
    setShowPaymentForm(true);
    setPaymentResult(null);
  };

  const handleBackToDemo = () => {
    setShowPaymentForm(false);
    setPaymentResult(null);
  };

  if (showPaymentForm) {
    return (
      <div className="App">
        <div className="payment-demo-container">
          <button onClick={handleBackToDemo} className="back-button">
            ← Back to Demo
          </button>
          <PaymentForm 
            sessionId={sessionId}
            amount={amount}
            onPaymentComplete={handlePaymentComplete}
          />
          {paymentResult && (
            <div className="payment-result">
              <h3>Payment Result</h3>
              <pre>{JSON.stringify(paymentResult, null, 2)}</pre>
            </div>
          )}
        </div>
      </div>
    );
  }

  return (
    <div className="App">
      <div className="demo-container">
        <h1>🚗 Parking Payment System Demo</h1>
        <p>This is a demonstration of the comprehensive payment system for the parking management application.</p>
        
        <div className="demo-section">
          <h2>Payment Features</h2>
          <ul>
            <li>💳 Credit/Debit Card Payments</li>
            <li>📱 Mobile Payment (Apple Pay, Google Pay)</li>
            <li>📱 QR Code Payments</li>
            <li>💰 Electronic Wallet (LINE Pay, PayPay)</li>
            <li>💵 Cash Payments</li>
            <li>🔄 Refund Processing</li>
            <li>📊 Payment Statistics</li>
          </ul>
        </div>

        <div className="demo-section">
          <h2>Try the Payment Form</h2>
          <div className="demo-form">
            <div className="form-group">
              <label>Session ID:</label>
              <input 
                type="number" 
                value={sessionId} 
                onChange={(e) => setSessionId(parseInt(e.target.value))}
                min="1"
              />
            </div>
            <div className="form-group">
              <label>Amount (¥):</label>
              <input 
                type="number" 
                value={amount} 
                onChange={(e) => setAmount(parseFloat(e.target.value))}
                min="0.01"
                step="0.01"
              />
            </div>
            <button onClick={handleStartPayment} className="demo-button">
              Start Payment Demo
            </button>
          </div>
        </div>

        <div className="demo-section">
          <h2>API Endpoints</h2>
          <div className="api-endpoints">
            <div className="endpoint">
              <strong>POST /api/payments/process</strong>
              <p>Process payment for parking session</p>
            </div>
            <div className="endpoint">
              <strong>GET /api/payments/session/{sessionId}</strong>
              <p>Get payment by session ID</p>
            </div>
            <div className="endpoint">
              <strong>POST /api/payments/{paymentId}/refund</strong>
              <p>Process refund for payment</p>
            </div>
            <div className="endpoint">
              <strong>GET /api/payments/statistics</strong>
              <p>Get payment statistics and revenue data</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default App;
