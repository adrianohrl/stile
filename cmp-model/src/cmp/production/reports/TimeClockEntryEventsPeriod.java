/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cmp.production.reports;

import cmp.exceptions.ReportException;
import cmp.model.events.CasualtyEntryEvent;
import cmp.model.events.EntryEvent;
import cmp.model.events.TimeClockEvent;
import cmp.model.production.PhaseProductionOrder;
import cmp.model.production.ProductionStates;

/**
 *
 * @author adrianohrl
 */
public class TimeClockEntryEventsPeriod extends AbstractEventsPeriod<TimeClockEvent, EntryEvent> {

    public TimeClockEntryEventsPeriod(PhaseProductionOrder phaseProductionOrder, TimeClockEvent firstEvent, EntryEvent lastEvent) throws ReportException {
        super(phaseProductionOrder, firstEvent, lastEvent);
        if (!firstEvent.isArrival()) {
            throw new ReportException("A DEPARTURE time clock event must be a first event only if the last event is an ARRIVAL time clock event!!!");
        }
    }

    @Override
    public double getProducedQuantity() {
        ProductionStates state = lastEvent.getProductionState();
        if (state.isPaused() || state.isFinished()) {
            return lastEvent.getProducedQuantity();
        }
        return 0.0;
    }

    @Override
    public double getReturnedQuantity() {
        if (lastEvent instanceof CasualtyEntryEvent && lastEvent.getProductionState().isReturned()) {
            CasualtyEntryEvent event = (CasualtyEntryEvent) lastEvent;
            return event.getReturnedQuantity();
        }
        return 0.0;
    }

    @Override
    public boolean isFreeWorkingPeriod() {
        return lastEvent.getProductionState().isStartingState();
    }

    @Override
    public boolean isEffectiveWorkingPeriod() {
        return !lastEvent.getProductionState().isStartingState();
    }
    
}
