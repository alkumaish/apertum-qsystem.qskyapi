/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.apertum.qsky.ejb;

import javax.ejb.Local;
import org.hibernate.SessionFactory;
import org.hibernate.Session;

/**
 * Доступ до сессии Hibernate.
 * @author egorov
 */
@Local
public interface IHibernateEJBLocal {

    /**
     * Фабрика сессий.
     * @return
     */
    public SessionFactory getSessionFactory();

    /**
     * Открывается новыя сессия. Сессия управляется программно.
     * @return открытая новая сессии для использования
     */
    public Session openSession();

    /**
     * Текущая скессия, вариант использования: одна транзакция - одна сессия.
     * Автоматическое управление.
     * @return
     */
    public Session getCurrentSession();

    /**
     * Текущая скессия, вариант использования: одна транзакция - одна сессия
     * Автоматическое управление.
     * Вариант для коротко записи
     * @return
     */
    public Session cs();
}
