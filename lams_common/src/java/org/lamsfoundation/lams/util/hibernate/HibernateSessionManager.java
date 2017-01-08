package org.lamsfoundation.lams.util.hibernate;

import org.hibernate.SessionFactory;
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
     * Puts a Hibernate session into the thread. Useful when thread missed the servlet filters but needs access to DB.
     */
    public static void bindHibernateSessionToCurrentThread(boolean recreate) {
	return;
	
// disabled to stop progress mayhem LDEV-4187
//	SessionFactory sessionFactory = HibernateSessionManager.getSessionFactory();
//	// is there a session bound to the current thread already?
//	Object session = TransactionSynchronizationManager.getResource(sessionFactory);
//	if (session != null) {
//	    if (recreate) {
//		TransactionSynchronizationManager.unbindResource(sessionFactory);
//	    } else {
//		return;
//	    }
//	}
//
//	SessionHolder sessionHolder = new SessionHolder(sessionFactory.openSession());
//	TransactionSynchronizationManager.bindResource(sessionFactory, sessionHolder);
    }

//    private static SessionFactory getSessionFactory() {
//	if (HibernateSessionManager.sessionFactory == null) {
//	    WebApplicationContext wac = WebApplicationContextUtils
//		    .getRequiredWebApplicationContext(SessionManager.getServletContext());
//	    HibernateSessionManager.sessionFactory = (SessionFactory) wac.getBean("coreSessionFactory");
//	}
//	return HibernateSessionManager.sessionFactory;
//    }
}