package tedu.sheng.dal;

import java.util.List;

public interface IDao<T> {

	List<T> getData();
}
