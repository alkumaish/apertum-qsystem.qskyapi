/*
 *  Copyright (C) 2010 {Apertum}Projects. web: www.apertum.ru email: info@apertum.ru
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ru.apertum.qsky.ejb;

import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import org.hibernate.criterion.Property;
import ru.apertum.qsky.api.ICustomerEvents;
import ru.apertum.qsky.common.ServerProps;
import ru.apertum.qsky.model.Customer;
import ru.apertum.qsky.model.Employee;
import ru.apertum.qsky.model.Service;
import ru.apertum.qsky.model.Step;

/**
 *
 * @author egorov
 */
@Singleton(mappedName = "ejb/qskyapi/customer_events", name = "qskyapi/CustomerEventsEJB")
//@Local(ICustomerEvents.class)
public class CustomerEventsEJB implements ICustomerEvents {

    @EJB(mappedName = "ejb/qskyapi/hibernate_session_factory")
    private IHibernateEJBLocal hib;
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public synchronized void standInService(Long branchId, Long serviceId, Long customerId, Integer number, String prefix) {
        System.out.println("Start standInService");
        hib.cs().beginTransaction();
        Customer customer = getCustomer(branchId, customerId);
        if (customer == null) {
            customer = new Customer(branchId, customerId);
        }
        if (serviceId != null && serviceId > 0) {
            customer.setServiceId(serviceId);
        }
        customer.setNumber(number);
        customer.setPrefix(prefix);
        customer.setState(Customer.States.NEWBIE);

        final Step firstStep = new Step(branchId, customerId);
        firstStep.setServiceId(serviceId);
        firstStep.setStandTime(new Date());
        firstStep.setStartState(Customer.States.NEWBIE);
        customer.setFirstStep(firstStep);

        try {
            hib.cs().saveOrUpdate(firstStep);
            hib.cs().saveOrUpdate(customer);
            hib.cs().getTransaction().commit();
        } catch (Exception ex) {
            hib.cs().getTransaction().rollback();
        } finally {
        }
        System.out.println("Finish standInService");
    }

    @Override
    public synchronized void kickCustomer(Long branchId, Long serviceId, Long customerId, Long employeeId) {
        System.out.println("Start kickCustomer");
        hib.cs().beginTransaction();
        Customer customer = getCustomer(branchId, customerId);
        if (customer == null) {
            return;
        }
        customer.setState(Customer.States.REMOVED);
        try {
            if (customer.getFirstStep() != null) {
                final Step step = customer.getFirstStep().getLastStep();
                step.setFinishState(Customer.States.REMOVED);
                hib.cs().saveOrUpdate(step);
            }
            hib.cs().saveOrUpdate(customer);
            hib.cs().getTransaction().commit();
        } catch (Exception ex) {
            hib.cs().getTransaction().rollback();
        } finally {
        }
        System.out.println("Finish kickCustomer");
    }

    @Override
    public synchronized void inviteCustomer(Long branchId, Long customerId, Long serviceId, Long employeeId) {
        System.out.println("Invoke inviteCustomer");
    }

    @Override
    public synchronized void inviteSecondary(Long branchId, Long customerId, Long serviceId, Long employeeId) {
        System.out.println("Invoke inviteSecondary");
    }

    @Override
    public synchronized void startWorkWithCustomer(Long branchId, Long customerId, Long serviceId, Long employeeId) {
        System.out.println("Start startWorkWithCustomer");
        hib.cs().beginTransaction();
        Customer customer = getCustomer(branchId, customerId);
        if (customer == null) {
            System.out.println("ERROR: Customer not found id=" + customerId);
            return;
        }
        if (serviceId != null && serviceId > 0) {
            customer.setServiceId(serviceId);
        }
        customer.setState(Customer.States.WORK_FIRST);

        final Step step = customer.getFirstStep().getLastStep();
        step.setEmployeeId(employeeId);
        step.setServiceId(serviceId);
        //step.setStartState(Customer.States.WORK_FIRST);
        step.setStartTime(new Date());
        step.setWaiting(step.getStartTime().getTime() - step.getStandTime().getTime());
        customer.setWaiting((customer.getWaiting() * (customer.getFirstStep().getStepsCount() - 1) + step.getWaiting()) / customer.getFirstStep().getStepsCount());

        try {
            hib.cs().saveOrUpdate(step);
            hib.cs().saveOrUpdate(customer);
            hib.cs().getTransaction().commit();
        } catch (Exception ex) {
            hib.cs().getTransaction().rollback();
        } finally {
        }
        System.out.println("Finish startWorkWithCustomer");
    }

    @Override
    public synchronized void startWorkSecondary(Long branchId, Long customerId, Long serviceId, Long employeeId) {
        System.out.println("Start startWorkSecondary");
        hib.cs().beginTransaction();
        Customer customer = getCustomer(branchId, customerId);
        if (customer == null) {
            System.out.println("ERROR: Customer not found id=" + customerId);
            return;
        }
        if (serviceId != null && serviceId > 0) {
            customer.setServiceId(serviceId);
        }
        customer.setState(Customer.States.WORK_SECONDARY);

        final Step step = customer.getFirstStep().getLastStep();
        step.setEmployeeId(employeeId);
        step.setServiceId(serviceId);
        step.setStartTime(new Date());
        step.setWaiting(step.getStartTime().getTime() - step.getStandTime().getTime());
        customer.setWaiting((customer.getWaiting() * (customer.getFirstStep().getStepsCount() - 1) + step.getWaiting()) / customer.getFirstStep().getStepsCount());

        try {
            hib.cs().saveOrUpdate(step);
            hib.cs().saveOrUpdate(customer);
            hib.cs().getTransaction().commit();
        } catch (Exception ex) {
            hib.cs().getTransaction().rollback();
        } finally {
        }
        System.out.println("Finish startWorkSecondary");
    }

    @Override
    public synchronized void customerToPostponed(Long branchId, Long customerId, Long employeeId) {
        System.out.println("Start customerToPostponed");
        hib.cs().beginTransaction();
        Customer customer = getCustomer(branchId, customerId);
        if (customer == null) {
            System.out.println("ERROR: Customer not found id=" + customerId);
            return;
        }
        customer.setState(Customer.States.POSTPONED);

        final Step step = customer.getFirstStep().getLastStep();
        step.setEmployeeId(employeeId);
        step.setFinishTime(new Date());
        step.setFinishState(Customer.States.POSTPONED);
        step.setWorking(step.getFinishTime().getTime() - step.getStartTime().getTime());
        customer.setWorking((customer.getWorking() * (customer.getFirstStep().getStepsCount() - 1) + step.getWorking()) / customer.getFirstStep().getStepsCount());

        final Step postponedStep = new Step(branchId, customerId);
        postponedStep.setStandTime(new Date());
        postponedStep.setStartState(Customer.States.POSTPONED);
        step.setAfter(postponedStep);
        postponedStep.setBefore(step);

        try {
            hib.cs().saveOrUpdate(postponedStep);
            hib.cs().saveOrUpdate(step);
            hib.cs().saveOrUpdate(customer);
            hib.cs().getTransaction().commit();
        } catch (Exception ex) {
            hib.cs().getTransaction().rollback();
        } finally {
        }
        System.out.println("Finish customerToPostponed");
    }

    @Override
    public synchronized void redirectCustomer(Long branchId, Long customerId, Long employeeId, Long serviceId, Long newServiceId) {
        System.out.println("Start redirectCustomer");
        hib.cs().beginTransaction();
        Customer customer = getCustomer(branchId, customerId);
        if (customer == null) {
            System.out.println("ERROR: Customer not found id=" + customerId);
            return;
        }
        customer.setState(Customer.States.REDIRECTED);
        customer.setServiceId(newServiceId);

        final Step step = customer.getFirstStep().getLastStep();
        step.setEmployeeId(employeeId);
        step.setFinishTime(new Date());
        step.setFinishState(Customer.States.REDIRECTED);
        step.setWorking(step.getFinishTime().getTime() - step.getStartTime().getTime());
        customer.setWorking((customer.getWorking() * (customer.getFirstStep().getStepsCount() - 1) + step.getWorking()) / customer.getFirstStep().getStepsCount());

        final Step redirectedStep = new Step(branchId, customerId);
        redirectedStep.setStandTime(new Date());
        redirectedStep.setStartState(Customer.States.REDIRECTED);
        redirectedStep.setServiceId(newServiceId);
        step.setAfter(redirectedStep);
        redirectedStep.setBefore(step);

        try {
            hib.cs().saveOrUpdate(redirectedStep);
            hib.cs().saveOrUpdate(step);
            hib.cs().saveOrUpdate(customer);
            hib.cs().getTransaction().commit();
        } catch (Exception ex) {
            hib.cs().getTransaction().rollback();
        } finally {
        }
        System.out.println("Finish redirectCustomer");
    }

    @Override
    public synchronized void backInService(Long branchId, Long customerId, Long employeeId, Long serviceId, Long newServiceId) {
        System.out.println("Start backInService");
        hib.cs().beginTransaction();
        Customer customer = getCustomer(branchId, customerId);
        if (customer == null) {
            System.out.println("ERROR: Customer not found id=" + customerId);
            return;
        }
        customer.setState(Customer.States.BACK_AFTER_REEDIRECT);
        customer.setServiceId(newServiceId);

        final Step step = customer.getFirstStep().getLastStep();
        step.setEmployeeId(employeeId);
        step.setFinishTime(new Date());
        step.setFinishState(Customer.States.BACK_AFTER_REEDIRECT);
        step.setWorking(step.getFinishTime().getTime() - step.getStartTime().getTime());
        customer.setWorking((customer.getWorking() * (customer.getFirstStep().getStepsCount() - 1) + step.getWorking()) / customer.getFirstStep().getStepsCount());

        final Step redirectedStep = new Step(branchId, customerId);
        redirectedStep.setStandTime(new Date());
        redirectedStep.setStartState(Customer.States.BACK_AFTER_REEDIRECT);
        redirectedStep.setServiceId(newServiceId);
        step.setAfter(redirectedStep);
        redirectedStep.setBefore(step);

        try {
            hib.cs().saveOrUpdate(redirectedStep);
            hib.cs().saveOrUpdate(step);
            hib.cs().saveOrUpdate(customer);
            hib.cs().getTransaction().commit();
        } catch (Exception ex) {
            hib.cs().getTransaction().rollback();
        } finally {
        }
        System.out.println("Finish backInService");
    }

    @Override
    public synchronized void finishWorkWithCustomer(Long branchId, Long customerId, Long employeeId) {
        System.out.println("Start standInService");
        hib.cs().beginTransaction();
        Customer customer = getCustomer(branchId, customerId);
        if (customer == null) {
            System.out.println("ERROR: Customer not found id=" + customerId);
            return;
        }
        customer.setState(Customer.States.FINISHED);

        final Step step = customer.getFirstStep().getLastStep();
        step.setEmployeeId(employeeId);
        step.setFinishState(Customer.States.FINISHED);
        step.setFinishTime(new Date());
        step.setWorking(step.getFinishTime().getTime() - step.getStartTime().getTime());
        customer.setWorking((customer.getWorking() * (customer.getFirstStep().getStepsCount() - 1) + step.getWorking()) / customer.getFirstStep().getStepsCount());

        try {
            hib.cs().saveOrUpdate(step);
            hib.cs().saveOrUpdate(customer);
            hib.cs().getTransaction().commit();
        } catch (Exception ex) {
            hib.cs().getTransaction().rollback();
        } finally {
        }
        System.out.println("Finish standInService");
    }

    @Override
    public synchronized void insertCustomer(Long branchId, Long serviceId, Long customerId, Long beforeCustId, Long afterCustId) {
        System.out.println("Start insertCustomer");
        hib.cs().beginTransaction();

        Customer customer = getCustomer(branchId, customerId);
        if (customer == null) {
            customer = new Customer(branchId, customerId);
            if (serviceId != null && serviceId > 0) {
                customer.setServiceId(serviceId);
            }
        }
        final Customer before = getCustomer(branchId, beforeCustId);
        final Customer after = getCustomer(branchId, afterCustId);
        if (before != null) {
            before.setAfter(customer);
            customer.setBefore(before);
        }
        if (after != null) {
            after.setBefore(customer);
            customer.setAfter(after);
        }
        try {
            hib.cs().saveOrUpdate(customer);
            if (after != null) {
                hib.cs().saveOrUpdate(after);
            }
            if (before != null) {
                hib.cs().saveOrUpdate(before);
            }
            hib.cs().getTransaction().commit();
        } catch (Exception ex) {
            System.out.println("ex = " + ex);
            hib.cs().getTransaction().rollback();
        } finally {
        }
        System.out.println("Finish insertCustomer");
    }

    @Override
    public synchronized void removeCustomer(Long branchId, Long serviceId, Long customerId) {
        System.out.println("Start removeCustomer");
        hib.cs().beginTransaction();

        final Customer customer = getCustomer(branchId, customerId);
        if (customer == null) {
            return;
        }
        if (customer.getBefore() != null) {
            customer.getBefore().setAfter(customer.getAfter());
        }
        if (customer.getAfter() != null) {
            customer.getAfter().setBefore(customer.getBefore());
        }
        customer.setAfter(null);
        customer.setBefore(null);
        try {
            if (customer.getBefore() != null) {
                hib.cs().saveOrUpdate(customer.getBefore());
            }
            if (customer.getAfter() != null) {
                hib.cs().saveOrUpdate(customer.getAfter());
            }
            hib.cs().saveOrUpdate(customer);
            hib.cs().getTransaction().commit();
        } catch (Exception ex) {
            System.out.println("ex = " + ex);
            hib.cs().getTransaction().rollback();
        } finally {
        }
        System.out.println("Finish removeCustomer");
    }

    @Override
    public Integer ping(String version) {
        return ServerProps.getInstance().isSupportClient(version) ? 1 : 100500;
    }

    @Override
    public void sendServiceName(Long branchId, Long serviceId, String name) {
        System.out.println("Invoke sendServiceName");
        dataLock.lock();
        try {
            hib.cs().beginTransaction();
            Service service = getService(branchId, serviceId);
            if (service == null) {
                service = new Service(branchId, serviceId, name);
            }
            service.setName(name);
            hib.cs().saveOrUpdate(service);
            hib.cs().getTransaction().commit();
        } catch (Exception ex) {
            System.out.println("ex = " + ex);
            hib.cs().getTransaction().rollback();
        } finally {
            dataLock.unlock();
        }
    }
    private static final ReentrantLock dataLock = new ReentrantLock();

    @Override
    public void sendUserName(Long branchId, Long employeeId, String name) {
        System.out.println("Invoke sendUserName");
        dataLock.lock();
        try {
            hib.cs().beginTransaction();
            Employee employee = getEmployee(branchId, employeeId);
            if (employee == null) {
                employee = new Employee(branchId, employeeId, name);
            }
            employee.setName(name);
            hib.cs().saveOrUpdate(employee);
            hib.cs().getTransaction().commit();
        } catch (Exception ex) {
            System.out.println("ex = " + ex);
            hib.cs().getTransaction().rollback();
        } finally {
            dataLock.unlock();
        }
    }
    //*******************************************************************************************************

    private Customer getCustomer(Long branchId, Long customerId) {
        final List<Customer> list = hib.cs().createCriteria(Customer.class).add(Property.forName("branchId").eq(branchId)).add(Property.forName("customerId").eq(customerId)).list();
        return list.isEmpty() ? null : list.get(0);
    }

    private Employee getEmployee(Long branchId, Long employeeId) {
        final List<Employee> list = hib.cs().createCriteria(Employee.class).add(Property.forName("branchId").eq(branchId)).add(Property.forName("employeeId").eq(employeeId)).list();
        return list.isEmpty() ? null : list.get(0);
    }

    private Service getService(Long branchId, Long serviceId) {
        final List<Service> list = hib.cs().createCriteria(Service.class).add(Property.forName("branchId").eq(branchId)).add(Property.forName("serviceId").eq(serviceId)).list();
        return list.isEmpty() ? null : list.get(0);
    }
}

/*
// QSkyAPI  listening at address at http://<server_address>:8080/<serviceName>/<name>
@WebService(name = "qskyapi/CustomerEventsWS", serviceName="customer_events", portName="qsky")
public class CustomerEventsWS {

@EJB(mappedName = "ejb/qskyapi/customer_events")
private ICustomerEvents ejbRef;
 */
