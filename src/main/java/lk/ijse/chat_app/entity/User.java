package lk.ijse.chat_app.entity;

import lombok.*;
import java.io.FileInputStream;

@Data
@AllArgsConstructor
public class User {
    private String UserID;
    private String UserName;
    private String Password;
    private String PassHint;
    private FileInputStream UserDP;

    public User(String userName, String password) {
        this.UserName = userName;
        this.Password = password;
    }
}