# 駐車場管理システム API仕様書

## 概要

このドキュメントは、駐車場管理システムのRESTful API仕様を定義します。

**ベースURL**: `http://localhost:8080/api`

**認証**: JWT Token（Bearer認証）

**Content-Type**: `application/json`

## 共通レスポンス形式

### 成功レスポンス
```json
{
  "status": "success",
  "data": { ... },
  "message": "処理が完了しました",
  "timestamp": "2024-01-15T10:30:00"
}
```

### エラーレスポンス
```json
{
  "status": "error",
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "入力内容に問題があります",
    "details": [ ... ]
  },
  "timestamp": "2024-01-15T10:30:00"
}
```

## 認証・認可

### ログイン
**POST** `/auth/login`

**リクエスト**:
```json
{
  "username": "user@example.com",
  "password": "password123"
}
```

**レスポンス**:
```json
{
  "status": "success",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
      "id": 1,
      "username": "user@example.com",
      "firstName": "太郎",
      "lastName": "田中",
      "userType": "CUSTOMER",
      "roles": ["ROLE_CUSTOMER"]
    }
  }
}
```

### ユーザー登録
**POST** `/auth/register`

**リクエスト**:
```json
{
  "username": "newuser@example.com",
  "email": "newuser@example.com",
  "password": "password123",
  "firstName": "花子",
  "lastName": "佐藤",
  "phoneNumber": "090-1234-5678"
}
```

## 駐車場管理

### 駐車スペース一覧取得
**GET** `/parking/spots`

**レスポンス**:
```json
{
  "status": "success",
  "data": [
    {
      "id": 1,
      "spotNumber": "A-001",
      "spotType": "REGULAR",
      "status": "AVAILABLE",
      "floorLevel": 1,
      "hourlyRate": 5.00
    }
  ]
}
```

### 利用可能な駐車スペース取得
**GET** `/parking/spots/available`

### 駐車セッション開始
**POST** `/parking/sessions/start`

**リクエスト**:
```json
{
  "spotId": 1,
  "licensePlate": "ABC-123"
}
```

**レスポンス**:
```json
{
  "status": "success",
  "data": {
    "id": 1,
    "parkingSpot": {
      "id": 1,
      "spotNumber": "A-001"
    },
    "licensePlate": "ABC-123",
    "entryTime": "2024-01-15T10:30:00",
    "status": "ACTIVE",
    "paymentStatus": "PENDING"
  }
}
```

### 駐車セッション終了
**POST** `/parking/sessions/{sessionId}/end`

**レスポンス**:
```json
{
  "status": "success",
  "data": {
    "id": 1,
    "exitTime": "2024-01-15T12:30:00",
    "totalAmount": 10.00,
    "status": "COMPLETED",
    "paymentStatus": "PENDING"
  }
}
```

## 決済管理

### 決済処理
**POST** `/payments/process`

**リクエスト**:
```json
{
  "sessionId": 1,
  "amount": 10.00,
  "paymentMethod": "CREDIT_CARD",
  "cardNumber": "4111111111111111",
  "cardHolderName": "田中太郎",
  "cardExpiryMonth": "12",
  "cardExpiryYear": "2025",
  "cardCvv": "123"
}
```

**レスポンス**:
```json
{
  "status": "success",
  "data": {
    "paymentId": 1,
    "sessionId": 1,
    "licensePlate": "ABC-123",
    "amount": 10.00,
    "paymentMethod": "CREDIT_CARD",
    "status": "COMPLETED",
    "transactionId": "TXN-ABC12345",
    "cardLastFour": "1111",
    "cardBrand": "Visa",
    "receiptUrl": "https://parking-system.com/receipts/TXN-ABC12345.pdf",
    "processedAt": "2024-01-15T12:35:00"
  }
}
```

### 決済履歴取得
**GET** `/payments/session/{sessionId}`

### 返金処理
**POST** `/payments/{paymentId}/refund`

### 決済統計取得
**GET** `/payments/statistics`

**レスポンス**:
```json
{
  "status": "success",
  "data": {
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
}
```

## ダッシュボード

### 概要データ取得
**GET** `/dashboard/overview`

**レスポンス**:
```json
{
  "status": "success",
  "data": {
    "availableSpots": 45,
    "occupiedSpots": 55,
    "activeSessions": 55,
    "paymentStats": {
      "totalPayments": 150,
      "completedPayments": 140,
      "monthlyRevenue": 2250.00
    },
    "todayRevenue": 15000.00,
    "monthlyRevenue": 450000.00,
    "systemStatus": "HEALTHY",
    "lastUpdated": "2024-01-15T10:30:00"
  }
}
```

### 売上レポート取得
**GET** `/dashboard/revenue/{period}`

**パラメータ**:
- `period`: `daily`, `weekly`, `monthly`

### 使用率レポート取得
**GET** `/dashboard/utilization`

**レスポンス**:
```json
{
  "status": "success",
  "data": {
    "totalSpots": 100,
    "occupiedSpots": 55,
    "availableSpots": 45,
    "utilizationRate": 55.0,
    "generatedAt": "2024-01-15T10:30:00"
  }
}
```

## 通知管理

### 通知一覧取得
**GET** `/notifications`

**レスポンス**:
```json
{
  "status": "success",
  "data": [
    {
      "id": 1,
      "title": "駐車開始通知",
      "message": "駐車スペースA-001で駐車を開始しました",
      "type": "PARKING_START",
      "status": "UNREAD",
      "deliveryMethod": "IN_APP",
      "createdAt": "2024-01-15T10:30:00"
    }
  ]
}
```

### 通知を既読にする
**PUT** `/notifications/{notificationId}/read`

## レポート管理

### レポート生成
**POST** `/reports/generate`

**リクエスト**:
```json
{
  "name": "月次売上レポート",
  "type": "MONTHLY_REVENUE",
  "startDate": "2024-01-01",
  "endDate": "2024-01-31"
}
```

### レポート一覧取得
**GET** `/reports`

### レポートダウンロード
**GET** `/reports/{reportId}/download`

## ユーザー管理

### ユーザー一覧取得
**GET** `/users`

### ユーザー詳細取得
**GET** `/users/{userId}`

### ユーザー作成
**POST** `/users`

**リクエスト**:
```json
{
  "username": "newuser@example.com",
  "email": "newuser@example.com",
  "password": "password123",
  "firstName": "花子",
  "lastName": "佐藤",
  "phoneNumber": "090-1234-5678",
  "userType": "CUSTOMER"
}
```

### ユーザー更新
**PUT** `/users/{userId}`

### ユーザー削除
**DELETE** `/users/{userId}`

## エラーコード

| コード | 説明 |
|--------|------|
| `VALIDATION_ERROR` | バリデーションエラー |
| `NOT_FOUND` | リソースが見つからない |
| `UNAUTHORIZED` | 認証が必要 |
| `FORBIDDEN` | アクセス権限がない |
| `CONFLICT` | リソース競合 |
| `INTERNAL_ERROR` | 内部サーバーエラー |

## ステータスコード

| コード | 説明 |
|--------|------|
| 200 | OK |
| 201 | Created |
| 400 | Bad Request |
| 401 | Unauthorized |
| 403 | Forbidden |
| 404 | Not Found |
| 409 | Conflict |
| 500 | Internal Server Error |

## レート制限

- **認証エンドポイント**: 5回/分
- **一般エンドポイント**: 100回/分
- **レポート生成**: 10回/分

## バージョニング

APIバージョンはURLパスで管理します：
- 現在のバージョン: `/api/v1/`
- 将来のバージョン: `/api/v2/`

## 変更履歴

| バージョン | 日付 | 変更内容 |
|------------|------|----------|
| 1.0.0 | 2024-01-15 | 初回リリース |
| 1.1.0 | 2024-01-20 | 決済機能追加 |
| 1.2.0 | 2024-01-25 | 通知機能追加 | 