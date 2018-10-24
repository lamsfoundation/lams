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

package org.lamsfoundation.lams.web.filter;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * HttpRequestWrapper overriding methods getLocale(), getLocales() to include
 * the user's preferred locale.
 */
public class LocaleRequestWrapper extends HttpServletRequestWrapper {

    private final transient Log log = LogFactory.getLog(LocaleRequestWrapper.class);
    private final Locale preferredLocale;

    public LocaleRequestWrapper(HttpServletRequest decorated, Locale userLocale) {
	super(decorated);
	preferredLocale = userLocale;
	if (null == preferredLocale) {
	    log.error("preferred locale = null, it is an unexpected value!");
	}
    }

    /**
     * @see javax.servlet.ServletRequestWrapper#getLocale()
     */
    @Override
    public Locale getLocale() {
	if (null != preferredLocale) {
	    return preferredLocale;
	} else {
	    return super.getLocale();
	}
    }

    /**
     * @see javax.servlet.ServletRequestWrapper#getLocales()
     */
    @Override
    public Enumeration getLocales() {
	if (null != preferredLocale) {
	    List list = Collections.list(super.getLocales());
	    if (list.contains(preferredLocale)) {
		list.remove(preferredLocale);
	    }
	    list.add(0, preferredLocale);
	    return Collections.enumeration(list);
	} else {
	    return super.getLocales();
	}
    }

}
