/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ceciliaprado.cmp.control.dao.personnel.io;

import br.com.ceciliaprado.cmp.control.dao.personnel.EmployeeDAO;
import br.com.ceciliaprado.cmp.control.dao.personnel.ManagerDAO;
import br.com.ceciliaprado.cmp.control.dao.personnel.SubordinateDAO;
import br.com.ceciliaprado.cmp.control.dao.personnel.SupervisorDAO;
import br.com.ceciliaprado.cmp.control.model.personnel.io.PersonnelReader;
import br.com.ceciliaprado.cmp.exceptions.IOException;
import br.com.ceciliaprado.cmp.model.personnel.Employee;
import br.com.ceciliaprado.cmp.model.personnel.Manager;
import br.com.ceciliaprado.cmp.model.personnel.Subordinate;
import br.com.ceciliaprado.cmp.model.personnel.Supervisor;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author adrianohrl
 */
public class PersonnelReaderDAO extends PersonnelReader {
    
    private final EntityManager em;
    private final List<Employee> registeredEmployees = new ArrayList<>();

    public PersonnelReaderDAO(EntityManager em) {
        this.em = em;
    }
    
    @Override
    public void readFile(String fileName) throws IOException {
        super.readFile(fileName);
        register();
    }
    
    @Override
    public void readFile(InputStream in) throws IOException {
        super.readFile(in);
        register();
    }
    
    private void register() {
        EmployeeDAO employeeDAO = new EmployeeDAO(em);
        for (Employee employee : getReadEntities()) {
            if (!employeeDAO.isRegistered(employee)) {
                if (employee instanceof Subordinate) {
                    register((Subordinate) employee);
                } else if (employee instanceof Supervisor) {
                    register((Supervisor) employee);
                } else {
                    register((Manager) employee);
                }
                registeredEmployees.add(employee);
            }
        }
    }
    
    private void register(Subordinate subordinate) {
        SubordinateDAO subordinateDAO = new SubordinateDAO(em);
        subordinateDAO.create(subordinate);
    }
    
    private void register(Supervisor supervisor) {
        SupervisorDAO supervisorDAO = new SupervisorDAO(em);
        supervisorDAO.create(supervisor);
    }
    
    private void register(Manager manager) {
        ManagerDAO managerDAO = new ManagerDAO(em);
        managerDAO.create(manager);
    }

    public List<Employee> getRegisteredEmployees() {
        return registeredEmployees;
    }

    @Override
    public Iterator<Employee> iterator() {
        return registeredEmployees.iterator();
    }
    
}
