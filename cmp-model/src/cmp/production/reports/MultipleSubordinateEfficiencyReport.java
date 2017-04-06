/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cmp.production.reports;

import cmp.exceptions.ReportException;
import cmp.model.events.EntryEvent;
import cmp.model.events.TimeClockEvent;
import cmp.model.personal.Manager;
import cmp.model.personal.Subordinate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

/**
 *
 * @author adrianohrl
 */
public class MultipleSubordinateEfficiencyReport extends AbstractEfficiencyReport implements Iterable<SubordinateEfficiencyReport> {

    private final ArrayList<SubordinateEfficiencyReport> subordinateEfficiencyReports = new ArrayList<>();

    public MultipleSubordinateEfficiencyReport(ArrayList<Subordinate> subordinates, ArrayList<TimeClockEvent> timeClockEvents, ArrayList<EntryEvent> entryEvents, Manager manager, Calendar startDate, Calendar endDate) throws ReportException {
        super(timeClockEvents, entryEvents, manager, startDate, endDate);
        for (Subordinate subordinate : subordinates) {
            subordinateEfficiencyReports.add(new SubordinateEfficiencyReport(subordinate, timeClockEvents, entryEvents, manager, startDate, endDate));
        }
    }
    
    @Override
    public Iterator<SubordinateEfficiencyReport> iterator() {
        return  subordinateEfficiencyReports.iterator();
    }
    
}
