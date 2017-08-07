package org.lamsfoundation.lams.util.hibernate;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.context.internal.ManagedSessionContext;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.springframework.orm.hibernate5.SessionHolder;
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

   //  private static Logger log = Logger.getLogger(HibernateSessionManager.class);
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
//	    log.debug("Opened new session "+session.toString());
//	} else {
//	    log.debug("Bound to open session "+session.toString());
//	}

	// binding to Context is not enough
	// an open session needs to be also manually bound to current thread
	SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager.getResource(sessionFactory);
	if (sessionHolder == null) {
	    sessionHolder = new SessionHolder(session);
	    TransactionSynchronizationManager.bindResource(sessionFactory, sessionHolder);
//	    log.debug("Linked to transaction "+session.getTransaction());
	}
    }

    public static void closeSession() {
	Session session = HibernateSessionManager.getSessionFactory().getCurrentSession();
	if (session.isOpen()) {
//	    log.debug("Closing session transaction "+session.getTransaction());
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