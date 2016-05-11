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

package org.lamsfoundation.lams.authoring.web;

/**
 * Constants used by Authoring.
 *
 * @author Fiona Malikoff
 */
public class AuthoringConstants {

    /** Spring context name for refering to the authoring service */
    public static final String AUTHORING_SERVICE_BEAN_NAME = "authoringService";
    public static final String TOOL_SERVICE_BEAN_NAME = "lamsToolService";

    //used by all tool authoring action class to mark the success flag.
    public static final String LAMS_AUTHORING_SUCCESS_FLAG = "LAMS_AUTHORING_SUCCESS_FLAG";

    // used for tool content folder creation.
    public static final String LAMS_WWW_FOLDER = "www/";
}
