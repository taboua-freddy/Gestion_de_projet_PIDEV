/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.tache;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author maysa
 */
public interface Tservice<T> {
   void insert(T t);
   void insertPST(T t);
    List<T> displayAll();
    void delete(int t);
    void update(T t);
}
