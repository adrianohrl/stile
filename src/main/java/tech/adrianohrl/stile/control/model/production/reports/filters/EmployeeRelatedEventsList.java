/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tech.adrianohrl.stile.control.model.production.reports.filters;

import tech.adrianohrl.stile.model.events.AbstractEmployeeRelatedEvent;
import tech.adrianohrl.stile.model.personnel.Employee;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author adrianohrl
 * @param <T>
 */
public class EmployeeRelatedEventsList<T extends AbstractEmployeeRelatedEvent> extends EventsList<T> {

    public EmployeeRelatedEventsList() {
    }

    public EmployeeRelatedEventsList(Collection<? extends T> c) {
        super(c);
    }
    
    public List<Employee> getInvolvedEmployees() {
        List<Employee> employees = new ArrayList<>();
        for (AbstractEmployeeRelatedEvent event : this) {
            Employee employee = event.getEmployee();
            if (!employees.contains(employee)) {
                employees.add(employee);
            }
        }
        Collections.sort(employees);
        return employees;
    }
    
}