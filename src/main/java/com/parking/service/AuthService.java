package com.parking.service;

import com.parking.entity.User;
import com.parking.entity.Role;
import com.parking.repository.UserRepository;
import com.parking.repository.RoleRepository;
import com.parking.dto.LoginRequest;
import com.parking.dto.LoginResponse;
import com.parking.dto.RegisterRequest;
import com.parking.dto.RegisterResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * 認証サービス
 * ユーザー登録、ログイン、JWTトークン管理を担当
 */
@Service
@Transactional
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    /**
     * ユーザー登録
     * @param request 登録リクエスト
     * @return 登録レスポンス
     */
    public RegisterResponse register(RegisterRequest request) {
        // ユーザー名の重複チェック
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("ユーザー名が既に使用されています");
        }

        // メールアドレスの重複チェック
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("メールアドレスが既に使用されています");
        }

        // デフォルトロール（USER）を取得
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("デフォルトロールが見つかりません"));

        // ユーザーを作成
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setEnabled(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        // ロールを設定
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        // ユーザーを保存
        User savedUser = userRepository.save(user);

        // JWTトークンを生成
        String token = jwtService.generateToken(savedUser);

        return new RegisterResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                token,
                "ユーザー登録が完了しました"
        );
    }

    /**
     * ユーザーログイン
     * @param request ログインリクエスト
     * @return ログインレスポンス
     */
    public LoginResponse login(LoginRequest request) {
        // ユーザーを検索
        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());
        if (userOpt.isEmpty()) {
            throw new RuntimeException("ユーザー名またはパスワードが正しくありません");
        }

        User user = userOpt.get();

        // パスワードを検証
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("ユーザー名またはパスワードが正しくありません");
        }

        // アカウントが有効かチェック
        if (!user.isEnabled()) {
            throw new RuntimeException("アカウントが無効です");
        }

        // 最終ログイン時刻を更新
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        // JWTトークンを生成
        String token = jwtService.generateToken(user);

        return new LoginResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                token,
                "ログインに成功しました"
        );
    }

    /**
     * ユーザー名でユーザーを検索
     * @param username ユーザー名
     * @return ユーザー（存在しない場合は空）
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * ユーザーIDでユーザーを検索
     * @param userId ユーザーID
     * @return ユーザー（存在しない場合は空）
     */
    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    /**
     * ユーザー情報を更新
     * @param user 更新するユーザー
     * @return 更新されたユーザー
     */
    public User updateUser(User user) {
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    /**
     * パスワードを変更
     * @param userId ユーザーID
     * @param newPassword 新しいパスワード
     */
    public void changePassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }
} 