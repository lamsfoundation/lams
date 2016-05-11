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

package org.lamsfoundation.lams.util.wddx;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

/**
 * @author Manpreet Minhas
 */
public class HashtableUtils {

    /*
     * Takes a string (possibly null) and returns a not null string
     */
    public static String getValue(String possValue) {
	return (possValue == null ? "" : possValue);
    }

    /*
     * Takes a Long (possibly null) and returns a not null Long
     */
    public static Long getIdLong(Long possValue) {
	return (possValue == null ? WDDXTAGS.NUMERIC_NULL_VALUE_LONG : possValue);
    }

    /*
     * Takes a Integer (possibly null) and returns a not null Integer
     */
    public static Integer getIdInteger(Integer possValue) {
	return (possValue == null ? WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER : possValue);
    }

    public static Date getIdDate(Date possValue) {
	return (possValue == null ? WDDXTAGS.DATE_NULL_VALUE : possValue);
    }

    public static Boolean getBoolean(Boolean possValue) {
	return (possValue == null ? WDDXTAGS.BOOLEAN_NULL_VALUE : possValue);
    }

    /**
     * Helper function to retrieve dataset from Hashtable. As it is possible to
     * receive object array inside a hashtable. Simply cast it to Collection
     * will generate ClassCasting Exception. Therefore, we need to create this
     * helper function.
     *
     * @param clientObj
     * @param questionSet
     * @return return vector of data
     * @author Jacky Fang
     */
    public static Vector getCollectionDataFromHashTable(String identifier, Hashtable clientObj) {
	Vector dataSet = new Vector();
	if (clientObj.get(identifier) instanceof Collection) {
	    dataSet = (Vector) clientObj.get(identifier);
	} else if (clientObj.get(identifier) != null) {
	    dataSet.addAll(Arrays.asList((Object[]) clientObj.get(identifier)));
	}
	return dataSet;
    }

}
