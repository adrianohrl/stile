/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cmp.control.model.events.io;

import cmp.util.BooleanField;
import cmp.util.Field;
import cmp.util.CalendarField;
import cmp.util.StringField;
import cmp.exceptions.IOException;
import cmp.exceptions.ReportException;
import cmp.model.events.TimeClockEvent;
import cmp.model.personnel.Employee;
import cmp.model.personnel.Subordinate;
import cmp.control.model.production.reports.EventsPeriodBuilder;
import cmp.control.model.production.reports.filters.EmployeeRelatedEventsList;
import cmp.control.model.production.reports.filters.FindByEmployee;
import cmp.util.AbstractReader;
import cmp.util.Calendars;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author adrianohrl
 */
public class TimeClockEventsReader extends AbstractReader<TimeClockEvent> {
    
    /** Column Titles **/
    private final static String DATE_COLUMN_TITLE = "Date";
    private final static String TIME_COLUMN_TITLE = "Time";
    private final static String EMPLOYEE_COLUMN_TITLE = "Employee";
    private final static String ARRIVAL_COLUMN_TITLE = "Arrival";
    private final static String OBSERVATION_COLUMN_TITLE = "Observation";
    
    @Override
    protected List<Field> getDefaultFields() {
        List<Field> defaultFields = new ArrayList<>();
        defaultFields.add(new CalendarField(DATE_COLUMN_TITLE, "dd/MM/yyyy", true));
        defaultFields.add(new CalendarField(TIME_COLUMN_TITLE, "HH:mm", true));
        defaultFields.add(new StringField(EMPLOYEE_COLUMN_TITLE, true));
        defaultFields.add(new BooleanField(ARRIVAL_COLUMN_TITLE, "y", true));
        defaultFields.add(new StringField(OBSERVATION_COLUMN_TITLE, false));
        return defaultFields;
    }

    @Override
    public void readFile(String fileName) throws IOException {
        super.readFile(fileName);
        validate();
    }
    
    @Override
    protected TimeClockEvent build(List<Field> fields) throws IOException {
        Calendar calendar = Calendars.sum((Calendar) Field.getFieldValue(fields, DATE_COLUMN_TITLE), (Calendar) Field.getFieldValue(fields, TIME_COLUMN_TITLE));
        Employee employee = createEmployee(Field.getFieldValue(fields, EMPLOYEE_COLUMN_TITLE));
        return new TimeClockEvent(employee, Field.getFieldValue(fields, ARRIVAL_COLUMN_TITLE), calendar, Field.getFieldValue(fields, OBSERVATION_COLUMN_TITLE));
    }
    
    protected Employee createEmployee(String employeeName) throws IOException {
        return new Subordinate("", employeeName);
    }
    
    private void validate() throws IOException {
        TimeClockEvent previous;
        FindByEmployee<TimeClockEvent> filter;
        EmployeeRelatedEventsList<TimeClockEvent> events = new EmployeeRelatedEventsList<>(getReadEntities());
        for (Employee employee : events.getInvolvedEmployees()) {
            filter = new FindByEmployee<>(employee);
            events.execute(filter);
            previous = null;
            for (TimeClockEvent current : filter.getItems()) {
                if (previous != null) {
                    if (previous.compareTo(current) >= 0) {
                        throw new IOException(previous + " must happen before " + current);
                    }
                    if (previous.isArrival() == current.isArrival()) {
                        throw new IOException(previous + " must be different than " + current);
                    }
                }
                previous = current;
            }
        }
    }
    
    public EmployeeRelatedEventsList getEmployeeRelatedEventsList() {
        return new EmployeeRelatedEventsList(getReadEntities());
    }
    
    public EventsPeriodBuilder getEventsPeriodBuilder() throws ReportException {
        return new EventsPeriodBuilder(new EmployeeRelatedEventsList(getReadEntities()));
    }
    
}