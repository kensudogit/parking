import React, { useState, useEffect } from 'react';
import './NotificationCenter.css';

/**
 * 通知センターコンポーネント
 * 通知の表示と管理機能を提供
 */
const NotificationCenter = ({ userId }) => {
    const [notifications, setNotifications] = useState([]);
    const [unreadCount, setUnreadCount] = useState(0);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [showAll, setShowAll] = useState(false);

    /**
     * 通知を取得
     */
    const fetchNotifications = async () => {
        if (!userId) return;

        setLoading(true);
        setError('');

        try {
            const endpoint = showAll 
                ? `/api/notifications/user/${userId}`
                : `/api/notifications/user/${userId}/unread`;
            
            const response = await fetch(endpoint, {
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('authToken')}`,
                    'Content-Type': 'application/json'
                }
            });

            if (response.ok) {
                const data = await response.json();
                setNotifications(data);
                if (!showAll) {
                    setUnreadCount(data.length);
                }
            } else {
                setError('通知の取得に失敗しました');
            }
        } catch (err) {
            setError('ネットワークエラーが発生しました');
        } finally {
            setLoading(false);
        }
    };

    /**
     * 通知を既読にする
     */
    const markAsRead = async (notificationId) => {
        try {
            const response = await fetch(`/api/notifications/${notificationId}/read`, {
                method: 'PUT',
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('authToken')}`,
                    'Content-Type': 'application/json'
                }
            });

            if (response.ok) {
                // 通知リストを更新
                setNotifications(prev => 
                    prev.map(notification => 
                        notification.id === notificationId 
                            ? { ...notification, readAt: new Date().toISOString() }
                            : notification
                    )
                );
                setUnreadCount(prev => Math.max(0, prev - 1));
            }
        } catch (err) {
            console.error('通知の既読処理に失敗しました:', err);
        }
    };

    /**
     * 通知を削除
     */
    const deleteNotification = async (notificationId) => {
        try {
            const response = await fetch(`/api/notifications/${notificationId}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('authToken')}`,
                    'Content-Type': 'application/json'
                }
            });

            if (response.ok) {
                setNotifications(prev => prev.filter(n => n.id !== notificationId));
                if (!showAll) {
                    setUnreadCount(prev => Math.max(0, prev - 1));
                }
            }
        } catch (err) {
            console.error('通知の削除に失敗しました:', err);
        }
    };

    /**
     * 通知を再送信
     */
    const resendNotification = async (notificationId) => {
        try {
            const response = await fetch(`/api/notifications/${notificationId}/resend`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('authToken')}`,
                    'Content-Type': 'application/json'
                }
            });

            if (response.ok) {
                const updatedNotification = await response.json();
                setNotifications(prev => 
                    prev.map(notification => 
                        notification.id === notificationId 
                            ? updatedNotification
                            : notification
                    )
                );
            }
        } catch (err) {
            console.error('通知の再送信に失敗しました:', err);
        }
    };

    /**
     * 通知タイプの表示名を取得
     */
    const getNotificationTypeLabel = (type) => {
        const typeLabels = {
            'EMAIL': 'メール',
            'SMS': 'SMS',
            'PUSH': 'プッシュ',
            'SYSTEM': 'システム'
        };
        return typeLabels[type] || type;
    };

    /**
     * 優先度の表示名を取得
     */
    const getPriorityLabel = (priority) => {
        const priorityLabels = {
            'LOW': '低',
            'NORMAL': '通常',
            'HIGH': '高',
            'CRITICAL': '緊急'
        };
        return priorityLabels[priority] || priority;
    };

    /**
     * ステータスの表示名を取得
     */
    const getStatusLabel = (status) => {
        const statusLabels = {
            'PENDING': '送信待ち',
            'SENT': '送信済み',
            'FAILED': '送信失敗'
        };
        return statusLabels[status] || status;
    };

    /**
     * 日時をフォーマット
     */
    const formatDateTime = (dateString) => {
        if (!dateString) return '';
        const date = new Date(dateString);
        return date.toLocaleString('ja-JP');
    };

    // コンポーネントマウント時に通知を取得
    useEffect(() => {
        fetchNotifications();
    }, [userId, showAll]);

    // 定期的に通知を更新（5分間隔）
    useEffect(() => {
        const interval = setInterval(() => {
            if (!showAll) {
                fetchNotifications();
            }
        }, 5 * 60 * 1000);

        return () => clearInterval(interval);
    }, [showAll]);

    if (loading) {
        return (
            <div className="notification-center">
                <div className="loading">読み込み中...</div>
            </div>
        );
    }

    return (
        <div className="notification-center">
            <div className="notification-header">
                <h3>通知センター</h3>
                <div className="notification-controls">
                    <button 
                        className={`toggle-btn ${!showAll ? 'active' : ''}`}
                        onClick={() => setShowAll(false)}
                    >
                        未読 ({unreadCount})
                    </button>
                    <button 
                        className={`toggle-btn ${showAll ? 'active' : ''}`}
                        onClick={() => setShowAll(true)}
                    >
                        すべて
                    </button>
                </div>
            </div>

            {error && <div className="error-message">{error}</div>}

            <div className="notification-list">
                {notifications.length === 0 ? (
                    <div className="no-notifications">
                        {showAll ? '通知がありません' : '未読の通知がありません'}
                    </div>
                ) : (
                    notifications.map(notification => (
                        <div 
                            key={notification.id} 
                            className={`notification-item ${notification.readAt ? 'read' : 'unread'} ${notification.priority.toLowerCase()}`}
                        >
                            <div className="notification-content">
                                <div className="notification-header">
                                    <h4>{notification.title}</h4>
                                    <div className="notification-meta">
                                        <span className="notification-type">
                                            {getNotificationTypeLabel(notification.type)}
                                        </span>
                                        <span className="notification-priority">
                                            {getPriorityLabel(notification.priority)}
                                        </span>
                                        <span className="notification-status">
                                            {getStatusLabel(notification.status)}
                                        </span>
                                    </div>
                                </div>
                                
                                <p className="notification-message">{notification.message}</p>
                                
                                <div className="notification-footer">
                                    <span className="notification-time">
                                        {formatDateTime(notification.createdAt)}
                                    </span>
                                    
                                    <div className="notification-actions">
                                        {!notification.readAt && (
                                            <button 
                                                className="action-btn read-btn"
                                                onClick={() => markAsRead(notification.id)}
                                            >
                                                既読
                                            </button>
                                        )}
                                        
                                        {notification.status === 'FAILED' && (
                                            <button 
                                                className="action-btn resend-btn"
                                                onClick={() => resendNotification(notification.id)}
                                            >
                                                再送信
                                            </button>
                                        )}
                                        
                                        <button 
                                            className="action-btn delete-btn"
                                            onClick={() => deleteNotification(notification.id)}
                                        >
                                            削除
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    ))
                )}
            </div>
        </div>
    );
};

export default NotificationCenter; 