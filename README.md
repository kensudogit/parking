# 駐車場管理システム (Parking Management System)

## 概要

このプロジェクトは、包括的な駐車場管理システムです。支払い処理、認証・認可、通知システム、多言語対応、管理者ダッシュボードなどの機能を提供します。

## 主な機能

### 🔐 認証・認可システム
- **ユーザー登録・ログイン**: JWTトークンベースの認証
- **ロールベースアクセス制御**: ユーザー、管理者の権限管理
- **パスワード暗号化**: BCryptによるセキュアなパスワード管理
- **セッション管理**: トークンの有効期限管理

### 💳 支払いシステム
- **多様な支払い方法**: クレジットカード、デビットカード、現金、電子ウォレット、モバイル決済、QRコード
- **支払い処理**: リアルタイム決済処理と検証
- **返金処理**: 自動・手動返金機能
- **支払い履歴**: 詳細な取引履歴と統計

### 📧 通知システム
- **多チャンネル通知**: メール、SMS、プッシュ通知、システム通知
- **通知管理**: 既読・未読管理、通知削除、再送信機能
- **優先度管理**: 低、通常、高、緊急の優先度設定
- **通知統計**: 送信成功率、失敗率の監視

### 📊 管理者ダッシュボード
- **リアルタイム統計**: 収益、利用状況、支払い方法別統計
- **レポート機能**: 日次・月次・年次レポート
- **システム監視**: ヘルスチェック、パフォーマンス監視

### 🌐 多言語対応
- **日本語・英語対応**: メッセージリソースの多言語化
- **ロケール設定**: 動的言語切り替え

### 🔒 セキュリティ機能
- **Spring Security**: 包括的なセキュリティ設定
- **CORS設定**: クロスオリジンリクエストの適切な処理
- **入力検証**: バリデーション機能
- **エラーハンドリング**: セキュアなエラー処理

## 技術スタック

### バックエンド
- **Spring Boot 3.2.0**: メインフレームワーク
- **Spring Security**: 認証・認可
- **Spring Data JPA**: データアクセス
- **PostgreSQL**: データベース
- **JWT**: トークンベース認証
- **Doma2**: SQLマッピングフレームワーク

### フロントエンド
- **React**: ユーザーインターフェース
- **CSS3**: モダンなスタイリング
- **レスポンシブデザイン**: モバイル対応

## データベーススキーマ

### 主要テーブル
- `users`: ユーザー情報
- `roles`: ロール定義
- `payments`: 支払い情報
- `notifications`: 通知データ
- `reports`: レポート情報
- `parking_sessions`: 駐車場セッション

## API エンドポイント

### 認証 API
```
POST /api/auth/register     # ユーザー登録
POST /api/auth/login        # ログイン
POST /api/auth/change-password  # パスワード変更
POST /api/auth/validate-token   # トークン検証
```

### 支払い API
```
POST /api/payments/process      # 支払い処理
POST /api/payments/refund       # 返金処理
GET  /api/payments/statistics   # 支払い統計
GET  /api/payments/history      # 支払い履歴
```

### 通知 API
```
GET  /api/notifications/user/{userId}      # ユーザー通知一覧
GET  /api/notifications/user/{userId}/unread  # 未読通知
PUT  /api/notifications/{id}/read          # 既読処理
DELETE /api/notifications/{id}              # 通知削除
POST /api/notifications/{id}/resend        # 再送信
```

### ダッシュボード API
```
GET /api/dashboard/overview        # 概要統計
GET /api/dashboard/revenue         # 収益レポート
GET /api/dashboard/utilization     # 利用率統計
GET /api/dashboard/payment-methods # 支払い方法統計
```

## セットアップ

### 前提条件
- Java 17+
- PostgreSQL 12+
- Node.js 16+
- npm または yarn

### バックエンドセットアップ

1. **データベース設定**
```sql
CREATE DATABASE parking_management;
```

2. **アプリケーション設定**
```properties
# application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/parking_management
spring.datasource.username=your_username
spring.datasource.password=your_password

# JWT設定
jwt.secret=your_jwt_secret_key
jwt.expiration=86400000

# メール設定
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password
```

3. **アプリケーション起動**
```bash
cd devlop/parking
./gradlew bootRun
```

### フロントエンドセットアップ

1. **依存関係インストール**
```bash
cd devlop/parking/frontend
npm install
```

2. **開発サーバー起動**
```bash
npm start
```

## 使用方法

### 1. ユーザー登録・ログイン
- フロントエンドの認証フォームからユーザー登録
- JWTトークンによる自動認証

### 2. 支払い処理
- 複数の支払い方法から選択
- リアルタイム決済処理
- 支払い履歴の確認

### 3. 通知管理
- 通知センターで通知一覧表示
- 既読・未読の管理
- 通知の削除・再送信

### 4. 管理者機能
- ダッシュボードで統計確認
- レポート生成
- システム監視

## セキュリティ考慮事項

### 認証・認可
- JWTトークンの適切な管理
- パスワードの暗号化（BCrypt）
- ロールベースアクセス制御

### データ保護
- 入力値の検証
- SQLインジェクション対策
- XSS対策

### 通信セキュリティ
- HTTPS通信の強制
- CORS設定の適切な管理
- セキュアなヘッダー設定

## 監視・ログ

### アプリケーション監視
- ヘルスチェックエンドポイント
- パフォーマンスメトリクス
- エラーログの収集

### 通知監視
- 送信成功率の監視
- 失敗通知の自動検出
- 通知統計の定期レポート

## 今後の拡張予定

### 短期目標
- [ ] リアルタイム通知（WebSocket）
- [ ] モバイルアプリ対応
- [ ] 決済ゲートウェイ統合

### 中期目標
- [ ] AI予測機能（需要予測）
- [ ] 動的料金設定
- [ ] 予約システム

### 長期目標
- [ ] マルチテナント対応
- [ ] マイクロサービス化
- [ ] クラウドネイティブ対応

## トラブルシューティング

### よくある問題

1. **データベース接続エラー**
   - PostgreSQLサービスの確認
   - 接続情報の確認

2. **JWT認証エラー**
   - トークンの有効期限確認
   - シークレットキーの設定確認

3. **通知送信エラー**
   - メール設定の確認
   - SMS設定の確認

## ライセンス

このプロジェクトはMITライセンスの下で公開されています。

## 貢献

プルリクエストやイシューの報告を歓迎します。貢献する前に、コーディング規約を確認してください。

## サポート

技術的な質問やサポートが必要な場合は、イシューを作成してください。

---

**注意**: 本システムは開発・テスト環境用です。本番環境での使用前に、セキュリティ設定の見直しと十分なテストを実施してください。 
