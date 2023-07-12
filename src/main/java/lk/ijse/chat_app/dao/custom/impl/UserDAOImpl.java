package lk.ijse.chat_app.dao.custom.impl;

import lk.ijse.chat_app.dao.custom.UserDAO;
import lk.ijse.chat_app.entity.User;
import lk.ijse.chat_app.util.CrudUtil;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAOImpl implements UserDAO {

    @Override
    public boolean isAvailableName(String userName) throws SQLException {
        String sql = "SELECT UserName FROM users WHERE UserName = ?";
        ResultSet resultSet = CrudUtil.execute(sql, userName);
        return resultSet.next();
    }

    @Override
    public boolean add(User user) throws SQLException {
        String sql = "INSERT INTO users ( UserID, UserName, Password, PassHint, UserDP ) VALUES ( ?, ?, ?, ?, ? )";
        return CrudUtil.execute(sql, user.getUserID(), user.getUserName(), user.getPassword(), user.getPassHint(), user.getUserDP());
    }

    @Override
    public boolean verifyLogin(User user) throws SQLException {
        String sql = "SELECT * FROM users WHERE UserName = ? AND Password = ?";
        ResultSet resultSet = CrudUtil.execute(sql, user.getUserName(), user.getPassword());
        return resultSet.next();
    }

    @Override
    public String getUserID(User user) throws SQLException {
        String sql = "SELECT UserID FROM users WHERE UserName = ? AND Password = ?";
        ResultSet resultSet = CrudUtil.execute(sql, user.getUserName(), user.getPassword());
        if(resultSet.next()) {
            return resultSet.getString(1);
        }
        return null;
    }

    @Override
    public InputStream getUserDP(String userID) throws SQLException {
        String sql = "SELECT UserDP FROM users WHERE UserID = ? ";
        ResultSet resultSet = CrudUtil.execute(sql, userID);
        if(resultSet.next()) {
            return resultSet.getBinaryStream(1);
        }
        return null;
    }

    @Override
    public boolean updateUserDP(FileInputStream fis, String userID) throws SQLException {
        String sql = "UPDATE users SET UserDP = ? WHERE UserID = ? ";
        return CrudUtil.execute(sql, fis, userID);
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
        String sql = "SELECT PassHint FROM users WHERE UserID = ?";
        ResultSet resultSet = CrudUtil.execute(sql, userID);
        if(resultSet.next()) {
            return resultSet.getString(1);
        }
        return null;
    }
}