package lk.ijse.chat_app.dao.custom;

import lk.ijse.chat_app.dao.CrudDAO;
import lk.ijse.chat_app.entity.User;
import java.io.FileInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface UserDAO extends CrudDAO<User> {
    String getPasswordHint(String userID) throws SQLException;
    boolean verifyLogin(String username, String password) throws SQLException;
    boolean addImg(String userID, FileInputStream fis) throws SQLException;
    String getUserID(String username, String password) throws SQLException;
    ResultSet getDP(String userID) throws SQLException;
}