package lk.ijse.chat_app.bo.custom.impl;

import lk.ijse.chat_app.bo.custom.LoginBO;
import lk.ijse.chat_app.dao.DAOFactory;
import lk.ijse.chat_app.dao.custom.UserDAO;
import lk.ijse.chat_app.dto.UserDTO;
import lk.ijse.chat_app.entity.User;
import java.io.FileInputStream;
import java.sql.SQLException;

public class LoginBOImpl implements LoginBO {
    UserDAO userDAO = (UserDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.USER);

    @Override
    public boolean verifyLogin(String username, String password) throws SQLException {
        return userDAO.verifyLogin(username, password);
    }

    @Override
    public String generateNextUserID() throws SQLException, ClassNotFoundException {
        return userDAO.generateNextID();
    }

    @Override
    public boolean addUser(UserDTO dto) throws SQLException, ClassNotFoundException{
        return userDAO.add(new User(dto.getUserID(), dto.getUserName(), dto.getPassword(), dto.getPassHint()));
    }

    @Override
    public boolean addImage(String userID, FileInputStream fis) throws SQLException {
        return userDAO.addImg(userID, fis);
    }

    @Override
    public String getPasswordHint(String userID) throws SQLException {
        return userDAO.getPasswordHint(userID);
    }

    @Override
    public String getUserID(String username, String password) throws SQLException {
        return userDAO.getUserID(username, password);
    }
}