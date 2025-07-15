package com.parking.dto;

import javax.validation.constraints.NotBlank;

/**
 * ログインリクエストDTO
 * ユーザーログイン時のリクエストデータを格納
 */
public class LoginRequest {

    @NotBlank(message = "ユーザー名は必須です")
    private String username;

    @NotBlank(message = "パスワードは必須です")
    private String password;

    /**
     * デフォルトコンストラクタ
     */
    public LoginRequest() {}

    /**
     * コンストラクタ
     * @param username ユーザー名
     * @param password パスワード
     */
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getter and Setter methods
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
} 