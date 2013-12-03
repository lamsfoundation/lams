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
package org.lamsfoundation.lams.util;

import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;

/**
 * Helper for JSON objects.
 * 
 * @author Marcin Cieslak
 * 
 */
public class JsonUtil {

    /**
     * Checks if given key exists in JSON object and returns defaultValue otherwise. Native JSONObject.opt() method does
     * not check for JSONNull value, so it had to be rewritten.
     */
    @SuppressWarnings("unchecked")
    public static <T> T opt(JSONObject object, String field, T defaultValue) throws JSONException {
	return object.isNull(field) ? defaultValue : (T) object.get(field);
    }

    /**
     * Checks if given key exists in JSON object and returns defaultValue otherwise. Native JSONObject.opt() method does
     * not check for JSONNull value, so it had to be rewritten.
     */
    public static Object opt(JSONObject object, String field) throws JSONException {
	return object.isNull(field) ? null : object.get(field);
    }

    /**
     * Checks if long value for given key exists in JSON object and returns null otherwise. This special method prevents
     * exception when casting Integer (JSON default type for numbers) to Long. Native JSONObject.opt() method does not
     * check for JSONNull value, so it had to be rewritten.
     */
    public static Long optLong(JSONObject object, String field) throws JSONException {
	Object value = object.isNull(field) ? null : object.get(field);
	if (value != null) {
	    if (value instanceof Number) {
		value = ((Number) value).longValue();
	    } else {
		value = new Long((String) value);
	    }
	}

	return (Long) value;
    }
}