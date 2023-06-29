package lk.ijse.chat_app.dao;

import java.sql.SQLException;

public interface CrudDAO<T> extends SuperDAO{
    boolean add(T entity) throws SQLException, ClassNotFoundException;
    String generateNextID() throws SQLException, ClassNotFoundException;
}