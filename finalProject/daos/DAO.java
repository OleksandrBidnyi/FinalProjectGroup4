package finalProject.daos;

import java.util.List;

/**
 * Created by Aleksandr on 26.04.2017.
 */
/**
 * DAO - interface with methods for accessing the optional work with the Hotel, Room and User .
 */
public interface DAO<T> {
    boolean add(T t);
    boolean edit (T t);
    boolean remove (T t);
    List<T> getBase();

}
