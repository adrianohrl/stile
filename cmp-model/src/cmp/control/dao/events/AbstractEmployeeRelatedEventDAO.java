/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cmp.control.dao.events;

import cmp.exceptions.DAOException;
import cmp.model.events.AbstractEmployeeRelatedEvent;
import cmp.model.personnel.Employee;
import cmp.control.model.production.reports.filters.EmployeeRelatedEventsList;
import java.util.Calendar;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;

/**
 *
 * @author adrianohrl
 * @param <E>
 */
public class AbstractEmployeeRelatedEventDAO<E extends AbstractEmployeeRelatedEvent> extends AbstractEventDAO<E> {
    
    public AbstractEmployeeRelatedEventDAO(EntityManager em) {
        super(em, AbstractEmployeeRelatedEvent.class);
    }

    protected AbstractEmployeeRelatedEventDAO(EntityManager em, Class clazz) {
        super(em, clazz);
    }
    
    public EmployeeRelatedEventsList findEmployeeEvents(Employee employee) {
        return new EmployeeRelatedEventsList(em.createQuery("SELECT event "
                + "FROM " + clazz.getSimpleName() + " event JOIN event.employee emp "
                + "WHERE emp.name = '" + employee.getName() + "' "
                + "ORDER BY event.eventDate ASC").getResultList());
    }
    
    public EmployeeRelatedEventsList findEmployeeEvents(Employee employee, Calendar start, Calendar end) throws DAOException {
        if (start.after(end)) {
            throw new DAOException("The start period calendar must be before the end period calendar!!!");
        }
        Query query = em.createQuery("SELECT event "
                + "FROM " + clazz.getSimpleName() + " event JOIN event.employee emp "
                + "WHERE emp.name = '" + employee.getName() + "' "
                + "AND event.eventDate BETWEEN :start AND :end "
                + "ORDER BY event.eventDate ASC");
        query.setParameter("start", start, TemporalType.TIMESTAMP);
        query.setParameter("end", end, TemporalType.TIMESTAMP);
        return new EmployeeRelatedEventsList(query.getResultList());
    }
    
}