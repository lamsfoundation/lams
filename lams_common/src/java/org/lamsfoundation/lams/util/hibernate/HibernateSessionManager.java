package org.lamsfoundation.lams.util.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.context.internal.ManagedSessionContext;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.springframework.orm.hibernate4.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Helper for Hibernate sessions.
 *
 * @author Marcin Cieslak
 */
public class HibernateSessionManager {
    private static SessionFactory sessionFactory;

    /**
     * Makes sure that an open Hibernate session is bound to current thread.
     */
    public static void openSession() {
	SessionFactory sessionFactory = HibernateSessionManager.getSessionFactory();
	// this call does not only fetch current session
	// if an open session is missing from Context, it creates it and binds it
	// with TransactionAwareSessionContext#currentSession()
	Session session = sessionFactory.getCurrentSession();
	if (!session.isOpen()) {
	    ManagedSessionContext.unbind(sessionFactory);
	    TransactionSynchronizationManager.unbindResourceIfPossible(sessionFactory);
	    session = sessionFactory.getCurrentSession();
	}

	// binding to Context is not enough
	// an open session needs to be also manually bound to current thread
	SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager.getResource(sessionFactory);
	if (sessionHolder == null) {
	    sessionHolder = new SessionHolder(session);
	    TransactionSynchronizationManager.bindResource(sessionFactory, sessionHolder);
	}
    }

    public static void closeSession() {
	Session session = HibernateSessionManager.getSessionFactory().getCurrentSession();
	if (session.isOpen()) {
	    session.close();
	}
    }

    private static SessionFactory getSessionFactory() {
	if (HibernateSessionManager.sessionFactory == null) {
	    WebApplicationContext wac = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(SessionManager.getServletContext());
	    HibernateSessionManager.sessionFactory = (SessionFactory) wac.getBean("coreSessionFactory");
	}
	return HibernateSessionManager.sessionFactory;
    }
}