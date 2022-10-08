/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IService;


import java.sql.SQLException;
import java.util.List;
import entity.user.User;

/**
 *
 * @author Skander 
 * @param <T>
 */
public interface IService<T> {
    void ajouter(T t) throws SQLException;
    boolean delete(T t) throws SQLException;
    boolean update(T t) throws SQLException;
    List<T> readAll() throws SQLException;
    User rechercher(String email) throws SQLException;
    public boolean login(User u) throws SQLException;
    public boolean resetPass(User u);
}

