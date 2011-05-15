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
@Table(name = "company")
public class Company extends Element {

    @Column(name = "name", length = 255, nullable = false, columnDefinition = "")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}