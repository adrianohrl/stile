package tech.adrianohrl.stile.control.dao.personnel;

import tech.adrianohrl.stile.model.personnel.Employee;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import tech.adrianohrl.dao.ArchivableDAO;

/**
 *
 * @author Adriano Henrique Rossette Leite (contact@adrianohrl.tech)
 * @param <E>
 */
public class EmployeeDAO<E extends Employee> extends ArchivableDAO<E, String> {

    public EmployeeDAO(EntityManager em) {
        super(em, Employee.class);
    }
    
    protected EmployeeDAO(EntityManager em, Class clazz) {
        super(em, clazz);
    }

    @Override
    public E find(String code) {
        E employee = super.find(code);
        if (employee == null) {
            try {
                employee = (E) em.createQuery("SELECT e "
                    + "FROM Employee e "
                    + "WHERE e.code = '" + code + "'").getSingleResult();
            } catch (NoResultException e) {
                return null;
            }
        }
        return employee;
    }

    @Override
    public boolean isRegistered(E entity) {
        return super.find(entity.getName()) != null;
    }
    
}
