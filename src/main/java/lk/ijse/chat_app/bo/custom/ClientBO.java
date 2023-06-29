package lk.ijse.chat_app.bo.custom;

import lk.ijse.chat_app.bo.SuperBO;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface ClientBO extends SuperBO {
    ResultSet getImage(String userID) throws SQLException;
}