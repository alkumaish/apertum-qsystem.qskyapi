/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.apertum.qsky.ejb;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.Session;

/**
 *
 * @author egorov
 */
@Singleton(mappedName = "ejb/qskyapi/hibernate_session_factory", name = "qskyapi/HibernateEJB")
public class HibernateEJB implements IHibernateEJBLocal {
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    private SessionFactory sessionFactory;

    @PostConstruct
    private void buildHibernateSessionFactory() {
        try {
            // Create the SessionFactory from standard (hibernate.cfg.xml)
            // config file.
            sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            // Log the exception.
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    @PreDestroy
    private void closeHibernateSessionFactory() {
        sessionFactory.close();
    }

    @Override
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    @Override
    public Session openSession() {
        return sessionFactory.openSession();
    }

    @Override
    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public Session cs() {
        return sessionFactory.getCurrentSession();
    }
}
