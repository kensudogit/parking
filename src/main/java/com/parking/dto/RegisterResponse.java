package com.parking.dto;

/**
 * 登録レスポンスDTO
 * ユーザー登録時のレスポンスデータを格納
 */
public class RegisterResponse {

    private Long userId;
    private String username;
    private String email;
    private String token;
    private String message;

    /**
     * デフォルトコンストラクタ
     */
    public RegisterResponse() {}

    /**
     * コンストラクタ
     * @param userId ユーザーID
     * @param username ユーザー名
     * @param email メールアドレス
     * @param token JWTトークン
     * @param message メッセージ
     */
    public RegisterResponse(Long userId, String username, String email, String token, String message) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.token = token;
        this.message = message;
    }

    // Getter and Setter methods
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
} 