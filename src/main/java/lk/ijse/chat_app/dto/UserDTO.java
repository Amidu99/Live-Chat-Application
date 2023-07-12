package lk.ijse.chat_app.dto;

import lombok.*;
import java.io.FileInputStream;

@Data
@AllArgsConstructor
public class UserDTO {
    private String UserID;
    private String UserName;
    private String Password;
    private String PassHint;
    private FileInputStream UserDP;

    public UserDTO(String UserName, String Password) {
        this.UserName = UserName;
        this.Password = Password;
    }
}