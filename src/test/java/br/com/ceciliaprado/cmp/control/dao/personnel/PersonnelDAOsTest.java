/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ceciliaprado.cmp.control.dao.personnel;

import br.com.ceciliaprado.cmp.control.dao.DataSource;
import br.com.ceciliaprado.cmp.model.personnel.Employee;
import br.com.ceciliaprado.cmp.model.personnel.Manager;
import br.com.ceciliaprado.cmp.model.personnel.Sector;
import br.com.ceciliaprado.cmp.model.personnel.Subordinate;
import br.com.ceciliaprado.cmp.model.personnel.Supervisor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;

/**
 *
 * @author adrianohrl
 */
public class PersonnelDAOsTest {
    
    private static EntityManager em = DataSource.createEntityManager();
    private static Map<String, Subordinate> subordinates;
    private static Map<String, Supervisor> supervisors;
    private static Map<String, Manager> managers;
    private static Map<String, Sector> sectors;
    
    public static void main(String[] args) {
        PersonnelDAOsTest.createEmployees();
        PersonnelDAOsTest.createSectors();
            
        Supervisor supervisor = supervisors.get("Rose");
        SupervisorDAO supervisorDAO = new SupervisorDAO(em);
        if (supervisor != null && supervisorDAO.isRegistered(supervisor)) {
            System.out.println("\n" + supervisor + "'s sectors:");
            for (Sector sector : supervisorDAO.findSupervisorSectors(supervisor)) {
                System.out.println("\t" + sector);
            }
        }
        
        SubordinateDAO subordinateDAO = new SubordinateDAO(em);
        String subordinateName = "Rose";
        boolean subordinateAvailable = subordinateDAO.isAvailable(subordinateName);
        System.out.println("\n" + subordinateName + " is " + (!subordinateAvailable ? "not " : "") + "available!!!");
        String sectorName = "Costura";
        if (!subordinateAvailable) {
            boolean workingAtSector = subordinateDAO.isWorkingAtSector(subordinateName, sectorName);
            System.out.println(subordinateName + " is " + (!workingAtSector ? "not " : "") + "working at " + sectorName + "!!!");
        }
        
        System.out.println("\n" + supervisor + "'s subordinates who is currently available or is working at " + sectorName + " sector:");
        for (Subordinate subordinate : subordinateDAO.findSupervisorAndSectorSubordinates(supervisor.getName(), sectorName)) {
            System.out.println("\t" + subordinate);
        }
        
        try {
            PersonnelTest.registerEmployees(subordinates.values());
            PersonnelTest.registerEmployees(supervisors.values());
            PersonnelTest.registerEmployees(managers.values());
            PersonnelTest.registerSectors(sectors.values());
            PersonnelTest.showAllRegisteredSubordinates();
            PersonnelTest.showAllRegisteredSupervisors();
            PersonnelTest.showAllRegisteredManagers();
            PersonnelTest.showAllRegisteredSectors();
        } catch (RuntimeException e) {
            System.out.println("Exception catched: " + e.getMessage());
        }
        em.close();
        DataSource.closeEntityManagerFactory();
    }
    
    private static void createEmployees() {
        List<Employee> employees = new ArrayList<>();
        /************************* Subordinates *************************/
        subordinates = new HashMap<>();
        subordinates.put("Joaquina", new Subordinate("00001", "Joaquina"));
        subordinates.put("Joaquim", new Subordinate("00002", "Joaquim"));
        subordinates.put("Rosana", new Subordinate("00003", "Rosana"));
        subordinates.put("Joana", new Subordinate("00004", "Joana"));
        subordinates.put("Maria", new Subordinate("00005", "Maria"));
        subordinates.put("João", new Subordinate("00006", "João"));
        subordinates.put("José", new Subordinate("00007", "José"));
        subordinates.put("Roseli", new Subordinate("00008", "Roseli"));
        subordinates.put("Marcelo", new Subordinate("00009", "Marcelo"));
        subordinates.put("Murilo", new Subordinate("00010", "Murilo"));
        employees.addAll(subordinates.values());
        /************************* Supervisors *************************/
        supervisors = new HashMap<>();
        Supervisor supervisor = new Supervisor("juh", "123456", "00011", "Juliane");
        supervisor.getSubordinates().add((Subordinate) subordinates.get("Joaquina"));
        supervisor.getSubordinates().add((Subordinate) subordinates.get("Rosana"));
        supervisor.getSubordinates().add((Subordinate) subordinates.get("José"));
        supervisor.getSubordinates().add((Subordinate) subordinates.get("Murilo"));
        supervisors.put(supervisor.getName(), supervisor);
        supervisor = new Supervisor("rose", "123", "00012", "Rose");
        supervisor.getSubordinates().add((Subordinate) subordinates.get("Joana"));
        supervisor.getSubordinates().add((Subordinate) subordinates.get("Maria"));
        supervisors.put(supervisor.getName(), supervisor);
        supervisor = new Supervisor("julio", "1234", "00013", "Julio");
        supervisor.getSubordinates().add((Subordinate) subordinates.get("Joaquim"));
        supervisor.getSubordinates().add((Subordinate) subordinates.get("Marcelo"));
        supervisors.put(supervisor.getName(), supervisor);
        supervisor = new Supervisor("ana", "123456789", "00014", "Ana");
        supervisor.getSubordinates().add((Subordinate) subordinates.get("João"));
        supervisor.getSubordinates().add((Subordinate) subordinates.get("Roseli"));
        supervisors.put(supervisor.getName(), supervisor);
        employees.addAll(supervisors.values());
        /************************* Managers *************************/
        managers = new HashMap<>();
        Manager manager = new Manager("carlos", "147258369", "00015", "Carlos");
        manager.getSupervisors().add(supervisors.get("Juliane"));
        manager.getSupervisors().add(supervisors.get("Ana"));
        managers.put(manager.getName(), manager);
        manager = new Manager("rafa", "147258", "00016", "Rafaela");
        manager.getSupervisors().add(supervisors.get("Rose"));
        manager.getSupervisors().add(supervisors.get("Julio"));
        managers.put(manager.getName(), manager);
        employees.addAll(managers.values());
        Collections.sort(employees);
        PersonnelTest.print(employees);
    }

    private static void createSectors() {
        sectors = new HashMap<>();
        SupervisorDAO supervisorDAO = new SupervisorDAO(em);
        Supervisor supervisor = supervisorDAO.find("Ana");
        Sector sector = new Sector("Passadoria", supervisor);
        sectors.put(sector.getName(), sector);
        supervisor = supervisorDAO.find("Rose");
        sector = new Sector("Costura", supervisor);
        sectors.put(sector.getName(), sector);
        supervisor = supervisorDAO.find("Julio");
        sector = new Sector("Tecimento", supervisor);
        sectors.put(sector.getName(), sector);
        supervisor = supervisorDAO.find("Juliane");
        sector = new Sector("Corte", supervisor);
        sectors.put(sector.getName(), sector);
    }
    
}
