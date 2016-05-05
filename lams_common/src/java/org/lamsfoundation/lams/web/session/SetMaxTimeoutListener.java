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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */
package org.lamsfoundation.lams.web.session;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;

/**
 * @author jliew
 *
 */
public class SetMaxTimeoutListener implements HttpSessionListener {

    private static Logger log = Logger.getLogger(SetMaxTimeoutListener.class);

    @Override
    public void sessionCreated(HttpSessionEvent se) {
	try {
	    int timeout = Configuration.getAsInt(ConfigurationKeys.INACTIVE_TIME);
	    se.getSession().setMaxInactiveInterval(timeout);
	} catch (Exception e) {
	    log.error("Couldn't set max inactive interval due to exception, ", e);
	}
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
	//nothing to do
    }
}
