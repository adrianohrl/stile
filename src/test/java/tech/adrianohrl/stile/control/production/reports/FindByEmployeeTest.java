/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tech.adrianohrl.stile.control.production.reports;

import tech.adrianohrl.stile.exceptions.StileException;
import tech.adrianohrl.stile.model.events.AbstractEmployeeRelatedEvent;
import tech.adrianohrl.stile.model.events.Casualty;
import tech.adrianohrl.stile.model.events.TimeClockEvent;
import tech.adrianohrl.stile.model.personnel.Sector;
import tech.adrianohrl.stile.model.personnel.Subordinate;
import tech.adrianohrl.stile.model.personnel.Supervisor;
import tech.adrianohrl.stile.model.production.Model;
import tech.adrianohrl.stile.model.production.Phase;
import tech.adrianohrl.stile.model.order.PhaseProductionOrder;
import tech.adrianohrl.stile.model.order.ProductionOrder;
import tech.adrianohrl.stile.model.order.ProductionStates;
import tech.adrianohrl.stile.control.production.EntryEventsBuilder;
import tech.adrianohrl.stile.control.production.reports.filters.EventsList;
import tech.adrianohrl.stile.control.production.reports.filters.FindByEmployee;
import tech.adrianohrl.stile.model.production.ModelPhase;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
 *
 * @author adrianohrl
 */
public class FindByEmployeeTest {
    
    public static void main(String[] args) throws StileException {
        Supervisor supervisor = new Supervisor("ahrl", "12345", "sup1", "Adriano Henrique Rossette Leite");
        Sector sector = new Sector("costura", supervisor);
        Subordinate subordinate1 = new Subordinate("sub1", "Subordinate 1");
        Subordinate subordinate2 = new Subordinate("sub2", "Subordinate 2");
        Subordinate subordinate3 = new Subordinate("sub3", "Subordinate 3");
        Subordinate subordinate4 = new Subordinate("sub4", "Subordinate 4");
        List<Subordinate> subordinates = new ArrayList<>();
        subordinates.add(subordinate1);
        subordinates.add(subordinate2);
        subordinates.add(subordinate3);
        subordinates.add(subordinate4);
        supervisor.setSubordinates(subordinates);
        
        Model model = new Model("ref1", "Reference 1");
        ModelPhase phase1 = new ModelPhase(new Phase("phase 1", sector), 10);
        ModelPhase phase2 = new ModelPhase(new Phase("phase 2", sector), 7.5);
        ModelPhase phase3 = new ModelPhase(new Phase("phase 3", sector), 5);
        ArrayList<ModelPhase> phases = new ArrayList<>();
        phases.add(phase1);
        phases.add(phase2);
        phases.add(phase3);
        model.setPhases(phases);
        
        ProductionOrder productionOrder = new ProductionOrder("production of ref1", model);
        
        PhaseProductionOrder phaseProductionOrder1 = new PhaseProductionOrder(phase1, productionOrder, 15);
        PhaseProductionOrder phaseProductionOrder2 = new PhaseProductionOrder(phase2, productionOrder, 15);
        PhaseProductionOrder phaseProductionOrder3 = new PhaseProductionOrder(phase3, productionOrder, 15);
        
        Casualty casualty1 = new Casualty("Falta de suprimento");
        Casualty casualty2 = new Casualty("Falta de energia elétrica");
        
        EntryEventsBuilder entryEventsBuilder = new EntryEventsBuilder(sector, supervisor);
        entryEventsBuilder.buildEntryEvent(phaseProductionOrder1, subordinate1, new GregorianCalendar(2017, 4, 3, 7, 5), "");
        entryEventsBuilder.buildEntryEvent(phaseProductionOrder1, subordinate1, ProductionStates.PAUSED, 5, new GregorianCalendar(2017, 4, 3, 9, 20), "", casualty2);
        entryEventsBuilder.buildEntryEvent(phaseProductionOrder1, subordinate1, ProductionStates.RESTARTED, new GregorianCalendar(2017, 4, 3, 13, 45), "");
        entryEventsBuilder.buildEntryEvent(phaseProductionOrder1, subordinate1, ProductionStates.RETURNED, 3, new GregorianCalendar(2017, 4, 3, 15, 0), "", casualty1);
        entryEventsBuilder.buildEntryEvent(phaseProductionOrder3, subordinate2, new GregorianCalendar(2017, 4, 3, 7, 3), "");
        entryEventsBuilder.buildEntryEvent(phaseProductionOrder3, subordinate2, ProductionStates.PAUSED, 7, new GregorianCalendar(2017, 4, 3, 9, 20), "", casualty2);
        entryEventsBuilder.buildEntryEvent(phaseProductionOrder3, subordinate3, ProductionStates.RESTARTED, new GregorianCalendar(2017, 4, 3, 9, 45), "");
        entryEventsBuilder.buildEntryEvent(phaseProductionOrder2, subordinate2, new GregorianCalendar(2017, 4, 3, 9, 45), "");
        entryEventsBuilder.buildEntryEvent(phaseProductionOrder2, subordinate2, ProductionStates.FINISHED, new GregorianCalendar(2017, 4, 3, 10, 45), "");
        entryEventsBuilder.buildEntryEvent(phaseProductionOrder3, subordinate3, ProductionStates.FINISHED, new GregorianCalendar(2017, 4, 3, 13, 45), "");
        
        List<TimeClockEvent> timeClockEvents = new ArrayList<>();
        timeClockEvents.add(new TimeClockEvent(subordinate1, true, new GregorianCalendar(2017, 4, 3, 7, 1), ""));
        timeClockEvents.add(new TimeClockEvent(subordinate2, true, new GregorianCalendar(2017, 4, 3, 7, 0), ""));
        timeClockEvents.add(new TimeClockEvent(subordinate3, true, new GregorianCalendar(2017, 4, 3, 8, 30), ""));
        timeClockEvents.add(new TimeClockEvent(subordinate1, false, new GregorianCalendar(2017, 4, 3, 11, 2), ""));
        timeClockEvents.add(new TimeClockEvent(subordinate2, false, new GregorianCalendar(2017, 4, 3, 11, 5), ""));
        timeClockEvents.add(new TimeClockEvent(subordinate3, false, new GregorianCalendar(2017, 4, 3, 11, 15), ""));
        timeClockEvents.add(new TimeClockEvent(subordinate1, true, new GregorianCalendar(2017, 4, 3, 12, 45), ""));
        timeClockEvents.add(new TimeClockEvent(subordinate2, true, new GregorianCalendar(2017, 4, 3, 13, 0), ""));
        timeClockEvents.add(new TimeClockEvent(subordinate3, true, new GregorianCalendar(2017, 4, 3, 13, 0), ""));
        timeClockEvents.add(new TimeClockEvent(subordinate1, false, new GregorianCalendar(2017, 4, 3, 17, 42), ""));
        timeClockEvents.add(new TimeClockEvent(subordinate2, false, new GregorianCalendar(2017, 4, 3, 17, 50), ""));
        timeClockEvents.add(new TimeClockEvent(subordinate3, false, new GregorianCalendar(2017, 4, 3, 18, 15), ""));
        
        EventsList<AbstractEmployeeRelatedEvent> events = new EventsList<>();
        events.addAll(entryEventsBuilder.getEntryEvents());
        events.addAll(timeClockEvents);
        FindByEmployee<AbstractEmployeeRelatedEvent> filter = new FindByEmployee<>(subordinate1);
        events.execute(filter);
        System.out.println("\nBefore filter: ");
        for (AbstractEmployeeRelatedEvent event : events) {
            System.out.println(event);
        }
        System.out.println("\nAfter filter: ");
        for (AbstractEmployeeRelatedEvent event : filter) {
            System.out.println(event);
        }
    }
    
}
