/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ceciliaprado.cmp.model.personnel;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author adrianohrl
 */
@Entity
public abstract class Employee implements Comparable<Employee>, Serializable {
    
    @Column(nullable = false, unique = true)
    private String code;
    @Id
    private String name;

    public Employee() {
    }

    public Employee(String code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public int compareTo(Employee employee) {
        return name.compareToIgnoreCase(employee.name);
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof Employee && equals((Employee) obj);
    }

    public boolean equals(Employee employee) {
        return employee != null && name.equalsIgnoreCase(employee.name);
    }

    @Override
    public String toString() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}