/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ceciliaprado.cmp.control.model.production;

import br.com.ceciliaprado.cmp.control.model.production.reports.filters.EmployeeRelatedEventsList;
import br.com.ceciliaprado.cmp.exceptions.ProductionStateMachineException;
import br.com.ceciliaprado.cmp.model.events.Casualty;
import br.com.ceciliaprado.cmp.model.events.CasualtyEntryEvent;
import br.com.ceciliaprado.cmp.model.events.EntryEvent;
import br.com.ceciliaprado.cmp.model.personnel.Sector;
import br.com.ceciliaprado.cmp.model.personnel.Subordinate;
import br.com.ceciliaprado.cmp.model.personnel.Supervisor;
import br.com.ceciliaprado.cmp.model.order.PhaseProductionOrder;
import br.com.ceciliaprado.cmp.exceptions.ProductionException;
import br.com.ceciliaprado.cmp.model.order.ProductionStates;
import java.io.Serializable;
import java.util.Calendar;

/**
 *
 * @author adrianohrl
 */
public class EntryEventsBuilder implements Serializable {
    
    private final Sector sector;
    private final Supervisor supervisor;
    private final EmployeeRelatedEventsList<EntryEvent> entryEvents = new EmployeeRelatedEventsList<>();

    public EntryEventsBuilder(Sector sector, Supervisor supervisor) {
        this.sector = sector;
        this.supervisor = supervisor;
    }
    
    /**
     * This method must be used when the new state of the input phase production order is STARTED.
     * @param phaseProductionOrder
     * @param subordinate
     * @param eventDate
     * @param observation
     * @return 
     * @throws ProductionException 
     */
    public EntryEvent buildEntryEvent(PhaseProductionOrder phaseProductionOrder, Subordinate subordinate, Calendar eventDate, String observation) throws ProductionException {
        if (phaseProductionOrder.getProductionState() != null) {
            throw new ProductionException("This method must be used when the input phase production order has not STARTED yet.");
        }
        return buildEntryEvent(new EntryEvent(sector, supervisor, phaseProductionOrder, subordinate, ProductionStates.STARTED, eventDate, observation));        
    }
    
    /**
     * This method must be used when the new state of the input phase production order is RESTARTED, or FINISHED.
     * @param phaseProductionOrder
     * @param subordinate
     * @param productionState
     * @param eventDate
     * @param observation
     * @return 
     * @throws ProductionException 
     */
    public EntryEvent buildEntryEvent(PhaseProductionOrder phaseProductionOrder, Subordinate subordinate, ProductionStates productionState, Calendar eventDate, String observation) throws ProductionException {
        if (phaseProductionOrder.getProductionState() == null) {
            throw new ProductionException("This method must not be used when the input phase production order has not STARTED yet.");
        }
        if (!productionState.isRestarted() && !productionState.isFinished()) {
            throw new ProductionException("This method must be used when the new state of the input phase production order is RESTARTED, or FINISHED.");
        }
        return buildEntryEvent(new EntryEvent(sector, supervisor, phaseProductionOrder, subordinate, productionState, eventDate, observation));        
    }
    
    /**
     * This method must be used when the new state of the input phase production order is PAUSED, or RETURNED.
     * @param phaseProductionOrder
     * @param subordinate
     * @param productionState
     * @param producedQuantity
     * @param eventDate
     * @param observation
     * @param casualty
     * @return 
     * @throws ProductionException 
     */
    public CasualtyEntryEvent buildEntryEvent(PhaseProductionOrder phaseProductionOrder, Subordinate subordinate, ProductionStates productionState, int producedQuantity, Calendar eventDate, String observation, Casualty casualty) throws ProductionException {
        if (phaseProductionOrder.getProductionState() == null) {
            throw new ProductionException("This method must not be used when the input phase production order has not STARTED yet.");
        }
        if (!productionState.isPaused() && !productionState.isReturned()) {
            throw new ProductionException("This method must be used when the new state of the input phase production order is PAUSED, or RETURNED.");
        }
        return (CasualtyEntryEvent) buildEntryEvent(new CasualtyEntryEvent(casualty, sector, supervisor, phaseProductionOrder, subordinate, productionState, producedQuantity, eventDate, observation));
    }
    
    protected EntryEvent buildEntryEvent(EntryEvent entryEvent) throws ProductionException {
        try {
            processStateTransition(entryEvent);
            entryEvents.add(entryEvent);
        } catch (ProductionStateMachineException e) {
            throw new ProductionException("Production state transition: " + e.getMessage());
        }
        return entryEvent;
    }
    
    private void processStateTransition(EntryEvent entryEvent) throws ProductionStateMachineException {
        PhaseProductionOrder phaseProductionOrder = entryEvent.getPhaseProductionOrder();
        phaseProductionOrder.process(entryEvent);
    }

    public EmployeeRelatedEventsList<EntryEvent> getEntryEvents() {
        return entryEvents;
    }
    
}