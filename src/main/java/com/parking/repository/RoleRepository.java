package com.parking.repository;

import com.parking.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * ロールリポジトリ
 * ロールエンティティのデータベース操作を担当
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * ロール名でロールを検索
     * @param name ロール名
     * @return ロール（存在しない場合は空）
     */
    Optional<Role> findByName(String name);

    /**
     * ロール名の存在チェック
     * @param name ロール名
     * @return 存在する場合true
     */
    boolean existsByName(String name);
} 