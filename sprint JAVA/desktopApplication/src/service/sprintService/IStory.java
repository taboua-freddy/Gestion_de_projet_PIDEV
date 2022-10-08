package service.sprintService;

import entity.Story;

import java.sql.SQLException;
import java.util.List;

public interface IStory<T> {
    void insert(T t) throws SQLException;


    List<T> displayAll();
    void delete(int id) throws SQLException;
    void update(T t) throws SQLException;
}
