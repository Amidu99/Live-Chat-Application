package lk.ijse.chat_app.entity;

import lombok.*;

@Data
@AllArgsConstructor
public class User {
    private String UserID;
    private String UserName;
    private String Password;
    private String PassHint;
}