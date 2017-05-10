package finalProject;

import java.util.List;

/**
 * Created by Aleksandr on 26.04.2017.
 */
public interface DAO<T> {
    boolean add(T t);
    boolean edit (T t);
    boolean remove (T t);
    List<T> getBase();

}
