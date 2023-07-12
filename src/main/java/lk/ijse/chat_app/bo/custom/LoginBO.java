package lk.ijse.chat_app.bo.custom;

import lk.ijse.chat_app.bo.SuperBO;
import lk.ijse.chat_app.dto.UserDTO;
import java.sql.SQLException;

public interface LoginBO extends SuperBO {
    boolean verifyLogin(UserDTO userDTO) throws SQLException;
    String generateNextUserID() throws SQLException, ClassNotFoundException;
    boolean addUser(UserDTO userDTO) throws SQLException, ClassNotFoundException;
    String getPasswordHint(String userID) throws SQLException;
    String getUserID(UserDTO userDTO) throws SQLException;
    boolean isAvailableName(String userName) throws SQLException;
}