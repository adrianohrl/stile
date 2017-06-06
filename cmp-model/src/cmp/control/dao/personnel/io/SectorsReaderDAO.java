/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cmp.control.dao.personnel.io;

import cmp.control.dao.personnel.SectorDAO;
import cmp.control.dao.personnel.SupervisorDAO;
import cmp.control.model.personnel.io.SectorsReader;
import cmp.exceptions.IOException;
import cmp.model.personnel.Sector;
import cmp.model.personnel.Supervisor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author adrianohrl
 */
public class SectorsReaderDAO extends SectorsReader {
    
    private final EntityManager em;
    private final List<Sector> registeredEmployees = new ArrayList<>();

    public SectorsReaderDAO(EntityManager em) {
        this.em = em;
    }    
    
    @Override
    public void readFile(String fileName) throws IOException {
        super.readFile(fileName);
        SectorDAO sectorDAO = new SectorDAO(em);
        for (Sector sector : getReadEntities()) {
            if (!sectorDAO.isRegistered(sector)) {
                sectorDAO.create(sector);
                registeredEmployees.add(sector);
            }
        }
    }
    
    @Override
    protected Supervisor getSupervisor(String supervisorName) throws IOException {
        SupervisorDAO supervisorDAO = new SupervisorDAO(em);
        return supervisorDAO.find(supervisorName);
    }

    @Override
    public Iterator<Sector> iterator() {
        return registeredEmployees.iterator();
    }
    
}