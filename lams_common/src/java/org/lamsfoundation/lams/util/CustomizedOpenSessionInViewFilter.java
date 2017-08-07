/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.util;

import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.hibernate5.SessionFactoryUtils;
import org.springframework.orm.hibernate5.support.OpenSessionInViewFilter;

/**
 *
 * @author Jacky Fang
 * @since 2005-4-4
 * @version
 *
 */
public class CustomizedOpenSessionInViewFilter extends OpenSessionInViewFilter {

    /**
     * Get a Session for the SessionFactory that this filter uses.
     * Note that this just applies in single session mode!
     * <p>
     * The default implementation delegates to SessionFactoryUtils'
     * getSession method and sets the Session's flushMode to NEVER.
     * <p>
     * Can be overridden in subclasses for creating a Session with a custom
     * entity interceptor or JDBC exception translator.
     * 
     * @param sessionFactory
     *            the SessionFactory that this filter uses
     * @return the Session to use
     * @throws DataAccessResourceFailureException
     *             if the Session could not be created
     * @see org.springframework.orm.hibernate5.SessionFactoryUtils#getSession(SessionFactory, boolean)
     * @see org.hibernate.FlushMode#NEVER
     */
    @Override
    protected Session openSession(SessionFactory sessionFactory) throws DataAccessResourceFailureException {
	try {
	    Session session = sessionFactory.openSession();
	    session.setFlushMode(FlushMode.AUTO);
	    return session;
	} catch (HibernateException ex) {
	    throw new DataAccessResourceFailureException("Could not open Hibernate Session", ex);
	}
    }

    /**
     * Close the given Session.
     * Note that this just applies in single session mode!
     * <p>
     * The default implementation delegates to SessionFactoryUtils'
     * closeSessionIfNecessary method.
     * <p>
     * Can be overridden in subclasses, e.g. for flushing the Session before
     * closing it. See class-level javadoc for a discussion of flush handling.
     * Note that you should also override getSession accordingly, to set
     * the flush mode to something else than NEVER.
     * 
     * @param session
     *            the Session used for filtering
     * @param sessionFactory
     *            the SessionFactory that this filter uses
     */
    protected void closeSession(Session session) {
	session.flush();
	SessionFactoryUtils.closeSession(session);
    }
}
