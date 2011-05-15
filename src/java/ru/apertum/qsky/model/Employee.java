/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.apertum.qsky.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author egorov
 */
@Entity
@Table(name = "employee")
public class Employee extends Element {

    public Employee() {
    }

    public Employee(Long branchId, Long employeeId, String name) {
        this.name = name;
        this.branchId = branchId;
        this.employeeId = employeeId;
    }
    @Column(name = "name", length = 255, nullable = false, columnDefinition = "")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Column(name = "branch_id")
    private Long branchId;

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }
    @Column(name = "employee_id")
    private Long employeeId;

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
}
