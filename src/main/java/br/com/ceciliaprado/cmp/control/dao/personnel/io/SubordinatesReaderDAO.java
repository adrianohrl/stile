/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ceciliaprado.cmp.control.dao.personnel.io;

import br.com.ceciliaprado.cmp.control.dao.personnel.SubordinateDAO;
import br.com.ceciliaprado.cmp.control.dao.personnel.SupervisorDAO;
import br.com.ceciliaprado.cmp.control.model.personnel.io.SubordinatesReader;
import br.com.ceciliaprado.cmp.exceptions.IOException;
import br.com.ceciliaprado.cmp.model.personnel.Subordinate;
import br.com.ceciliaprado.cmp.model.personnel.Supervisor;
import javax.persistence.EntityManager;

/**
 *
 * @author adrianohrl
 */
public class SubordinatesReaderDAO extends SubordinatesReader {
    
    private final EntityManager em;

    public SubordinatesReaderDAO(EntityManager em) {
        this.em = em;
    }
    
    @Override
    public void readFile(String fileName) throws IOException {
        super.readFile(fileName);
        SupervisorDAO supervisorDAO = new SupervisorDAO(em);
        for (Supervisor supervisor : this) {
            supervisorDAO.update(supervisor);
        }
    }

    @Override
    protected Subordinate getSubordinate(String subordinateName) {
        SubordinateDAO subordinateDAO = new SubordinateDAO(em);
        return subordinateDAO.find(subordinateName);
    }

    @Override
    protected Supervisor getSupervisor(String supervisorName) {
        SupervisorDAO supervisorDAO = new SupervisorDAO(em);
        return supervisorDAO.find(supervisorName);
    }
    
}