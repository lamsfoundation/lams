/*
 * Created on 2005-1-19
 *
 * Last modified on 2005-1-19
 */
package org.lamsfoundation.lams.admin.util;

import java.io.IOException;

import org.apache.commons.fileupload.FileItem;

import org.lamsfoundation.lams.usermanagement.Organisation;

/**
 * TODO Add description here
 *
 * <p>
 * <a href="IUserImportFileParser.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public interface IUserImportFileParser {

	public static final String STATUS_DISABLED = "disabled";
	public static final String SEPERATOR = "|";
	public static final String NO_AUTHORISATION =
		"<br>You are not authorised to select the user!";
	public static final String ROW = "<br>Row ";
	public static final String FAIL_ADD = ": Failed to add ";
	public static final String SUCCESS_ADD =
		" user(s) have been added successfully!";
	public static final String SKIP = ": Skipped. Error was: ";
	public static final String USER_NOT_EXIST = " does not exist! ";
	public static final String MEMBERSHIP_EXIST = " is already a member of the current org: ";

	public static final String LOGIN_REQUIRED = "Login is required.";
	public static final String PASSWORD_REQUIRED = "Password is required.";
	public static final String AUTH_METHOD_REQUIRED = "Authentication method is required.";
	public static final String ROLE_REQUIRED = "Role is required.";
	
	public String parseUsersInOrganisation(FileItem fileItem, Organisation org, String adminLogin, boolean existingUsersOnly) throws IOException;
	
}
