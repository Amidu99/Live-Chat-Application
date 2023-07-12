package lk.ijse.chat_app.bo.custom.impl;

import lk.ijse.chat_app.bo.custom.ClientBO;
import lk.ijse.chat_app.dao.DAOFactory;
import lk.ijse.chat_app.dao.custom.UserDAO;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.SQLException;

public class ClientBOImpl implements ClientBO {
    UserDAO userDAO = (UserDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.USER);

    @Override
    public InputStream getUserDP(String userID) throws SQLException {
        return userDAO.getUserDP(userID);
    }

    @Override
    public boolean updateUserDP(FileInputStream fis, String userID) throws SQLException {
        return userDAO.updateUserDP(fis, userID);
    }
}