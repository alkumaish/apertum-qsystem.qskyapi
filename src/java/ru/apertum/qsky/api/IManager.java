/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.apertum.qsky.api;

/**
 *
 * @author egorov
 */
public interface IManager {

    public Long createCompany(String name);

    public Boolean removeCompany(Long companyId);

    public Boolean renameCompany(Long companyId, String name);

    public Long addBranchOffice(Long companyId, String name);

    public Boolean removeBranchOffice(Long branchId);

    public Boolean renameBranchOffice(Long branchId, String name);

    public void setActiveBranch(Long branchId, Boolean avtive);
}
