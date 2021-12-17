package Repository;

import Exceptions.InputException;
import Exceptions.NullException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface ICrudRepository<T> {
    /**
     * finds the object with id 'id'
     *
     * @param id id of searched object
     * @return object with id or null if it doesn't exist
     */
    T findOne(Integer id) throws NullException, SQLException, InputException;


    /**
     * returns all stored objects
     *
     * @return all stored objects
     */
    List<T> findAll() throws SQLException, InputException;


    /**
     * saves(adds) an object to repository
     *
     * @param obj obj different from null
     * @return null if object is saved otherwise obje if id exists
     */
    T save(T obj) throws NullException, IOException, SQLException, InputException;


    /**
     * modifies an object
     *
     * @param obj obj different from null
     * @return null - null if object is updated otherwise obj if id doesn't exist
     */
    T update(T obj) throws NullException, IOException, SQLException, InputException;


    /**
     * removes object with id 'id'
     *
     * @param id id must be not null
     * @return obj or null obj id doesn't exist
     */
    T delete(Integer id) throws NullException, IOException, SQLException, InputException;

}
