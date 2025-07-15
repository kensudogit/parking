import React, { useState } from 'react';
import './PaymentForm.css';

const PaymentForm = ({ sessionId, amount, onPaymentComplete }) => {
  const [paymentMethod, setPaymentMethod] = useState('CREDIT_CARD');
  const [formData, setFormData] = useState({
    sessionId: sessionId,
    amount: amount,
    paymentMethod: 'CREDIT_CARD',
    cardNumber: '',
    cardHolderName: '',
    cardExpiryMonth: '',
    cardExpiryYear: '',
    cardCvv: '',
    phoneNumber: '',
    walletType: '',
    qrCodeData: '',
    walletId: '',
    walletProvider: ''
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handlePaymentMethodChange = (method) => {
    setPaymentMethod(method);
    setFormData(prev => ({
      ...prev,
      paymentMethod: method
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    setSuccess('');

    try {
      const response = await fetch('http://localhost:8080/api/payments/process', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData)
      });

      if (response.ok) {
        const result = await response.json();
        setSuccess('Payment processed successfully!');
        if (onPaymentComplete) {
          onPaymentComplete(result);
        }
      } else {
        const errorData = await response.json();
        setError(errorData.message || 'Payment processing failed');
      }
    } catch (err) {
      setError('Network error. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const renderCardForm = () => (
    <div className="card-form">
      <div className="form-group">
        <label>Card Number</label>
        <input
          type="text"
          name="cardNumber"
          value={formData.cardNumber}
          onChange={handleInputChange}
          placeholder="1234 5678 9012 3456"
          maxLength="19"
        />
      </div>
      <div className="form-group">
        <label>Cardholder Name</label>
        <input
          type="text"
          name="cardHolderName"
          value={formData.cardHolderName}
          onChange={handleInputChange}
          placeholder="John Doe"
        />
      </div>
      <div className="form-row">
        <div className="form-group">
          <label>Expiry Month</label>
          <select name="cardExpiryMonth" value={formData.cardExpiryMonth} onChange={handleInputChange}>
            <option value="">MM</option>
            {Array.from({length: 12}, (_, i) => i + 1).map(month => (
              <option key={month} value={month.toString().padStart(2, '0')}>
                {month.toString().padStart(2, '0')}
              </option>
            ))}
          </select>
        </div>
        <div className="form-group">
          <label>Expiry Year</label>
          <select name="cardExpiryYear" value={formData.cardExpiryYear} onChange={handleInputChange}>
            <option value="">YYYY</option>
            {Array.from({length: 10}, (_, i) => new Date().getFullYear() + i).map(year => (
              <option key={year} value={year}>{year}</option>
            ))}
          </select>
        </div>
        <div className="form-group">
          <label>CVV</label>
          <input
            type="text"
            name="cardCvv"
            value={formData.cardCvv}
            onChange={handleInputChange}
            placeholder="123"
            maxLength="4"
          />
        </div>
      </div>
    </div>
  );

  const renderMobilePaymentForm = () => (
    <div className="mobile-payment-form">
      <div className="form-group">
        <label>Phone Number</label>
        <input
          type="tel"
          name="phoneNumber"
          value={formData.phoneNumber}
          onChange={handleInputChange}
          placeholder="+81-90-1234-5678"
        />
      </div>
      <div className="form-group">
        <label>Wallet Type</label>
        <select name="walletType" value={formData.walletType} onChange={handleInputChange}>
          <option value="">Select Wallet</option>
          <option value="Apple Pay">Apple Pay</option>
          <option value="Google Pay">Google Pay</option>
          <option value="Samsung Pay">Samsung Pay</option>
          <option value="PayPal">PayPal</option>
        </select>
      </div>
    </div>
  );

  const renderQrCodeForm = () => (
    <div className="qr-code-form">
      <div className="form-group">
        <label>QR Code Data</label>
        <input
          type="text"
          name="qrCodeData"
          value={formData.qrCodeData}
          onChange={handleInputChange}
          placeholder="parking-payment-12345"
        />
      </div>
      <div className="qr-info">
        <p>Scan the QR code with your mobile payment app to complete the payment.</p>
      </div>
    </div>
  );

  const renderElectronicWalletForm = () => (
    <div className="electronic-wallet-form">
      <div className="form-group">
        <label>Wallet Provider</label>
        <select name="walletProvider" value={formData.walletProvider} onChange={handleInputChange}>
          <option value="">Select Provider</option>
          <option value="LINE Pay">LINE Pay</option>
          <option value="PayPay">PayPay</option>
          <option value="Rakuten Pay">Rakuten Pay</option>
          <option value="d Payment">d Payment</option>
        </select>
      </div>
      <div className="form-group">
        <label>Wallet ID</label>
        <input
          type="text"
          name="walletId"
          value={formData.walletId}
          onChange={handleInputChange}
          placeholder="wallet-12345"
        />
      </div>
    </div>
  );

  const renderCashForm = () => (
    <div className="cash-form">
      <div className="cash-info">
        <p>Please pay the amount of <strong>¥{amount}</strong> at the payment counter.</p>
        <p>Your transaction will be marked as completed once payment is received.</p>
      </div>
    </div>
  );

  const renderPaymentMethodForm = () => {
    switch (paymentMethod) {
      case 'CREDIT_CARD':
      case 'DEBIT_CARD':
        return renderCardForm();
      case 'MOBILE_PAYMENT':
        return renderMobilePaymentForm();
      case 'QR_CODE':
        return renderQrCodeForm();
      case 'ELECTRONIC_WALLET':
        return renderElectronicWalletForm();
      case 'CASH':
        return renderCashForm();
      default:
        return null;
    }
  };

  return (
    <div className="payment-form-container">
      <h2>Payment Form</h2>
      <div className="payment-summary">
        <p><strong>Session ID:</strong> {sessionId}</p>
        <p><strong>Amount:</strong> ¥{amount}</p>
      </div>

      <form onSubmit={handleSubmit} className="payment-form">
        <div className="payment-methods">
          <h3>Select Payment Method</h3>
          <div className="method-buttons">
            {[
              { value: 'CREDIT_CARD', label: 'Credit Card', icon: '💳' },
              { value: 'DEBIT_CARD', label: 'Debit Card', icon: '💳' },
              { value: 'MOBILE_PAYMENT', label: 'Mobile Payment', icon: '📱' },
              { value: 'QR_CODE', label: 'QR Code', icon: '📱' },
              { value: 'ELECTRONIC_WALLET', label: 'E-Wallet', icon: '💰' },
              { value: 'CASH', label: 'Cash', icon: '💵' }
            ].map(method => (
              <button
                key={method.value}
                type="button"
                className={`method-button ${paymentMethod === method.value ? 'active' : ''}`}
                onClick={() => handlePaymentMethodChange(method.value)}
              >
                <span className="method-icon">{method.icon}</span>
                <span className="method-label">{method.label}</span>
              </button>
            ))}
          </div>
        </div>

        <div className="payment-details">
          <h3>Payment Details</h3>
          {renderPaymentMethodForm()}
        </div>

        {error && <div className="error-message">{error}</div>}
        {success && <div className="success-message">{success}</div>}

        <div className="form-actions">
          <button type="submit" disabled={loading} className="submit-button">
            {loading ? 'Processing...' : `Pay ¥${amount}`}
          </button>
        </div>
      </form>
    </div>
  );
};

export default PaymentForm; 