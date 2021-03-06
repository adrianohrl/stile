/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tech.adrianohrl.stile.control.dao.events;

import tech.adrianohrl.dao.DataSource;
import tech.adrianohrl.stile.control.dao.events.io.EntryEventsReaderDAO;
import tech.adrianohrl.stile.control.dao.events.io.TimeClockEventsReaderDAO;
import tech.adrianohrl.stile.control.dao.personnel.PersonnelKeyboardEntries;
import tech.adrianohrl.stile.control.dao.production.ProductionKeyboardEntries;
import tech.adrianohrl.stile.exceptions.DAOException;
import tech.adrianohrl.stile.exceptions.IOException;
import tech.adrianohrl.stile.exceptions.ProductionException;
import tech.adrianohrl.stile.exceptions.ReportException;
import tech.adrianohrl.stile.model.events.AbstractEmployeeRelatedEvent;
import tech.adrianohrl.stile.model.events.Casualty;
import tech.adrianohrl.stile.model.events.CasualtyEntryEvent;
import tech.adrianohrl.stile.model.events.EntryEvent;
import tech.adrianohrl.stile.model.events.TimeClockEvent;
import tech.adrianohrl.stile.model.personnel.Employee;
import tech.adrianohrl.stile.model.personnel.Sector;
import tech.adrianohrl.stile.model.personnel.Subordinate;
import tech.adrianohrl.stile.model.personnel.Supervisor;
import tech.adrianohrl.stile.model.order.PhaseProductionOrder;
import tech.adrianohrl.stile.model.order.ProductionStates;
import tech.adrianohrl.stile.control.production.reports.EmployeeEventsPeriodBuilder;
import tech.adrianohrl.stile.control.production.reports.filters.EmployeeRelatedEventsList;
import tech.adrianohrl.stile.control.production.reports.filters.EntryEventsList;
import tech.adrianohrl.stile.control.production.reports.filters.FindByEmployee;
import tech.adrianohrl.stile.model.production.ModelPhase;
import tech.adrianohrl.util.CalendarFormat;
import tech.adrianohrl.util.Calendars;
import tech.adrianohrl.util.Keyboard;
import tech.adrianohrl.util.KeyboardEntries;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.persistence.EntityManager;

/**
 *
 * @author adrianohrl
 */
public class EventsTest {
    
    private static EntityManager em = DataSource.createEntityManager();
    
    public static void main(String[] args) {
        EventsMenuOptions option = EventsMenuOptions.getOption();
        while (!option.quit()) {
            try {
                EventsTest.process(option);
            } catch (RuntimeException e) {
                System.out.println("Exception caught: " + e.getMessage());
            }
            option = EventsMenuOptions.getOption();
        }
        System.out.println("Quitting!!!");
        em.close();
        DataSource.closeEntityManagerFactory();
    }
    
    public static void process(EventsMenuOptions option) {
        if (option.quit()) {
            return;
        }
        switch (option) {
            case REGISTER_CASUALTY:
                EventsTest.createCasualty();
                break;
            case REGISTER_ENTRY_EVENT:
                EventsTest.createEntryEvent();
                break;
            case REGISTER_TIME_CLOCK_EVENT:
                EventsTest.createTimeClockEvent();
                break;
            case REGISTER_COLLECTIVE_ENTRY_EVENT_PER_SUBORDINATES:
                EventsTest.createCollectiveEntryEventPerSubordinates();
                break;
            case REGISTER_COLLECTIVE_ENTRY_EVENT_PER_SUPERVISOR:
                EventsTest.createCollectiveEntryEventPerSupervisor();
                break;
            case REGISTER_COLLECTIVE_ENTRY_EVENT_PER_SUPERVISORS:
                EventsTest.createCollectiveEntryEventPerSupervisors();
                break;
            case REGISTER_COLLECTIVE_ENTRY_EVENT_PER_SECTOR:
                EventsTest.createCollectiveEntryEventPerSector();
                break;
            case REGISTER_COLLECTIVE_ENTRY_EVENT_PER_SECTORS:
                EventsTest.createCollectiveEntryEventPerSectors();
                break;
            case SHOW_ALL_CASUALTIES:
                EventsTest.showAllCasualties();
                break;
            case SHOW_ALL_COLLECTIVE_CASUALTIES:
                EventsTest.showAllCollectiveCasualties();
                break;
            case SHOW_ALL_NON_COLLECTIVE_CASUALTIES:
                EventsTest.showAllNonCollectiveCasualties();
                break;
            case SHOW_ALL_ENTRY_EVENTS:
                EventsTest.showAllEntryEvents();
                break;
            case SHOW_PERIOD_ENTRY_EVENTS:
                EventsTest.showPeriodEntryEvents();
                break;
            case SHOW_ALL_TIME_CLOCK_EVENTS:
                EventsTest.showAllTimeClockEvents();
                break;
            case SHOW_PERIOD_TIME_CLOCK_EVENTS:
                EventsTest.showPeriodTimeClockEvents();
                break;
            case SHOW_ALL_EVENTS:
                EventsTest.showAllEvents();
                break;
            case SHOW_PERIOD_EVENTS:
                EventsTest.showPeriodEvents();
                break;
            case IMPORT_ENTRY_EVENTS:
                EventsTest.importEntryEvents();
                break;
            case IMPORT_TIME_CLOCK_EVENTS:
                EventsTest.importTimeClockEvents();
                break;
            case REPORT_PER_SUBORDINATE:
                EventsTest.reportPerformancePerSubordinate();
                break;
            case REPORT_PER_SUBORDINATES:
                EventsTest.reportPerformancePerSubordinates();
                break;
            case REPORT_PER_SUPERVISOR:
                EventsTest.reportPerformancePerSupervisor();
                break;
            case REPORT_PER_SUPERVISORS:
                EventsTest.reportPerformancePerSupervisors();
                break;
            case REPORT_PER_SECTOR:
                EventsTest.reportPerformancePerSector();
                break;
            case REPORT_PER_SECTORS:
                EventsTest.reportPerformancePerSectors();
                break;
            default:
                System.out.println("Invalid option!!!");
        }
    }

    private static void createCasualty() {
        System.out.println("\nRegistering a new casualty ...");
        Keyboard keyboard = Keyboard.getKeyboard();
        System.out.println("Enter the info of the new casualty below:");
        String name = keyboard.readString("name: ");
        boolean collective = KeyboardEntries.askForYesOrNo("collective");
        try {
            EventsTest.register(new Casualty(name, collective));
            System.out.println("The casualty registration succeeded!!!");
        } catch (RuntimeException e) {
            System.out.println("The casualty registration failed: " + e.getMessage() + "!!!");
            em.clear();
        }
    }
    
    private static void createEntryEvent() {
        System.out.println("\nRegistering a new entry event ...");
        System.out.println("Enter the supervisor:");
        Supervisor supervisor = PersonnelKeyboardEntries.selectOneSupervisor();
        EventsTest.createEntryEvent(supervisor);
    }

    private static void createEntryEvent(Supervisor supervisor) {
        Subordinate subordinate = PersonnelKeyboardEntries.selectOneSubordinateOfSupervisor(supervisor.getName());
        if (subordinate == null) {
            return;
        }
        EventsTest.createEntryEvent(supervisor, subordinate);
    }

    private static void createEntryEvent(Supervisor supervisor, Subordinate subordinate) {
        Sector sector = PersonnelKeyboardEntries.selectOneSector(supervisor);
        if (sector == null) {
            return;
        }
        PhaseProductionOrder phaseProductionOrder = ProductionKeyboardEntries.selectOnePhaseProductionOrder();
        if (phaseProductionOrder == null) {
            return;
        }
        Calendar timestamp = KeyboardEntries.askForDateAndTime();
        ProductionStates state = KeyboardEntries.selectOne(phaseProductionOrder.getPossibleNextStates(), "production state: ");
        Casualty casualty = null;
        int producedQuantity = 0;
        if (state.hasCasualty()) {
            casualty = EventsKeyboardEntries.selectOneCasualty();
            if (casualty == null) {
                return;
            }
            producedQuantity = KeyboardEntries.askForPositiveInteger("produced quantity: ");
        }
        Keyboard keyboard = Keyboard.getKeyboard();
        String observation = keyboard.readString("observation: ");
        EntryEvent entryEvent = null;
        try {
            switch (state) {
                case STARTED:
                case RESTARTED:
                case FINISHED:
                    entryEvent = new EntryEvent(sector, supervisor, phaseProductionOrder, subordinate, state, timestamp, observation);
                    break;
                case PAUSED:
                case RETURNED:
                    entryEvent = new CasualtyEntryEvent(casualty, sector, supervisor, phaseProductionOrder, subordinate, state, producedQuantity, timestamp, observation);
                    break;
                default:
                    System.out.println("Invalid production state!!!");
            }
            phaseProductionOrder.process(entryEvent);
            EventsTest.register(entryEvent);
            System.out.println("The entry event registration succeeded!!!");
        } catch (RuntimeException e) {
            System.out.println("The entry event registration failed: " + e.getMessage());
            em.clear();
        } catch (ProductionException e) {
            System.out.println("The entry event registration failed: " + e.getMessage());
        }
    }

    private static void createTimeClockEvent() {
        System.out.println("\nRegistering a new time clock event ...");
        Employee employee = PersonnelKeyboardEntries.selectOneEmployee();
        if (employee == null) {
            return;
        }
        EventsTest.createTimeClockEvent(employee);
    }
    
    private static void createTimeClockEvent(Employee employee) {
        System.out.println("\nRegistering " + employee + "'s time event ...");
        Keyboard keyboard = Keyboard.getKeyboard();
        System.out.println("Enter the info of the " + employee + "'s new time clock event below:");
        Calendar timestamp = KeyboardEntries.askForDateAndTime();
        boolean arrival = KeyboardEntries.askForYesOrNo("arrival");
        String observation = keyboard.readString("observation: ");
        try {
            TimeClockEvent timeClockEvent = new TimeClockEvent(employee, arrival, timestamp, observation);
            EventsTest.register(timeClockEvent);
            System.out.println("The time clock event registration succeeded!!!");
        } catch (RuntimeException e) {
            System.out.println("The time clock event registration failed: " + e.getMessage() + "!!!");
            em.clear();
        } 
    }

    private static void createCollectiveEntryEventPerSubordinates() {
        ProductionStates state = EventsTest.getProductionState("\nRegistering a new collective entry event for a group of subordinates ...");
        if (state == null) {
            return;
        }
        EntryEventDAO entryEventDAO = new EntryEventDAO(em);
        EntryEventsList entryEvents = null;
        if (state == ProductionStates.RESTARTED) {
            entryEvents = entryEventDAO.findEntryEventsThatCanBeRestarted();
        } else if (state == ProductionStates.PAUSED) {
            entryEvents = entryEventDAO.findEntryEventsThatCanBePaused();
        } 
        EventsTest.createCollectiveEntryEvents(entryEvents, state);
    }

    private static void createCollectiveEntryEventPerSupervisor() {
        ProductionStates state = EventsTest.getProductionState("\nRegistering a new collective entry event for all supervisor subordinates ...");
        if (state == null) {
            return;
        }
        System.out.println("Enter the supervisor:");
        Supervisor supervisor = PersonnelKeyboardEntries.selectOneSupervisor();
        if (supervisor == null) {
            return;
        }
        EntryEventDAO entryEventDAO = new EntryEventDAO(em);
        EntryEventsList entryEvents = null;
        if (state == ProductionStates.RESTARTED) {
            entryEvents = entryEventDAO.findEntryEventsThatCanBeRestarted(supervisor);
        } else if (state == ProductionStates.PAUSED) {
            entryEvents = entryEventDAO.findEntryEventsThatCanBePaused(supervisor);
        } 
        EventsTest.createCollectiveEntryEvents(entryEvents, state);
    }

    private static void createCollectiveEntryEventPerSupervisors() {
        ProductionStates state = EventsTest.getProductionState("\nRegistering a new collective entry event for all supervisors subordinates ...");
        if (state == null) {
            return;
        }
        System.out.println("Enter the supervisors:");
        List<Supervisor> supervisors = PersonnelKeyboardEntries.selectManySupervisors();
        if (supervisors == null) {
            return;
        }
        EntryEventDAO entryEventDAO = new EntryEventDAO(em);
        EntryEventsList entryEvents = new EntryEventsList();
        for (Supervisor supervisor : supervisors) {
            if (state == ProductionStates.RESTARTED) {
                entryEvents.addAll(entryEventDAO.findEntryEventsThatCanBeRestarted(supervisor));
            } else if (state == ProductionStates.PAUSED) {
                entryEvents.addAll(entryEventDAO.findEntryEventsThatCanBePaused(supervisor));
            } 
        }
        EventsTest.createCollectiveEntryEvents(entryEvents, state);
    }

    private static void createCollectiveEntryEventPerSector() {
        ProductionStates state = EventsTest.getProductionState("\nRegistering a new collective entry event for all sector subordinates ...");
        if (state == null) {
            return;
        }
        System.out.println("Enter the sector:");
        Sector sector = PersonnelKeyboardEntries.selectOneSector();
        if (sector == null) {
            return;
        }
        EntryEventDAO entryEventDAO = new EntryEventDAO(em);
        EntryEventsList entryEvents = null;
        if (state == ProductionStates.RESTARTED) {
            entryEvents = entryEventDAO.findEntryEventsThatCanBeRestarted(sector);
        } else if (state == ProductionStates.PAUSED) {
            entryEvents = entryEventDAO.findEntryEventsThatCanBePaused(sector);
        } 
        EventsTest.createCollectiveEntryEvents(entryEvents, state);
    }

    private static void createCollectiveEntryEventPerSectors() {
        ProductionStates state = EventsTest.getProductionState("\nRegistering a new collective entry event for all sectors subordinates ...");
        if (state == null) {
            return;
        }
        System.out.println("Enter the sectors:");
        List<Sector> sectors = PersonnelKeyboardEntries.selectManySectors();
        if (sectors == null) {
            return;
        }
        EntryEventDAO entryEventDAO = new EntryEventDAO(em);
        EntryEventsList entryEvents = new EntryEventsList();
        for (Sector sector : sectors) {
            if (state == ProductionStates.RESTARTED) {
                entryEvents.addAll(entryEventDAO.findEntryEventsThatCanBeRestarted(sector));
            } else if (state == ProductionStates.PAUSED) {
                entryEvents.addAll(entryEventDAO.findEntryEventsThatCanBePaused(sector));
            } 
        }
        EventsTest.createCollectiveEntryEvents(entryEvents, state);
    }
    
    private static ProductionStates getProductionState(String prompt) {
        System.out.println(prompt);
        System.out.println("Enter the production state:");
        return ProductionKeyboardEntries.selectOneRestartedOrPausedState();
    }

    private static void createCollectiveEntryEvents(EntryEventsList entryEvents, ProductionStates state) {
        if (entryEvents == null) {
            return;
        }
        if (entryEvents.isEmpty()) {
            System.out.println("There is no phase production order that can be taken to this state in the present moment!!!");
            return;
        }
        List<Subordinate> subordinates = KeyboardEntries.selectMany(entryEvents.getInvolvedSubordinates(), "subordinate");
        Calendar timestamp = KeyboardEntries.askForDateAndTime();
        Keyboard keyboard = Keyboard.getKeyboard();
        String observation = keyboard.readString("observation: ");        
        FindByEmployee filter;
        List<EntryEvent> filteredEntryEvents = new ArrayList<>();
        for (Subordinate subordinate : subordinates) {
            filter = new FindByEmployee(subordinate);
            entryEvents.execute(filter);
            filteredEntryEvents.addAll(filter.getItems());
        }
        if (state == ProductionStates.RESTARTED) {
            EventsTest.createCollectiveEntryEvents(filteredEntryEvents, timestamp, observation);
        } else if (state == ProductionStates.PAUSED) {
            Casualty casualty = EventsKeyboardEntries.selectOneCollectiveCasualty();
            if (casualty == null) {
                return;
            }
            EventsTest.createCollectiveEntryEvents(filteredEntryEvents, casualty, timestamp, observation);
        }        
    }
    
    private static void createCollectiveEntryEvents(List<EntryEvent> entryEvents, Calendar timestamp, String observation)  {
        Sector sector;
        Supervisor supervisor;
        PhaseProductionOrder phaseProductionOrder;
        Subordinate subordinate;
        EntryEvent entryEvent;
        for (EntryEvent previousEntryEvent : entryEvents) {
            sector = previousEntryEvent.getSector();
            supervisor = previousEntryEvent.getSupervisor();
            phaseProductionOrder = previousEntryEvent.getPhaseProductionOrder();
            subordinate = previousEntryEvent.getSubordinate();
            if (subordinate == null) {
                continue;
            }
            try {
                entryEvent = new EntryEvent(sector, supervisor, phaseProductionOrder, subordinate, ProductionStates.RESTARTED, timestamp, observation);
                phaseProductionOrder.process(entryEvent);
                EventsTest.register(entryEvent);
                System.out.println(subordinate + "'s phase production order transaction succeeded!!!");
            } catch (ProductionException e) {
                System.out.println(subordinate + "'s phase production order transaction failed: " + e.getMessage());
            } catch (RuntimeException e) {
                System.out.println(subordinate + "'s phase production order transaction failed: " + e.getMessage());
                em.clear();
            }
        }
    }
    
    private static void createCollectiveEntryEvents(List<EntryEvent> entryEvents, Casualty casualty, Calendar timestamp, String observation)  {
        Sector sector;
        Supervisor supervisor;
        PhaseProductionOrder phaseProductionOrder;
        Subordinate subordinate;
        CasualtyEntryEvent casualtyEntryEvent;
        for (EntryEvent previousEntryEvent : entryEvents) {
            sector = previousEntryEvent.getSector();
            supervisor = previousEntryEvent.getSupervisor();
            phaseProductionOrder = previousEntryEvent.getPhaseProductionOrder();
            subordinate = previousEntryEvent.getSubordinate();
            if (subordinate == null) {
                continue;
            }
            try {
                casualtyEntryEvent = new CasualtyEntryEvent(casualty, sector, supervisor, phaseProductionOrder, subordinate, ProductionStates.PAUSED, 0, timestamp, observation);
                phaseProductionOrder.process(casualtyEntryEvent);
                EventsTest.register(casualtyEntryEvent);
                System.out.println(subordinate + "'s phase production order transaction succeeded!!!");
            } catch (ProductionException e) {
                System.out.println(subordinate + "'s phase production order transaction failed: " + e.getMessage());
            } catch (RuntimeException e) {
                System.out.println(subordinate + "'s phase production order transaction failed: " + e.getMessage());
                em.clear();
            } 
        }
    }

    public static void showAllCasualties() {
        System.out.println("Showing all registered casualties ...");
        CasualtyDAO casualtyDAO = new CasualtyDAO(em);
        for (Casualty casualty : casualtyDAO.findAll()) {
            System.out.println("Casualty: " + casualty + (casualty.isCollective() ? "*" : ""));
        }
        System.out.println("\nOBS.: (*) collective casualty.");
    }

    public static void showAllCollectiveCasualties() {
        System.out.println("Showing all registered collective casualties ...");
        CasualtyDAO casualtyDAO = new CasualtyDAO(em);
        for (Casualty casualty : casualtyDAO.findCollectives()) {
            System.out.println("Casualty: " + casualty);
        }
    }

    public static void showAllNonCollectiveCasualties() {
        System.out.println("Showing all registered non collective casualties ...");
        CasualtyDAO casualtyDAO = new CasualtyDAO(em);
        for (Casualty casualty : casualtyDAO.findNonCollectives()) {
            System.out.println("Casualty: " + casualty);
        }
    }

    public static void showAllEntryEvents() {
        System.out.println("Showing all registered subordinate entry events ...");
        Subordinate subordinate = PersonnelKeyboardEntries.selectOneSubordinate();
        if (subordinate == null) {
            return;
        }
        EventsTest.showSubordinateEntryEvents(subordinate);        
    }
    
    public static void showPeriodEntryEvents() {
        Calendar start = KeyboardEntries.askForDateAndTime();
        Calendar end = KeyboardEntries.askForDateAndTime();
        System.out.println("Showing registered subordinate period entry events from " + CalendarFormat.format(start) + " to " + CalendarFormat.format(end) + " ...");
        Subordinate subordinate = PersonnelKeyboardEntries.selectOneSubordinate();
        if (subordinate == null) {
            return;
        }
        try {
            EventsTest.showSubordinateEntryEvents(subordinate, start, end);
        } catch (DAOException e) {
            System.out.println("DAOException caught: " + e.getMessage());
        }
    }
    
    private static void showSubordinateEntryEvents(Subordinate subordinate) {
        EntryEventDAO entryEventDAO = new EntryEventDAO(em);
        EventsTest.showEvents(entryEventDAO.findEmployeeEvents(subordinate));
    }
    
    private static void showSubordinateEntryEvents(Subordinate subordinate, Calendar start, Calendar end) throws DAOException {
        EntryEventDAO entryEventDAO = new EntryEventDAO(em);
        EventsTest.showEvents(entryEventDAO.findEmployeeEvents(subordinate, start, end));
    }
    
    private static void showEvents(List<? extends AbstractEmployeeRelatedEvent> events) {
        for (AbstractEmployeeRelatedEvent event : events) {
            System.out.println("\t" + event);
        }
    }

    public static void showAllTimeClockEvents() {
        System.out.println("Showing all registered employee time clock events ...");
        Employee employee = PersonnelKeyboardEntries.selectOneEmployee();
        if (employee == null) {
            return;
        }
        EventsTest.showEmployeeTimeClockEvents(employee);
    }
    
    public static void showPeriodTimeClockEvents() {
        Calendar start = KeyboardEntries.askForDateAndTime();
        Calendar end = KeyboardEntries.askForDateAndTime();
        System.out.println("Showing registered employee period time clock events from " + CalendarFormat.format(start) + " to " + CalendarFormat.format(end) + " ...");
        Employee employee = PersonnelKeyboardEntries.selectOneEmployee();
        if (employee == null) {
            return;
        }
        try {
            EventsTest.showEmployeeTimeClockEvents(employee, start, end);
        } catch (DAOException e) {
            System.out.println("DAOException caught: " + e.getMessage());
        }
    }
    
    private static void showEmployeeTimeClockEvents(Employee employee) {
        TimeClockEventDAO timeClockEventDAO = new TimeClockEventDAO(em);
        EventsTest.showEvents(timeClockEventDAO.findEmployeeEvents(employee));
    }
    
    private static void showEmployeeTimeClockEvents(Employee employee, Calendar start, Calendar end) throws DAOException {
        TimeClockEventDAO timeClockEventDAO = new TimeClockEventDAO(em);
        EventsTest.showEvents(timeClockEventDAO.findEmployeeEvents(employee, start, end));
    }

    public static void showAllEvents() {
        System.out.println("Showing all registered employee events ...");
        Employee employee = PersonnelKeyboardEntries.selectOneEmployee();
        if (employee == null) {
            return;
        }
        EventsTest.showEmployeeEvents(employee);
    }

    public static void showPeriodEvents() {
        Calendar start = KeyboardEntries.askForDateAndTime();
        Calendar end = KeyboardEntries.askForDateAndTime();
        System.out.println("Showing registered employee period events " + CalendarFormat.format(start) + " to " + CalendarFormat.format(end) + " ...");
        Employee employee = PersonnelKeyboardEntries.selectOneEmployee();
        if (employee == null) {
            return;
        }
        try {
            EventsTest.showEmployeeEvents(employee, start, end);
        } catch (DAOException e) {
            System.out.println("DAOException caught: " + e.getMessage());
        }
    }
    
    private static void showEmployeeEvents(Employee employee) {
        AbstractEmployeeRelatedEventDAO eventDAO = new AbstractEmployeeRelatedEventDAO(em);
        EventsTest.showEvents(eventDAO.findEmployeeEvents(employee));
    }
    
    private static void showEmployeeEvents(Employee employee, Calendar start, Calendar end) throws DAOException {
        AbstractEmployeeRelatedEventDAO eventDAO = new AbstractEmployeeRelatedEventDAO(em);
        EventsTest.showEvents(eventDAO.findEmployeeEvents(employee, start, end));
    }

    public static void importEntryEvents() {
        System.out.println("\nImporting entry events ...");
        Keyboard keyboard = Keyboard.getKeyboard();
        EntryEventsReaderDAO reader = new EntryEventsReaderDAO(em);
        String fileName = keyboard.readString("Enter the file name: ");
        try {
            reader.readFile(fileName);
            EmployeeRelatedEventsList events = new EmployeeRelatedEventsList();
            events.addAll(reader.getEmployeeRelatedEventsList());
            EventsTest.register(events);
            System.out.println("Entry events importation succeeded!!!");
        } catch (IOException e) {
            System.out.println("Entry events importation failed: " + e.getMessage());
        }
    }

    public static void importTimeClockEvents() {
        System.out.println("\nImporting time clock events ...");
        Keyboard keyboard = Keyboard.getKeyboard();
        TimeClockEventsReaderDAO reader = new TimeClockEventsReaderDAO(em);
        String fileName = keyboard.readString("Enter the file name: ");
        try {
            reader.readFile(fileName);
            EmployeeRelatedEventsList events = new EmployeeRelatedEventsList();
            events.addAll(reader.getEmployeeRelatedEventsList());
            EventsTest.register(events);
            System.out.println("Time clock events importation succeeded!!!");
        } catch (IOException e) {
            System.out.println("Time clock events importation failed: " + e.getMessage());
        }
    }

    public static void reportPerformancePerSubordinate() {
        System.out.println("Reporting subordinate performance ...");
        Subordinate subordinate = PersonnelKeyboardEntries.selectOneSubordinate();
        System.out.println("Enter the start date:");
        Calendar start = KeyboardEntries.askForDate();
        System.out.println("Enter the end date:");
        Calendar end = KeyboardEntries.askForDate();
        try {
            end = Calendars.combine(end, "23:59:59");
        } catch (IOException e) {
            System.out.println("IOException caught: " + e.getMessage());
            return;
        }
        EventsTest.reportPerformance(subordinate, start, end);
    }
    
    private static void reportPerformance(Subordinate subordinate, Calendar start, Calendar end) {
        if (subordinate == null || start.after(end)) {
            return;
        }
        System.out.println("\n\tReporting " + subordinate + "'s performance:");
        AbstractEmployeeRelatedEventDAO eventsDAO = new AbstractEmployeeRelatedEventDAO(em);
        try {
            EmployeeRelatedEventsList events = eventsDAO.findEmployeeEvents(subordinate, start, end);
            if (events.isEmpty()) {
                System.out.println("\t\tNo activity found in the given period!!!");
                return;
            }
            EmployeeEventsPeriodBuilder builder = new EmployeeEventsPeriodBuilder(subordinate, events);
            for (ModelPhase phase : builder.getPhases()) {
                System.out.println("\t\tPhase: " + phase);
                System.out.println("\t\t\tEffective Duration: " + builder.getEffectiveDuration(phase) + " [min]");
                System.out.println("\t\t\tExpected Duration: " + builder.getExpectedDuration(phase) + " [min]");
                System.out.println("\t\t\tProduced Quantity: " + builder.getProducedQuantity(phase) + " [un]");
                System.out.println("\t\t\tReturned Quantity: " + builder.getReturnedQuantity(phase) + " [un]");
                System.out.println("\t\t\tEffective Efficiency: " + (builder.getEffectiveEfficiency(phase) * 100) + " %");
            }
            System.out.println("\n\t\t-------------------------------------------------------------\n");
            System.out.println("\t\tTotals:");
            System.out.println("\t\t\tEffective Duration: " + builder.getTotalEffectiveDuration() + " [min]");
            System.out.println("\t\t\tExpected Duration: " + builder.getTotalExpectedDuration() + " [min]");
            System.out.println("\t\t\tFree Duration: " + builder.getTotalFreeDuration() + " [min]");
            System.out.println("\t\t\tTotal Duration: " + builder.getTotalDuration() + " [min]");
            System.out.println("\t\t\tProduced Quantity: " + builder.getTotalProducedQuantity() + " [un]");
            System.out.println("\t\t\tReturned Quantity: " + builder.getTotalReturnedQuantity() + " [un]");
            System.out.println("\t\t\tEffective Efficiency: " + (builder.getTotalEffectiveEfficiency() * 100) + " %");
            System.out.println("\t\t\tTotal Efficiency: " + (builder.getTotalEfficiency() * 100) + " %");
            System.out.println("\n=====================================================================");
        } catch (DAOException | ReportException e) {
            System.out.println(e.getClass().getSimpleName() + " caught: " + e.getMessage());
        } 
    }

    public static void reportPerformancePerSubordinates() {
        System.out.println("Reporting subordinates performance ...");
        EventsTest.reportPermance(PersonnelKeyboardEntries.selectManySubordinates());
    }
    
    private static void reportPermance(Collection<Subordinate> subordinates) {
        if (subordinates == null) {
            return;
        }
        System.out.println("Enter the start date:");
        Calendar start = KeyboardEntries.askForDate();
        System.out.println("Enter the end date:");
        Calendar end = KeyboardEntries.askForDate();
        try {
            end = Calendars.combine(end, "23:59:59");
        } catch (IOException e) {
            System.out.println("IOException caught: " + e.getMessage());
            return;
        }
        for (Subordinate subordinate : subordinates) {
            EventsTest.reportPerformance(subordinate, start, end);
        }
    }

    public static void reportPerformancePerSupervisor() {
        System.out.println("Reporting supervisor subordinates performance ...");
        Supervisor supervisor = PersonnelKeyboardEntries.selectOneSupervisor();
        if (supervisor == null) {
            return;
        }
        EventsTest.reportPermance(supervisor.getSubordinates());
    }

    private static void reportPerformancePerSupervisors() {
        System.out.println("Reporting supervisors subordinates performance ...");
        List<Supervisor> supervisors = PersonnelKeyboardEntries.selectManySupervisors();
        if (supervisors == null) {
            return;
        }
        SortedSet<Subordinate> subordinates = new TreeSet<>();
        for (Supervisor supervisor : supervisors) {
            if (supervisor != null) {
                subordinates.addAll(supervisor.getSubordinates());
            }
        }
        EventsTest.reportPermance(subordinates);
    }

    private static void reportPerformancePerSector() {
        System.out.println("Reporting sector subordinates performance ...");
        Sector sector = PersonnelKeyboardEntries.selectOneSector();
        if (sector == null) {
            return;
        }
        Supervisor supervisor = sector.getSupervisor();
        if (supervisor == null) {
            return;
        }
        EventsTest.reportPermance(supervisor.getSubordinates());        
    }

    private static void reportPerformancePerSectors() {
        System.out.println("Reporting sectors subordinates performance ...");
        List<Sector> sectors = PersonnelKeyboardEntries.selectManySectors();
        if (sectors == null) {
            return;
        }
        Supervisor supervisor;
        SortedSet<Subordinate> subordinates = new TreeSet<>();
        for (Sector sector : sectors) {
            supervisor = sector.getSupervisor();
            if (supervisor != null) {
                subordinates.addAll(supervisor.getSubordinates());
            }
        }
        EventsTest.reportPermance(subordinates);  
    }
    
    public static void registerCasualties(Collection<Casualty> casualties) {
        for (Casualty casualty : casualties) {
            register(casualty);
        }
    }
    
    public static void register(Casualty casualty) {
        CasualtyDAO casualtyDAO = new CasualtyDAO(em);
        casualtyDAO.create(casualty);
    }
    
    public static void register(EmployeeRelatedEventsList<? extends AbstractEmployeeRelatedEvent> events) {
        for (AbstractEmployeeRelatedEvent event : events) { 
            EventsTest.register(event);
        }
    }
    
    public static void register(AbstractEmployeeRelatedEvent event) {
        if (event instanceof TimeClockEvent) {
            TimeClockEventDAO timeClockEventDAO = new TimeClockEventDAO(em);
            timeClockEventDAO.create((TimeClockEvent) event);
        } else if (event instanceof CasualtyEntryEvent) {
            CasualtyEntryEventDAO casualtyEntryEventDAO = new CasualtyEntryEventDAO(em);
            casualtyEntryEventDAO.create((CasualtyEntryEvent) event);
        } else if (event instanceof EntryEvent) {
            EntryEventDAO entryEventDAO = new EntryEventDAO(em);
            entryEventDAO.create(event);
        } 
    }
    
}
