package lk.ijse.chat_app.bo.custom;

import lk.ijse.chat_app.bo.SuperBO;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.SQLException;

public interface ClientBO extends SuperBO {
    InputStream getUserDP(String userID) throws SQLException;
    boolean updateUserDP(FileInputStream fis, String userID) throws SQLException;
}