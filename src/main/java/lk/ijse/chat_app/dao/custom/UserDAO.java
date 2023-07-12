package lk.ijse.chat_app.dao.custom;

import lk.ijse.chat_app.dao.CrudDAO;
import lk.ijse.chat_app.entity.User;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.SQLException;

public interface UserDAO extends CrudDAO<User> {
    boolean verifyLogin(User user) throws SQLException;
    String getUserID(User user) throws SQLException;
    String getPasswordHint(String userID) throws SQLException;
    InputStream getUserDP(String userID) throws SQLException;
    boolean updateUserDP(FileInputStream fis, String userID) throws SQLException;
    boolean isAvailableName(String userName) throws SQLException;
}