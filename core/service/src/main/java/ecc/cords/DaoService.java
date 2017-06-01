package ecc.cords;

import java.util.List;

public class DaoService{

	private Dao dao;

	public void setDao(Dao dao) {
		this.dao = dao;
	}

	public <E> void deleteElement(E e) {
		dao.delete(e);
	}

	public void evictCollection(String role, Long owner) {
		dao.evictCollection(role, owner);
	}

	public <E> List<E> getAllElements(final Class<E> type) {
		return dao.getAll(type);
	}

	public <E> E getElement(E e) { 
      	return dao.get(e);
    }

    public <E> E getElement(final long id, final Class<E> type) {
      	return dao.get(id,type);
    }

	public List getOrderedEmployees(String order) {
		return dao.getByCriteria(order, Employee.class);
	}

	public List getSimplifiedEmployees(String order, String asc_desc, String query) {
		return dao.getAllSimplified(order, asc_desc, query, Employee.class);
	}
    
	public <E> void saveElement(E e) {
		dao.save(e);
	}

	public <E> void updateElement(E e) {
		dao.saveOrUpdate(e);
	}
}