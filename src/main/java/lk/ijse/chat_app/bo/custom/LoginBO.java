package lk.ijse.chat_app.bo.custom;

import lk.ijse.chat_app.bo.SuperBO;
import lk.ijse.chat_app.dto.UserDTO;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;

public interface LoginBO extends SuperBO {
    boolean verifyLogin(String username, String password) throws SQLException;
    String generateNextUserID() throws SQLException, ClassNotFoundException;
    boolean addUser(UserDTO userDTO) throws SQLException, ClassNotFoundException;
    boolean addImage(String userID, FileInputStream fis) throws SQLException, IOException, ClassNotFoundException;
    String getPasswordHint(String userID) throws SQLException;
    String getUserID(String username, String password) throws SQLException;
}