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


package org.lamsfoundation.lams.tool.assessment.util;

import org.lamsfoundation.lams.tool.assessment.AssessmentConstants;

/**
 * Contains helper methods used by the Action Servlets
 *
 * @author Anthony Sukkar
 *
 */
public class AssessmentWebUtils {

    /**
     * If there is not url prefix, such as http://, https:// or ftp:// etc, this method will add default url protocol.
     *
     * @param url
     * @return
     */
    public static String protocol(String url) {
	if (url == null) {
	    return "";
	}

	if (!url.matches("^" + AssessmentConstants.ALLOW_PROTOCOL_REFIX + ".*")) {
	    url = AssessmentConstants.DEFUALT_PROTOCOL_REFIX + url;
	}

	return url;
    }

}
