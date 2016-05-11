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

package org.lamsfoundation.lams.usermanagement.util;

/**
 * <p>
 * <a href="IUserImportFileParser.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public interface IUserImportFileParser {

    public static final String STATUS_DISABLED = "disabled";
    public static final String SEPERATOR = "|";
    public static final String NO_AUTHORISATION = "<br>You are not authorised to select the user!";
    public static final String ROW = "<br>Row ";
    public static final String FAIL_ADD = ": Failed to add ";
    public static final String SUCCESS_ADD = " user(s) have been added successfully!";
    public static final String SKIP = ": Skipped. Error was: ";
    public static final String USER_NOT_EXIST = " does not exist! ";
    public static final String MEMBERSHIP_EXIST = " is already a member of the current org: ";

    public static final String LOGIN_REQUIRED = "Login is required.";
    public static final String PASSWORD_REQUIRED = "Password is required.";
    public static final String AUTH_METHOD_REQUIRED = "Authentication method is required.";
    public static final String ROLE_REQUIRED = "Role is required.";

    //public String parseUsersInOrganisation(FileItem fileItem, Organisation org, String adminLogin, boolean existingUsersOnly) throws IOException;

}
