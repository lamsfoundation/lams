/***************************************************************************
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
 * ************************************************************************
 */

package org.lamsfoundation.lams.usermanagement.util;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Comparator;

import org.apache.log4j.Logger;

/**
 * Compare two objects using alphabetical comparison by Last Name, First Name
 * and Login in that order. The object must have methods getLastName,
 * getFirstName, getLogin.
 *
 * @author Anthony Sukkar
 *
 */
public class LastNameAlphabeticComparator implements Comparator, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 4294765895924263296L;

    private static Logger log = Logger.getLogger(LastNameAlphabeticComparator.class);

    /**
     * @see java.util.Comparator#compare(Object, Object)
     */
    @Override
    public int compare(Object o1, Object o2) {
	if (o1 == null || o2 == null) {
	    return compareNull(o1, o2);
	} else {
	    int retValue = compareLastName(o1, o2);
	    if (retValue == 0) {
		retValue = compareFirstName(o1, o2);
	    }
	    if (retValue == 0) {
		retValue = compareLogin(o1, o2);
	    }
	    return retValue;
	}
    }

    private int compareLastName(Object o1, Object o2) {
	String surname1 = null;
	String surname2 = null;

	try {

	    Method lastNameMethod;
	    lastNameMethod = o1.getClass().getMethod("getLastName", (Class[]) null);
	    surname1 = (String) lastNameMethod.invoke(o1, (Object[]) null);

	    lastNameMethod = o2.getClass().getMethod("getLastName", (Class[]) null);
	    surname2 = (String) lastNameMethod.invoke(o2, (Object[]) null);

	} catch (Exception e) {
	    log.warn("Compare threw ", e);
	}

	int retValue = -1;
	if (surname1 == null || surname2 == null) {
	    retValue = compareNull(surname1, surname2);
	} else {
	    retValue = surname1.compareTo(surname2);
	}
	return retValue;
    }

    private int compareFirstName(Object o1, Object o2) {
	String name1 = null;
	String name2 = null;

	try {

	    Method lastNameMethod;

	    lastNameMethod = o1.getClass().getMethod("getFirstName", (Class[]) null);
	    name1 = (String) lastNameMethod.invoke(o1, (Object[]) null);

	    lastNameMethod = o2.getClass().getMethod("getFirstName", (Class[]) null);
	    name2 = (String) lastNameMethod.invoke(o2, (Object[]) null);

	} catch (Exception e) {
	    log.warn("Compare threw ", e);
	}

	int retValue = -1;
	if (name1 == null || name2 == null) {
	    retValue = compareNull(name1, name2);
	} else {
	    retValue = name1.compareTo(name2);
	}
	return retValue;
    }

    private int compareLogin(Object o1, Object o2) {
	String name1 = null;
	String name2 = null;

	try {

	    Method lastNameMethod;

	    lastNameMethod = o1.getClass().getMethod("getLogin", (Class[]) null);
	    name1 = (String) lastNameMethod.invoke(o1, (Object[]) null);

	    lastNameMethod = o2.getClass().getMethod("getLogin", (Class[]) null);
	    name2 = (String) lastNameMethod.invoke(o2, (Object[]) null);

	} catch (Exception e) {
	    log.warn("Compare threw ", e);
	}

	int retValue = -1;
	if (name1 == null || name2 == null) {
	    retValue = compareNull(name1, name2);
	} else {
	    retValue = name1.compareTo(name2);
	}
	return retValue;
    }

    private int compareNull(Object o1, Object o2) {
	if (o1 == null) {
	    return (o2 != null ? -1 : 0);
	} else {
	    return 1;
	}
    }
}
