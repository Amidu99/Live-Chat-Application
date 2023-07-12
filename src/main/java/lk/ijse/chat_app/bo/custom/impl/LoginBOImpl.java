package lk.ijse.chat_app.bo.custom.impl;

import lk.ijse.chat_app.bo.custom.LoginBO;
import lk.ijse.chat_app.dao.DAOFactory;
import lk.ijse.chat_app.dao.custom.UserDAO;
import lk.ijse.chat_app.dto.UserDTO;
import lk.ijse.chat_app.entity.User;
import java.sql.SQLException;

public class LoginBOImpl implements LoginBO {
    UserDAO userDAO = (UserDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.USER);

    @Override
    public boolean verifyLogin(UserDTO dto) throws SQLException {
        return userDAO.verifyLogin(new User(dto.getUserName(), dto.getPassword()));
    }

    @Override
    public String generateNextUserID() throws SQLException, ClassNotFoundException {
        return userDAO.generateNextID();
    }

    @Override
    public boolean addUser(UserDTO dto) throws SQLException, ClassNotFoundException{
        return userDAO.add(new User(dto.getUserID(), dto.getUserName(), dto.getPassword(), dto.getPassHint(), dto.getUserDP()));
    }

    @Override
    public String getPasswordHint(String userID) throws SQLException {
        return userDAO.getPasswordHint(userID);
    }

    @Override
    public String getUserID(UserDTO dto) throws SQLException {
        return userDAO.getUserID(new User(dto.getUserName(), dto.getPassword()));
    }

    @Override
    public boolean isAvailableName(String userName) throws SQLException {
        return userDAO.isAvailableName(userName);
    }
}