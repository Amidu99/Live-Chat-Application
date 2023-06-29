package lk.ijse.chat_app.bo.custom.impl;

import lk.ijse.chat_app.bo.custom.ClientBO;
import lk.ijse.chat_app.dao.DAOFactory;
import lk.ijse.chat_app.dao.custom.UserDAO;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientBOImpl implements ClientBO {
    UserDAO userDAO = (UserDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.USER);

    @Override
    public ResultSet getImage(String userID) throws SQLException {
        return userDAO.getDP(userID);
    }
}