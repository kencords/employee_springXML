package ecc.cords;

import java.util.Arrays;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.HibernateException;
import org.hibernate.stat.Statistics;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class HibernateDao implements Dao {

	private SessionFactory sessionFactory;
	private Statistics stats;

	public void setSessionFactory(SessionFactory sessionFactory) {
    	this.sessionFactory = sessionFactory;
    	this.stats = sessionFactory.getStatistics();
    }

  	public <T> void delete(final T t) {
	   	sessionFactory.getCurrentSession().delete(t);
 	}

 	public void evictCollection(String role, Long owner) {
 		 sessionFactory.evictCollection(role, owner);
 	}

	public <T> T get(final long id, final Class<T> type) {
		T t = (T) sessionFactory.getCurrentSession().get(type, id);
		showStatistics("from get(id,type)",stats);
		return t;
	}

	public <T> T get(T t) {
		List<T> list = sessionFactory.getCurrentSession().createCriteria(t.getClass()).setCacheable(true).list();
		showStatistics("from get(t)",stats);
		return (T) list.get(list.indexOf((T)t));
	}

	public <T> List<T> getAll(final Class<T> type) {
	   	List<T> list = sessionFactory.getCurrentSession().createCriteria(type).setCacheable(true).list();
	   	showStatistics("from getAll(type)",stats);
	   	return list;
 	}

 	public <T> List getAllSimplified(String order, String asc_desc, String query, final Class<T> type) {
 		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(type);
 		criteria.setCacheable(true);
 		if(query!=null && !query.trim().equals("")) {
 			query = "%" + query + "%";
 			criteria.add(Restrictions.disjunction()
                .add(Restrictions.like("name.lastName", query).ignoreCase())
                .add(Restrictions.like("name.firstName", query).ignoreCase())
                .add(Restrictions.like("name.middleName", query).ignoreCase())
            );
 		}
 		criteria.setProjection(Projections.projectionList()
 										  .add(Projections.property("empId"))
 										  .add(Projections.property("name"))
 										  .add(Projections.property("hireDate"))
 										  .add(Projections.property("gwa"))
 		);
 		criteria.addOrder(asc_desc.equals("asc")? Property.forName(order).asc() : Property.forName(order).desc());
 		List list = criteria.list();
 		showStatistics("from getAllSimplified(type)",stats);
 		return list;
 	}

 	public <T> List getByCriteria(String order, final Class<T> type) {
  		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(type)
  						   .addOrder(Property.forName(order).asc());	  
		criteria.setCacheable(true);  						   
		List list = criteria.list();
		showStatistics("from getByCriteria(order,type)",stats);
  		return list;	
  	}

  	public <T> void save(final T t) {
	   	sessionFactory.getCurrentSession().save(t);
  	}

	public <T> void saveOrUpdate(final T t) {
	   	sessionFactory.getCurrentSession().saveOrUpdate(t);
	}

	private void showStatistics(String from, Statistics stats) {
		System.out.println("***********************************");
		//System.out.println(HibernateUtil.getSessionFactory().getStatistics());
		System.out.println(from);
        System.out.println("Entity fetch count :" + stats.getEntityFetchCount());
        System.out.println("Second level cache hit count : "+ stats.getSecondLevelCacheHitCount());
        System.out.println("Second level cache put count : " + stats.getSecondLevelCachePutCount());
        System.out.println("Second level cache miss count : " + stats.getSecondLevelCacheMissCount());
        System.out.println("***********************************");
	}
}