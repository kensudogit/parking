package com.parking.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 登録リクエストDTO
 * ユーザー登録時のリクエストデータを格納
 */
public class RegisterRequest {

    @NotBlank(message = "ユーザー名は必須です")
    @Size(min = 3, max = 50, message = "ユーザー名は3文字以上50文字以下で入力してください")
    private String username;

    @NotBlank(message = "メールアドレスは必須です")
    @Email(message = "有効なメールアドレスを入力してください")
    private String email;

    @NotBlank(message = "パスワードは必須です")
    @Size(min = 6, max = 100, message = "パスワードは6文字以上100文字以下で入力してください")
    private String password;

    @NotBlank(message = "姓は必須です")
    private String firstName;

    @NotBlank(message = "名は必須です")
    private String lastName;

    private String phoneNumber;

    /**
     * デフォルトコンストラクタ
     */
    public RegisterRequest() {}

    /**
     * コンストラクタ
     * @param username ユーザー名
     * @param email メールアドレス
     * @param password パスワード
     * @param firstName 姓
     * @param lastName 名
     * @param phoneNumber 電話番号
     */
    public RegisterRequest(String username, String email, String password, 
                         String firstName, String lastName, String phoneNumber) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }

    // Getter and Setter methods
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
} 