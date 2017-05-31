package ecc.cords;

import java.util.List;

public interface Dao {
	
	public <T> void delete(final T t);

	public void evictCollection(String role, Long owner);

	public <T> T get(final long id, final Class<T> type);

	public <T> T get(T t);

	public <T> List<T> getAll(final Class<T> type);

 	public <T> List getAllSimplified(String order, String asc_desc, final Class<T> type);

 	public <T> List getByCriteria(String order, final Class<T> type);

  	public <T> void save(final T t);

	public <T> void saveOrUpdate(final T t);

}