/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.apertum.qsky.webservices;

import javax.ejb.EJB;
import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import ru.apertum.qsky.api.ICustomerEvents;

/**
 *
 * @author Evgeniy Egorov
 */
// QSkyAPI  listening at address at http://<server_address>:8080/<serviceName>/<name>
@WebService(name = "qskyapi/CustomerEventsWS", serviceName = "customer_events", portName = "qsky")
public class CustomerEventsWS {
    @EJB
    private ICustomerEvents ejbRef;// Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Web Service Operation")

    @WebMethod(operationName = "standInService")
    @Oneway
    public void standInService(@WebParam(name = "branchId") Long branchId, @WebParam(name = "serviceId") Long serviceId, @WebParam(name = "customerId") Long customerId, @WebParam(name = "number") Integer number, @WebParam(name = "prefix") String prefix) {
        ejbRef.standInService(branchId, serviceId, customerId, number, prefix);
    }

    @WebMethod(operationName = "kickCustomer")
    @Oneway
    public void kickCustomer(@WebParam(name = "branchId") Long branchId, @WebParam(name = "serviceId") Long serviceId, @WebParam(name = "customerId") Long customerId, @WebParam(name = "employeeId") Long employeeId) {
        ejbRef.kickCustomer(branchId, serviceId, customerId, employeeId);
    }

    @WebMethod(operationName = "inviteCustomer")
    @Oneway
    public void inviteCustomer(@WebParam(name = "branchId") Long branchId, @WebParam(name = "customerId") Long customerId, @WebParam(name = "serviceId") Long serviceId, @WebParam(name = "employeeId") Long employeeId) {
        ejbRef.inviteCustomer(branchId, customerId, serviceId, employeeId);
    }

    @WebMethod(operationName = "inviteSecondary")
    @Oneway
    public void inviteSecondary(@WebParam(name = "branchId") Long branchId, @WebParam(name = "customerId") Long customerId, @WebParam(name = "serviceId") Long serviceId, @WebParam(name = "employeeId") Long employeeId) {
        ejbRef.inviteSecondary(branchId, customerId, serviceId, employeeId);
    }

    @WebMethod(operationName = "startWorkWithCustomer")
    @Oneway
    public void startWorkWithCustomer(@WebParam(name = "branchId") Long branchId, @WebParam(name = "customerId") Long customerId, @WebParam(name = "serviceId") Long serviceId, @WebParam(name = "employeeId") Long employeeId) {
        ejbRef.startWorkWithCustomer(branchId, customerId, serviceId, employeeId);
    }

    @WebMethod(operationName = "customerToPostponed")
    @Oneway
    public void customerToPostponed(@WebParam(name = "branchId") Long branchId, @WebParam(name = "customerId") Long customerId, @WebParam(name = "employeeId") Long employeeId) {
        ejbRef.customerToPostponed(branchId, customerId, employeeId);
    }

    @WebMethod(operationName = "redirectCustomer")
    @Oneway
    public void redirectCustomer(@WebParam(name = "branchId") Long branchId, @WebParam(name = "customerId") Long customerId, @WebParam(name = "employeeId") Long employeeId, @WebParam(name = "serviceId") Long serviceId, @WebParam(name = "newServiceId") Long newServiceId) {
        ejbRef.redirectCustomer(branchId, customerId, employeeId, serviceId, newServiceId);
    }

    @WebMethod(operationName = "finishWorkWithCustomer")
    @Oneway
    public void finishWorkWithCustomer(@WebParam(name = "branchId") Long branchId, @WebParam(name = "customerId") Long customerId, @WebParam(name = "employeeId") Long employeeId) {
        ejbRef.finishWorkWithCustomer(branchId, customerId, employeeId);
    }

    @WebMethod(operationName = "startWorkSecondary")
    @Oneway
    public void startWorkSecondary(@WebParam(name = "branchId") Long branchId, @WebParam(name = "customerId") Long customerId, @WebParam(name = "serviceId") Long serviceId, @WebParam(name = "employeeId") Long employeeId) {
        ejbRef.startWorkSecondary(branchId, customerId, serviceId, employeeId);
    }

    @WebMethod(operationName = "backInService")
    @Oneway
    public void backInService(@WebParam(name = "branchId") Long branchId, @WebParam(name = "customerId") Long customerId, @WebParam(name = "employeeId") Long employeeId, @WebParam(name = "serviceId") Long serviceId, @WebParam(name = "newServiceId") Long newServiceId) {
        ejbRef.backInService(branchId, customerId, employeeId, serviceId, newServiceId);
    }

    @WebMethod(operationName = "insertCustomer")
    @Oneway
    public void insertCustomer(@WebParam(name = "branchId") Long branchId, @WebParam(name = "serviceId") Long serviceId, @WebParam(name = "customerId") Long customerId, @WebParam(name = "beforeCustId") Long beforeCustId, @WebParam(name = "afterCustId") Long afterCustId) {
        ejbRef.insertCustomer(branchId, serviceId, customerId, beforeCustId, afterCustId);
    }

    @WebMethod(operationName = "removeCustomer")
    @Oneway
    public void removeCustomer(@WebParam(name = "branchId") Long branchId, @WebParam(name = "serviceId") Long serviceId, @WebParam(name = "customerId") Long customerId) {
        ejbRef.removeCustomer(branchId, serviceId, customerId);
    }

    @WebMethod(operationName = "ping")
    public Integer ping(@WebParam(name = "version") String version) {
        return ejbRef.ping(version);
    }

    @WebMethod(operationName = "sendServiceName")
    @Oneway
    public void sendServiceName(@WebParam(name = "branchId") Long branchId, @WebParam(name = "serviceId") Long serviceId, @WebParam(name = "name") String name) {
        ejbRef.sendServiceName(branchId, serviceId, name);
    }

    @WebMethod(operationName = "sendUserName")
    @Oneway
    public void sendUserName(@WebParam(name = "branchId") Long branchId, @WebParam(name = "employeeId") Long employeeId, @WebParam(name = "name") String name) {
        ejbRef.sendUserName(branchId, employeeId, name);
    }
}
