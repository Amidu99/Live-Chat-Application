package lk.ijse.chat_app.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class UserDTO {
    private String UserID;
    private String UserName;
    private String Password;
    private String PassHint;
}