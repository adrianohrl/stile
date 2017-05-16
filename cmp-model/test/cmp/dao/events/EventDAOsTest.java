/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cmp.dao.events;

import cmp.dao.DataSource;
import cmp.dao.events.io.EntryEventsReaderDAO;
import cmp.dao.events.io.TimeClockEventsReaderDAO;
import cmp.exceptions.IOException;
import cmp.exceptions.ProductionStateMachineException;
import cmp.model.events.Casualty;
import cmp.production.reports.filters.EmployeeRelatedEventsList;
import cmp.util.Keyboard;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.RollbackException;

/**
 *
 * @author adrianohrl
 */
public class EventDAOsTest {
    
    private static EntityManager em = DataSource.createEntityManager();
    private static List<Casualty> casualties;
    
    public static void main(String[] args) throws ProductionStateMachineException {
        try {
            EventDAOsTest.createCasualties();
            EventsTest.registerCasualties(casualties);
            System.out.println("");
            EventsTest.showAllCasualties();
            System.out.println("");
            EventsTest.showAllCollectiveCasualties();
            System.out.println("");
            EventsTest.showAllNonCollectiveCasualties();
        } catch (RuntimeException e) {
            System.out.println("RuntimeException catched:" + e.getMessage());
        }
        try {
            Keyboard keyboard = Keyboard.getKeyboard();
            //String fileName = keyboard.readString("Enter the file name to import time clock events: ");
            String fileName = "../others/tests/ImportTimeClockEvents1.csv";
            TimeClockEventsReaderDAO timeClockEventsReader = new TimeClockEventsReaderDAO(em, fileName);
            timeClockEventsReader.readFile();
            //fileName = keyboard.readString("Enter the file name to import entry events: ");
            fileName = "../others/tests/ImportEntryEvents1.csv";
            EntryEventsReaderDAO entryEventsReader = new EntryEventsReaderDAO(em, fileName);
            entryEventsReader.readFile();
            EmployeeRelatedEventsList events = new EmployeeRelatedEventsList();
            events.addAll(timeClockEventsReader.getEmployeeRelatedEventsList());
            events.addAll(entryEventsReader.getEmployeeRelatedEventsList());
            EventsTest.register(events);
        } catch (RollbackException e) {
            System.out.println("RollbackException catched: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException catched: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("RuntimeException catched: " + e.getMessage());
        } finally {
            em.close();
            DataSource.closeEntityManagerFactory();
        }
    }    

    private static void createCasualties() {
        casualties = new ArrayList<>();
        casualties.add(new Casualty("Falta de matéria-prima"));
        casualties.add(new Casualty("Realocação"));
        casualties.add(new Casualty("Falta de energia elétrica", true));
    }
    
}