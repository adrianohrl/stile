package tech.adrianohrl.stile.control.production.reports.filters;

import tech.adrianohrl.stile.model.events.AbstractEmployeeRelatedEvent;
import tech.adrianohrl.stile.model.events.EntryEvent;
import tech.adrianohrl.stile.model.personnel.Subordinate;
import tech.adrianohrl.stile.model.production.ModelPhase;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Adriano Henrique Rossette Leite (contact@adrianohrl.tech)
 */
public class EntryEventsList extends EmployeeRelatedEventsList<EntryEvent> {

    public EntryEventsList() {
    }

    public EntryEventsList(Collection<? extends EntryEvent> c) {
        super(c);
    }
    
    public List<ModelPhase> getPhases() {
        ArrayList<ModelPhase> phases = new ArrayList<>();
        for (EntryEvent event : this) {
            ModelPhase phase = event.getPhaseProductionOrder().getPhase();
            if (!phases.contains(phase)) {
                phases.add(phase);
            }
        }
        return phases;
    }
    
    public List<Subordinate> getInvolvedSubordinates() {
        List<Subordinate> subordinates = new ArrayList<>();
        for (EntryEvent event : this) {
            Subordinate subordinate = event.getEmployee();
            if (!subordinates.contains(subordinate)) {
                subordinates.add(subordinate);
            }
        }
        Collections.sort(subordinates);
        return subordinates;
    }
    
    public static EntryEventsList convert(EmployeeRelatedEventsList<AbstractEmployeeRelatedEvent> events) {
        EntryEventsList entryEvents = new EntryEventsList();
        for (AbstractEmployeeRelatedEvent event : events) {
            if (event instanceof EntryEvent) {
                entryEvents.add((EntryEvent) event);
            }
        }
        return entryEvents;
    }
    
}
