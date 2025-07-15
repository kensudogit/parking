import React, { useState } from 'react';
import './AuthForm.css';

/**
 * 認証フォームコンポーネント
 * ログインと登録機能を提供
 */
const AuthForm = ({ onAuthSuccess }) => {
    const [isLogin, setIsLogin] = useState(true);
    const [formData, setFormData] = useState({
        username: '',
        email: '',
        password: '',
        confirmPassword: '',
        firstName: '',
        lastName: '',
        phoneNumber: ''
    });
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');

    /**
     * フォームデータを更新
     */
    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    /**
     * フォームを送信
     */
    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError('');
        setSuccess('');

        try {
            const endpoint = isLogin ? '/api/auth/login' : '/api/auth/register';
            const requestData = isLogin 
                ? { username: formData.username, password: formData.password }
                : formData;

            const response = await fetch(endpoint, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(requestData)
            });

            const data = await response.json();

            if (response.ok) {
                setSuccess(data.message || (isLogin ? 'ログインに成功しました' : '登録に成功しました'));
                
                // トークンをローカルストレージに保存
                if (data.token) {
                    localStorage.setItem('authToken', data.token);
                    localStorage.setItem('userInfo', JSON.stringify({
                        userId: data.userId,
                        username: data.username,
                        email: data.email
                    }));
                }

                // 認証成功コールバックを呼び出し
                if (onAuthSuccess) {
                    onAuthSuccess(data);
                }

                // フォームをリセット
                setFormData({
                    username: '',
                    email: '',
                    password: '',
                    confirmPassword: '',
                    firstName: '',
                    lastName: '',
                    phoneNumber: ''
                });
            } else {
                setError(data.message || 'エラーが発生しました');
            }
        } catch (err) {
            setError('ネットワークエラーが発生しました');
        } finally {
            setLoading(false);
        }
    };

    /**
     * モードを切り替え
     */
    const toggleMode = () => {
        setIsLogin(!isLogin);
        setError('');
        setSuccess('');
        setFormData({
            username: '',
            email: '',
            password: '',
            confirmPassword: '',
            firstName: '',
            lastName: '',
            phoneNumber: ''
        });
    };

    return (
        <div className="auth-container">
            <div className="auth-form">
                <h2>{isLogin ? 'ログイン' : 'ユーザー登録'}</h2>
                
                {error && <div className="error-message">{error}</div>}
                {success && <div className="success-message">{success}</div>}
                
                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label htmlFor="username">ユーザー名 *</label>
                        <input
                            type="text"
                            id="username"
                            name="username"
                            value={formData.username}
                            onChange={handleInputChange}
                            required
                            placeholder="ユーザー名を入力"
                        />
                    </div>

                    {!isLogin && (
                        <>
                            <div className="form-group">
                                <label htmlFor="email">メールアドレス *</label>
                                <input
                                    type="email"
                                    id="email"
                                    name="email"
                                    value={formData.email}
                                    onChange={handleInputChange}
                                    required
                                    placeholder="メールアドレスを入力"
                                />
                            </div>

                            <div className="form-row">
                                <div className="form-group">
                                    <label htmlFor="firstName">姓 *</label>
                                    <input
                                        type="text"
                                        id="firstName"
                                        name="firstName"
                                        value={formData.firstName}
                                        onChange={handleInputChange}
                                        required
                                        placeholder="姓"
                                    />
                                </div>

                                <div className="form-group">
                                    <label htmlFor="lastName">名 *</label>
                                    <input
                                        type="text"
                                        id="lastName"
                                        name="lastName"
                                        value={formData.lastName}
                                        onChange={handleInputChange}
                                        required
                                        placeholder="名"
                                    />
                                </div>
                            </div>

                            <div className="form-group">
                                <label htmlFor="phoneNumber">電話番号</label>
                                <input
                                    type="tel"
                                    id="phoneNumber"
                                    name="phoneNumber"
                                    value={formData.phoneNumber}
                                    onChange={handleInputChange}
                                    placeholder="電話番号を入力"
                                />
                            </div>
                        </>
                    )}

                    <div className="form-group">
                        <label htmlFor="password">パスワード *</label>
                        <input
                            type="password"
                            id="password"
                            name="password"
                            value={formData.password}
                            onChange={handleInputChange}
                            required
                            placeholder="パスワードを入力"
                        />
                    </div>

                    {!isLogin && (
                        <div className="form-group">
                            <label htmlFor="confirmPassword">パスワード確認 *</label>
                            <input
                                type="password"
                                id="confirmPassword"
                                name="confirmPassword"
                                value={formData.confirmPassword}
                                onChange={handleInputChange}
                                required
                                placeholder="パスワードを再入力"
                            />
                        </div>
                    )}

                    <button 
                        type="submit" 
                        className="submit-btn" 
                        disabled={loading}
                    >
                        {loading ? '処理中...' : (isLogin ? 'ログイン' : '登録')}
                    </button>
                </form>

                <div className="auth-toggle">
                    <p>
                        {isLogin ? 'アカウントをお持ちでない方' : '既にアカウントをお持ちの方'}
                        <button 
                            type="button" 
                            className="toggle-btn"
                            onClick={toggleMode}
                        >
                            {isLogin ? '新規登録' : 'ログイン'}
                        </button>
                    </p>
                </div>
            </div>
        </div>
    );
};

export default AuthForm; 