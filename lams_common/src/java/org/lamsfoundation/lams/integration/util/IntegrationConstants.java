/**
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
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
 */
package org.lamsfoundation.lams.integration.util;

public class IntegrationConstants {

    public static final String PARAM_USER_ID = "uid";

    public static final String PARAM_SERVER_ID = "sid";

    public static final String PARAM_TIMESTAMP = "ts";

    public static final String PARAM_HASH = "hash";

    public static final String PARAM_METHOD = "method";

    public static final String PARAM_COURSE_ID = "courseid";

    public static final String PARAM_COUNTRY = "country";

    public static final String PARAM_LANGUAGE = "lang";

    public static final String PARAM_IS_UPDATE_USER_DETAILS = "isUpdateUserDetails";

    public static final String PARAM_FIRST_NAME = "firstName";

    public static final String PARAM_LAST_NAME = "lastName";

    public static final String PARAM_EMAIL = "email";

    public static final String PARAM_CUSTOM_CSV = "customCSV";

    public static final String PARAM_EXT_LMS_ID = "extlmsid";

    public static final String PARAM_MODE = "mode";

    public static final String MODE_GRADEBOOK = "gradebook";

    public static final String METHOD_AUTHOR = "author";

    public static final String METHOD_MONITOR = "monitor";

    public static final String METHOD_LEARNER = "learner";

    // the same as METHOD_LEARNER but additionally requires hash to contain lsId in order to prevent users tampering
    // with lesson id parameter
    public static final String METHOD_LEARNER_STRICT_AUTHENTICATION = "learnerStrictAuth";

    public static final String PARAM_LEARNING_DESIGN_ID = "ldId";

    public static final String PARAM_LESSON_ID = "lsid";

}
