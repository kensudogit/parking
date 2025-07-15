# 駐車場管理システム 運用マニュアル

## 目次

1. [システム概要](#システム概要)
2. [インストール・セットアップ](#インストールセットアップ)
3. [日常運用](#日常運用)
4. [トラブルシューティング](#トラブルシューティング)
5. [バックアップ・復旧](#バックアップ復旧)
6. [セキュリティ](#セキュリティ)
7. [パフォーマンス監視](#パフォーマンス監視)
8. [アップデート・メンテナンス](#アップデートメンテナンス)

## システム概要

### 機能一覧
- **駐車場管理**: 駐車スペースの管理、セッション管理
- **決済処理**: 複数決済方法対応、決済履歴管理
- **ユーザー管理**: 認証・認可、ユーザー情報管理
- **通知機能**: メール・SMS・アプリ内通知
- **レポート機能**: 売上・使用率・各種統計レポート
- **ダッシュボード**: 管理者向けリアルタイム監視
- **多言語対応**: 日本語・英語対応

### システム構成
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   フロントエンド   │    │    バックエンド    │    │   データベース    │
│   (React)       │◄──►│  (Spring Boot)  │◄──►│  (PostgreSQL)   │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

## インストール・セットアップ

### 前提条件
- Java 17以上
- PostgreSQL 12以上
- Node.js 16以上
- メモリ: 4GB以上
- ディスク容量: 10GB以上

### 1. データベースセットアップ

```sql
-- PostgreSQLにログイン
psql -U postgres

-- データベース作成
CREATE DATABASE parking_db;

-- ユーザー作成
CREATE USER parking_user WITH PASSWORD 'parking_password';

-- 権限付与
GRANT ALL PRIVILEGES ON DATABASE parking_db TO parking_user;
```

### 2. アプリケーション設定

`application.yml`の設定:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/parking_db
    username: parking_user
    password: parking_password
  
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
  
  flyway:
    enabled: true
    baseline-on-migrate: true

# 決済設定
payment:
  stripe:
    secret-key: ${STRIPE_SECRET_KEY}
    publishable-key: ${STRIPE_PUBLISHABLE_KEY}
  
  email:
    smtp-host: ${SMTP_HOST}
    smtp-port: ${SMTP_PORT}
    username: ${SMTP_USERNAME}
    password: ${SMTP_PASSWORD}

# 通知設定
notification:
  email:
    enabled: true
  sms:
    enabled: false
  push:
    enabled: true
```

### 3. 環境変数設定

```bash
# データベース
export DB_URL=jdbc:postgresql://localhost:5432/parking_db
export DB_USERNAME=parking_user
export DB_PASSWORD=parking_password

# 決済
export STRIPE_SECRET_KEY=sk_test_...
export STRIPE_PUBLISHABLE_KEY=pk_test_...

# メール
export SMTP_HOST=smtp.gmail.com
export SMTP_PORT=587
export SMTP_USERNAME=your-email@gmail.com
export SMTP_PASSWORD=your-app-password
```

### 4. アプリケーション起動

```bash
# バックエンド起動
cd parking
./gradlew bootRun

# フロントエンド起動
cd frontend
npm install
npm start
```

## 日常運用

### 1. システム監視

#### ヘルスチェック
```bash
# システム状態確認
curl http://localhost:8080/api/dashboard/health

# データベース接続確認
curl http://localhost:8080/api/dashboard/overview
```

#### ログ監視
```bash
# アプリケーションログ確認
tail -f logs/application.log

# エラーログ確認
grep "ERROR" logs/application.log
```

### 2. バックアップ

#### データベースバックアップ
```bash
# 日次バックアップ
pg_dump -U parking_user -d parking_db > backup_$(date +%Y%m%d).sql

# 週次バックアップ（圧縮）
pg_dump -U parking_user -d parking_db | gzip > backup_week_$(date +%Y%m).sql.gz
```

#### ファイルバックアップ
```bash
# 設定ファイルバックアップ
tar -czf config_backup_$(date +%Y%m%d).tar.gz src/main/resources/

# ログファイルバックアップ
tar -czf logs_backup_$(date +%Y%m%d).tar.gz logs/
```

### 3. 定期メンテナンス

#### 日次タスク
- [ ] システムログ確認
- [ ] エラー件数確認
- [ ] 決済成功率確認
- [ ] ディスク使用量確認

#### 週次タスク
- [ ] データベースバックアップ
- [ ] ログファイルローテーション
- [ ] パフォーマンス統計確認
- [ ] セキュリティログ確認

#### 月次タスク
- [ ] システムアップデート
- [ ] セキュリティパッチ適用
- [ ] 容量計画見直し
- [ ] バックアップ復旧テスト

## トラブルシューティング

### よくある問題と対処法

#### 1. アプリケーションが起動しない

**症状**: アプリケーションが起動時にエラーで停止

**原因と対処法**:
```bash
# 1. ログ確認
tail -f logs/application.log

# 2. データベース接続確認
psql -U parking_user -d parking_db -c "SELECT 1;"

# 3. ポート確認
netstat -tlnp | grep 8080

# 4. メモリ確認
free -h
```

#### 2. 決済処理が失敗する

**症状**: 決済処理でエラーが発生

**対処法**:
```bash
# 1. 決済ログ確認
grep "PAYMENT" logs/application.log

# 2. 決済設定確認
curl http://localhost:8080/api/payments/health

# 3. 外部決済サービス確認
curl https://api.stripe.com/v1/account
```

#### 3. データベース接続エラー

**症状**: データベース接続でタイムアウト

**対処法**:
```bash
# 1. PostgreSQLサービス確認
sudo systemctl status postgresql

# 2. 接続数確認
psql -U parking_user -d parking_db -c "SELECT count(*) FROM pg_stat_activity;"

# 3. 設定確認
cat /etc/postgresql/*/main/postgresql.conf | grep max_connections
```

#### 4. メモリ不足

**症状**: OutOfMemoryErrorが発生

**対処法**:
```bash
# 1. JVM設定確認
java -XX:+PrintFlagsFinal -version | grep MaxHeapSize

# 2. メモリ使用量確認
jstat -gc <pid>

# 3. ヒープサイズ調整
export JAVA_OPTS="-Xmx2g -Xms1g"
```

### エラーコード一覧

| エラーコード | 説明 | 対処法 |
|-------------|------|--------|
| `DB_CONNECTION_ERROR` | データベース接続エラー | PostgreSQLサービス確認 |
| `PAYMENT_GATEWAY_ERROR` | 決済ゲートウェイエラー | 外部サービス確認 |
| `EMAIL_SEND_ERROR` | メール送信エラー | SMTP設定確認 |
| `FILE_NOT_FOUND` | ファイルが見つからない | ファイルパス確認 |
| `PERMISSION_DENIED` | 権限エラー | ファイル権限確認 |

## バックアップ・復旧

### バックアップ戦略

#### 1. データベースバックアップ
```bash
#!/bin/bash
# daily_backup.sh

DATE=$(date +%Y%m%d_%H%M%S)
BACKUP_DIR="/backup/database"
DB_NAME="parking_db"
DB_USER="parking_user"

# バックアップ実行
pg_dump -U $DB_USER -d $DB_NAME > $BACKUP_DIR/backup_$DATE.sql

# 古いバックアップ削除（30日以上）
find $BACKUP_DIR -name "backup_*.sql" -mtime +30 -delete

# バックアップ確認
if [ $? -eq 0 ]; then
    echo "Backup completed: backup_$DATE.sql"
else
    echo "Backup failed!"
    exit 1
fi
```

#### 2. 設定ファイルバックアップ
```bash
#!/bin/bash
# config_backup.sh

DATE=$(date +%Y%m%d)
BACKUP_DIR="/backup/config"
CONFIG_DIR="src/main/resources"

tar -czf $BACKUP_DIR/config_$DATE.tar.gz $CONFIG_DIR
```

### 復旧手順

#### 1. データベース復旧
```bash
# バックアップファイルから復旧
psql -U parking_user -d parking_db < backup_20240115.sql

# 特定のテーブルのみ復旧
pg_restore -U parking_user -d parking_db -t payments backup_20240115.sql
```

#### 2. 設定ファイル復旧
```bash
# 設定ファイル復旧
tar -xzf config_20240115.tar.gz

# アプリケーション再起動
./gradlew bootRun
```

## セキュリティ

### セキュリティチェックリスト

#### 1. アクセス制御
- [ ] ファイアウォール設定
- [ ] SSH鍵認証
- [ ] 不要なポート閉鎖
- [ ] 強力なパスワード設定

#### 2. データ保護
- [ ] データベース暗号化
- [ ] 通信暗号化（HTTPS）
- [ ] 機密情報の暗号化
- [ ] アクセスログ記録

#### 3. アプリケーションセキュリティ
- [ ] SQLインジェクション対策
- [ ] XSS対策
- [ ] CSRF対策
- [ ] 入力値検証

### セキュリティ監査

#### 月次セキュリティチェック
```bash
# 1. 脆弱性スキャン
nmap -sV localhost

# 2. ログイン試行確認
grep "Failed login" logs/application.log

# 3. 異常アクセス確認
grep "ERROR" logs/application.log | grep -i security
```

## パフォーマンス監視

### 監視項目

#### 1. システムリソース
```bash
# CPU使用率
top -bn1 | grep "Cpu(s)"

# メモリ使用率
free -h

# ディスク使用率
df -h

# ネットワーク使用率
iftop
```

#### 2. アプリケーションパフォーマンス
```bash
# レスポンス時間確認
curl -w "@curl-format.txt" -o /dev/null -s http://localhost:8080/api/dashboard/overview

# データベースクエリ時間
grep "Query execution time" logs/application.log
```

#### 3. ビジネス指標
- 駐車場使用率
- 決済成功率
- ユーザーアクティビティ
- 売上推移

### アラート設定

#### 1. システムアラート
```bash
# CPU使用率80%以上
if [ $(top -bn1 | grep "Cpu(s)" | awk '{print $2}' | cut -d'%' -f1) -gt 80 ]; then
    echo "High CPU usage detected"
    # アラート送信処理
fi

# メモリ使用率90%以上
if [ $(free | grep Mem | awk '{printf "%.0f", $3/$2 * 100.0}') -gt 90 ]; then
    echo "High memory usage detected"
    # アラート送信処理
fi
```

#### 2. アプリケーションアラート
```bash
# エラー率5%以上
ERROR_RATE=$(grep "ERROR" logs/application.log | wc -l)
TOTAL_REQUESTS=$(grep "Request" logs/application.log | wc -l)
if [ $TOTAL_REQUESTS -gt 0 ] && [ $(echo "scale=2; $ERROR_RATE * 100 / $TOTAL_REQUESTS" | bc) -gt 5 ]; then
    echo "High error rate detected"
    # アラート送信処理
fi
```

## アップデート・メンテナンス

### アップデート手順

#### 1. 事前準備
```bash
# 1. 現在のバックアップ
./backup.sh

# 2. メンテナンスモード有効化
curl -X POST http://localhost:8080/api/admin/maintenance/enable

# 3. ユーザーへの通知
curl -X POST http://localhost:8080/api/notifications/broadcast \
  -H "Content-Type: application/json" \
  -d '{"title":"メンテナンス通知","message":"システムメンテナンスを開始します"}'
```

#### 2. アップデート実行
```bash
# 1. アプリケーション停止
./gradlew bootRun --stop

# 2. 新しいバージョンデプロイ
git pull origin main
./gradlew build

# 3. データベースマイグレーション
./gradlew flywayMigrate

# 4. アプリケーション起動
./gradlew bootRun
```

#### 3. アップデート後確認
```bash
# 1. システム状態確認
curl http://localhost:8080/api/dashboard/health

# 2. 主要機能テスト
curl http://localhost:8080/api/parking/spots

# 3. メンテナンスモード解除
curl -X POST http://localhost:8080/api/admin/maintenance/disable
```

### 定期メンテナンス

#### 月次メンテナンス
- セキュリティパッチ適用
- パフォーマンス最適化
- ログファイル整理
- バックアップテスト

#### 四半期メンテナンス
- システム全体の見直し
- 容量計画の更新
- セキュリティ監査
- ドキュメント更新

---

## 緊急時連絡先

| 役割 | 連絡先 | 対応時間 |
|------|--------|----------|
| システム管理者 | admin@parking-system.com | 24時間 |
| 開発チーム | dev@parking-system.com | 平日9:00-18:00 |
| サポート | support@parking-system.com | 平日9:00-18:00 |

## 変更履歴

| 日付 | バージョン | 変更内容 | 担当者 |
|------|------------|----------|--------|
| 2024-01-15 | 1.0.0 | 初版作成 | システム管理者 |
| 2024-01-20 | 1.1.0 | 決済機能追加 | 開発チーム |
| 2024-01-25 | 1.2.0 | 通知機能追加 | 開発チーム | 