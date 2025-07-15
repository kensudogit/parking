# Doma2 Framework Integration

このプロジェクトには、Doma2フレームワークが統合されています。Doma2は、Javaの軽量なORMフレームワークで、SQLファイルとアノテーションプロセッサーを使用してデータベースアクセスを実装します。

## 設定

### build.gradle
- Doma2プラグインと依存関係が追加されています
- アノテーションプロセッサーが設定されています

### 設定ファイル
- `src/main/resources/doma2-config.properties`: Doma2の基本設定
- `src/main/java/com/parking/config/DomaConfig.java`: Spring Boot統合用の設定クラス

## ファイル構造

```
src/main/java/com/parking/
├── entity/
│   └── ParkingSpotDoma.java          # Doma2エンティティ
├── dao/
│   └── ParkingSpotDao.java           # DAOインターフェース
├── service/
│   └── ParkingSpotDomaService.java   # ビジネスロジック
├── controller/
│   └── ParkingSpotDomaController.java # REST API
└── config/
    └── DomaConfig.java               # Doma2設定

src/main/resources/
├── META-INF/sql/
│   └── ParkingSpotDao/               # SQLファイル
│       ├── selectAvailableSpots.sql
│       └── deleteById.sql
└── doma2-config.properties          # Doma2設定ファイル
```

## 使用方法

### 1. エンティティの定義
```java
@Entity(immutable = true)
@Table(name = "parking_spots")
public class ParkingSpotDoma {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;
    
    @Column(name = "spot_number")
    private final String spotNumber;
    
    // 他のフィールド...
}
```

### 2. DAOインターフェースの定義
```java
@Dao
public interface ParkingSpotDao {
    @Select
    List<ParkingSpotDoma> selectAll();
    
    @Select
    ParkingSpotDoma selectById(Long id);
    
    @Insert
    int insert(ParkingSpotDoma parkingSpot);
    
    @Update
    int update(ParkingSpotDoma parkingSpot);
    
    @Delete
    int delete(ParkingSpotDoma parkingSpot);
}
```

### 3. SQLファイルの作成
複雑なクエリはSQLファイルとして作成します：

```sql
-- src/main/resources/META-INF/sql/ParkingSpotDao/selectAvailableSpots.sql
SELECT
    id,
    spot_number,
    spot_type,
    status,
    floor_level,
    hourly_rate,
    created_at,
    updated_at
FROM
    parking_spots
WHERE
    status = 'AVAILABLE'
ORDER BY
    spot_number
```

### 4. サービスクラスでの使用
```java
@Service
@Transactional
public class ParkingSpotDomaService {
    @Autowired
    private ParkingSpotDao parkingSpotDao;
    
    public List<ParkingSpotDoma> getAvailableSpots() {
        return parkingSpotDao.selectAvailableSpots();
    }
}
```

## API エンドポイント

Doma2を使用したAPIエンドポイント：

- `GET /api/doma/parking-spots` - 全駐車スペース取得
- `GET /api/doma/parking-spots/{id}` - 特定の駐車スペース取得
- `GET /api/doma/parking-spots/available` - 利用可能な駐車スペース取得
- `GET /api/doma/parking-spots/status/{status}` - ステータス別駐車スペース取得
- `GET /api/doma/parking-spots/type/{spotType}` - タイプ別駐車スペース取得
- `POST /api/doma/parking-spots` - 駐車スペース作成
- `PUT /api/doma/parking-spots/{id}/status` - 駐車スペースステータス更新
- `DELETE /api/doma/parking-spots/{id}` - 駐車スペース削除

## ビルドと実行

```bash
# プロジェクトのビルド
./gradlew clean build

# アプリケーションの実行
./gradlew bootRun
```

## 注意事項

1. Doma2エンティティは不変（immutable）である必要があります
2. SQLファイルは`META-INF/sql/{DaoClassName}/`ディレクトリに配置する必要があります
3. アノテーションプロセッサーが自動的にDAOの実装クラスを生成します
4. トランザクション管理はSpring Bootの`@Transactional`アノテーションを使用します

## 既存のJPAとの併用

このプロジェクトでは、既存のJPAエンティティとDoma2エンティティを併用できます：
- JPA: `ParkingSpot.java`, `ParkingSession.java`
- Doma2: `ParkingSpotDoma.java`

それぞれ異なるAPIエンドポイントでアクセスできます：
- JPA: `/api/parking-spots/*`
- Doma2: `/api/doma/parking-spots/*` 