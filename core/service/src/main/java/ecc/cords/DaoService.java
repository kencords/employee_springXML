package ecc.cords;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class DaoService{

	private Dao dao;
	private final Logger logger = LoggerFactory.getLogger(DaoService.class);

	public void setDao(Dao dao) {
		this.dao = dao;
	}

	public <E> void deleteElement(E e) {
		logger.info("Deleting record " + e);
		dao.delete(e);
	}

	public void evictCollection(String role, Long owner) {
		dao.evictCollection(role, owner);
	}

	public <E> List<E> getAllElements(final Class<E> type) {
		logger.info("Getting all records of type " + type);
		return dao.getAll(type);
	}

	public <E> E getElement(E e) { 
		logger.info("Getting record " + e);
      	return dao.get(e);
    }

    public <E> E getElement(final long id, final Class<E> type) {
    	logger.info("Getting records of type " + type + " with ID of " + id);
      	return dao.get(id,type);
    }

	public List getOrderedEmployees(String order) {
		logger.info("Getting Employee List ordered by " + order);
		return dao.getByCriteria(order, Employee.class);
	}

	public List getSimplifiedEmployees(String order, String asc_desc) {
		logger.info("Getting Employee List ordered by " + order + "," + asc_desc);
		return dao.getAllSimplified(order, asc_desc, Employee.class);
	}
    
	public <E> void saveElement(E e) {
		logger.info("Saving record " + e);
		dao.save(e);
	}

	public <E> void updateElement(E e) {
		logger.info("Updating record " + e);
		dao.saveOrUpdate(e);
	}
}