/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.projetService;

import entity.activite.Objectif;
import java.util.List;


/**
 *
 * @param <T>
 */
public interface IService<T> {

    /**
     *  insert object t with container id
     * @param t
     * @param id
     */
    public void insert(T t,int id);

    /**
     *  insert object t 
     * @param t
     */
    public void insert(T t);

    /**
     *  insert object t and return id generated
     * @param t
     * @return
     */
    public int insertIdReturn(T t);

    /**
     *  delete by id
     * @param id
     */
    public void delete(int id);

    /**
     *  delete all children container by container id
     * @param id
     */
    public void deleteByContainer(int id);

    /**
     *  update object
     * @param t
     */
    public void update(T t);

    /**
     *  display by id
     * @param id
     * @return
     */
    public T display(int id);

    /**
     *  display all children container  by container id
     * @param id
     * @return - List
     */
    public List<T> displayAll(int id);

    /**
     *  display all objects
     * @return - List
     */
    public List<T> displayAll();

    /**
     *  check if object existe by id
     * @param id
     * @return - boolean
     */
    public boolean existe(int id);
    
}
