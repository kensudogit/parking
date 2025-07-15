package com.parking.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * パスワード変更リクエストDTO
 * パスワード変更時のリクエストデータを格納
 */
public class PasswordChangeRequest {

    @NotNull(message = "ユーザーIDは必須です")
    private Long userId;

    @NotBlank(message = "現在のパスワードは必須です")
    private String currentPassword;

    @NotBlank(message = "新しいパスワードは必須です")
    @Size(min = 6, max = 100, message = "新しいパスワードは6文字以上100文字以下で入力してください")
    private String newPassword;

    @NotBlank(message = "新しいパスワードの確認は必須です")
    private String confirmPassword;

    /**
     * デフォルトコンストラクタ
     */
    public PasswordChangeRequest() {}

    /**
     * コンストラクタ
     * @param userId ユーザーID
     * @param currentPassword 現在のパスワード
     * @param newPassword 新しいパスワード
     * @param confirmPassword 新しいパスワードの確認
     */
    public PasswordChangeRequest(Long userId, String currentPassword, String newPassword, String confirmPassword) {
        this.userId = userId;
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

    // Getter and Setter methods
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
} 