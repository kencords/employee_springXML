package ecc.cords;

import java.util.List;

public interface DaoService{

	public void setDao(Dao dao);

	public <E> void deleteElement(E e);

	public void evictCollection(String role, Long owner);

	public <E> List<E> getAllElements(final Class<E> type);

	public <E> E getElement(E e);

    public <E> E getElement(final long id, final Class<E> type);

	public List getOrderedEmployees(String order);

	public List getSimplifiedEmployees(String order, String asc_desc, String query);
    
	public <E> void saveElement(E e);

	public <E> void updateElement(E e);
}