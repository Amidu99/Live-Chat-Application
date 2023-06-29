package lk.ijse.chat_app.dao.custom.impl;

import lk.ijse.chat_app.dao.custom.UserDAO;
import lk.ijse.chat_app.entity.User;
import lk.ijse.chat_app.util.CrudUtil;
import java.io.FileInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAOImpl implements UserDAO {
    @Override
    public boolean add(User user) throws SQLException {
        String sql = "INSERT INTO users ( UserID, UserName, Password, PassHint ) VALUES ( ?, ?, ?, ? )";
        return CrudUtil.execute(sql, user.getUserID(), user.getUserName(), user.getPassword(), user.getPassHint());
    }

    @Override
    public boolean verifyLogin(String username, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE UserName = ? AND Password = ?";
        ResultSet resultSet = CrudUtil.execute(sql, username, password);
        return resultSet.next();
    }

    @Override
    public boolean addImg(String userID, FileInputStream fis) throws SQLException {
        String sql = "INSERT INTO user_dp (UserID, DP) VALUES (?, ?)";
        return CrudUtil.execute(sql, userID, fis);
    }

    @Override
    public String getUserID(String username, String password) throws SQLException {
        String sql = "SELECT UserID FROM users WHERE UserName = ? AND Password = ?";
        ResultSet resultSet = CrudUtil.execute(sql, username, password);
        if(resultSet.next()) {
            return resultSet.getString(1);
        }
        return null;
    }

    @Override
    public ResultSet getDP(String userID) throws SQLException {
        String sql = "SELECT * FROM user_dp WHERE UserID = ?";
        return CrudUtil.execute(sql, userID);
    }

    @Override
    public String generateNextID() throws SQLException {
        String sql = "SELECT UserID FROM users ORDER BY UserID DESC LIMIT 1";
        ResultSet resultSet = CrudUtil.execute(sql);
        if(resultSet.next()) {
            String id = resultSet.getString(1);
            int newId = Integer.parseInt(id.replace("U", "")) + 1;
            return String.format("U%03d", newId);
        }
        return "U001";
    }

    @Override
    public String getPasswordHint(String userID) throws SQLException {
        String sql = "SELECT * FROM users WHERE UserID = ?";
        ResultSet resultSet = CrudUtil.execute(sql, userID);
        if(resultSet.next()) {
            return resultSet.getString(4);
        }
        return null;
    }
}