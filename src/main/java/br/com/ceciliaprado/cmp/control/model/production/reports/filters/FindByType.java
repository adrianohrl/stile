/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ceciliaprado.cmp.control.model.production.reports.filters;

import br.com.ceciliaprado.cmp.model.events.AbstractEmployeeRelatedEvent;
import br.com.ceciliaprado.cmp.util.AbstractFilter;

/**
 *
 * @author adrianohrl
 * @param <T>
 */
public class FindByType<T extends AbstractEmployeeRelatedEvent> extends AbstractFilter<AbstractEmployeeRelatedEvent> {
    
    private final String type;
    private final Class<T> clazz;
    
    public FindByType(Class<T> clazz) {
        this.clazz = clazz;
        this.type = clazz.getName();
    }

    @Override
    public void execute(AbstractEmployeeRelatedEvent item) {
        if (type.equals(item.getClass().getName())) {
            super.add(item);
        }
    }
    
    @Override
    public EmployeeRelatedEventsList<AbstractEmployeeRelatedEvent> getItems() {
        return new EmployeeRelatedEventsList<>(super.getItems());
    }
    
}
