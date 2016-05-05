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
/* $$Id$$ */
package org.lamsfoundation.lams.web;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.lamsfoundation.lams.web.util.HttpSessionManager;

/**
 * Listens for the creation and destruction of http sessions, and reports back to
 * the ClientSessionInfoManager.
 *
 * @web.listener
 */
/*
 * Should come out in web.xml as:
 * <!-- Listeners -->
 * <listener>
 * <listener-class>
 * org.lamsfoundation.lams.web.SessionListener
 * </listener-class>
 * </listener>
 */
public class SessionListener implements HttpSessionListener {

    /** HttpSessionListener interface */
    @Override
    public void sessionCreated(HttpSessionEvent se) {
	HttpSessionManager.getInstance().sessionCreated(se);
    }

    /** HttpSessionListener interface */
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
	//nothing to do
    }

}
