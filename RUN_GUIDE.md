# アプリケーション実行ガイド

このガイドでは、IDEで▷マークをクリックしてアプリケーションを実行する方法を説明します。

## VS Code での実行方法

### 1. 必要な拡張機能
- Extension Pack for Java
- Spring Boot Extension Pack
- Gradle for Java

### 2. 実行手順
1. **VS Codeでプロジェクトを開く**
   ```
   code devlop/parking
   ```

2. **Java拡張機能がプロジェクトを認識するまで待つ**
   - 右下に「Java Projects」が表示されるまで待機

3. **▷マークで実行**
   - `src/main/java/com/parking/ParkingApplication.java` を開く
   - `main`メソッドの横に▷マークが表示される
   - ▷マークをクリックして「Run Java」を選択

4. **デバッグ実行**
   - ▷マークの横にある虫マークをクリック
   - または、F5キーを押す

### 3. 実行設定の選択
- **Launch ParkingApplication**: 通常の実行
- **Debug ParkingApplication**: デバッグモードで実行
- **Run Tests**: テストの実行

## IntelliJ IDEA での実行方法

### 1. プロジェクトを開く
1. IntelliJ IDEAを起動
2. 「Open」をクリック
3. `devlop/parking`フォルダを選択

### 2. 実行手順
1. **プロジェクトが読み込まれるまで待つ**
   - Gradleの同期が完了するまで待機

2. **▷マークで実行**
   - `src/main/java/com/parking/ParkingApplication.java` を開く
   - `main`メソッドの横に▷マークが表示される
   - ▷マークをクリック

3. **実行設定の選択**
   - **ParkingApplication**: 通常の実行
   - **ParkingApplication (Debug)**: デバッグモードで実行

## 実行可能なメソッド

### メインクラス内の実行メソッド
```java
// 通常の起動
ParkingApplication.run();

// 開発環境用の起動
ParkingApplication.runDev();

// デバッグモードで起動
ParkingApplication.runDebug();
```

### テストの実行
```java
// 個別のテストメソッド
@Test
void testApplicationStartup() { ... }

@Test
void testDomaConfiguration() { ... }
```

## トラブルシューティング

### ▷マークが表示されない場合
1. **Java拡張機能がインストールされているか確認**
2. **プロジェクトの再読み込み**
   - VS Code: `Ctrl+Shift+P` → "Java: Reload Projects"
   - IntelliJ: File → Invalidate Caches and Restart

3. **Gradleの同期**
   ```bash
   ./gradlew clean build
   ```

### 実行時にエラーが発生する場合
1. **ログを確認**
   - コンソールに表示されるエラーメッセージを確認

2. **データベース接続の確認**
   - PostgreSQLが起動しているか確認
   - データベース接続設定を確認

3. **ポートの確認**
   - 8080番ポートが使用されていないか確認

## 環境変数の設定

### 開発環境
```bash
export SPRING_PROFILES_ACTIVE=dev
```

### Windows
```cmd
set SPRING_PROFILES_ACTIVE=dev
```

## アクセスURL

アプリケーションが正常に起動したら、以下のURLでアクセスできます：

- **メインアプリケーション**: http://localhost:8080
- **Doma2 API**: http://localhost:8080/api/doma/parking-spots
- **ヘルスチェック**: http://localhost:8080/actuator/health

## ログの確認

### アプリケーションログ
- コンソールに表示されるログを確認
- ログレベル: DEBUG（開発環境）

### データベースログ
- SQLクエリがコンソールに表示される
- Doma2のログも表示される 