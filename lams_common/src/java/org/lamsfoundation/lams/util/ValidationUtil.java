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
package org.lamsfoundation.lams.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility methods for String validation.
 */
public class ValidationUtil {

    private final static String REGEX_USER_NAME = "^[^<>^!#&()/\\|'\"?,.:{}= ~`*@%$]*$";
    
    private final static String REGEX_FIRST_LAST_NAME = "^[\\p{L}]++(?:[' -][\\p{L}]++)*+\\.?$";
    
    private final static String REGEX_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    
    private final static String REGEX_ORGANISATION_NAME = "^[^<>^*@%$]*$";
    
    /**
     * Checks whether supplied username is valid. Username can only contain alphanumeric characters and no spaces.
     * 
     * @param Username
     * @return
     */
    public static boolean isUserNameValid(String userName) {
	
	boolean isUserNameValid = ValidationUtil.isRegexMatches(REGEX_USER_NAME,userName);
	return isUserNameValid;
    }

    /**
     * Checks whether supplied name is valid, which should only contain letters and the following characters ''',' ','-' 
     * 
     * @param name
     * @return
     */
    public static boolean isFirstLastNameValid(String name) {
	
	boolean isNameValid = ValidationUtil.isRegexMatches(REGEX_FIRST_LAST_NAME, name);
	return isNameValid;
    }

    /**
     * Checks whether supplied email address is valid.
     * 
     * @param email
     * @return
     */
    public static boolean isEmailValid(String email) {
	
	boolean isEmailValid = ValidationUtil.isRegexMatches(REGEX_EMAIL, email);
	return isEmailValid;
    }
    
    /**
     * Checks whether supplied string is valid. Name cannot contain any of these characters < > ^ * @ % $
     * 
     * @param orgName
     * @return
     */
    public static boolean isOrgNameValid(String orgName) {
	
	boolean isOrgNameValid = ValidationUtil.isRegexMatches(REGEX_ORGANISATION_NAME, orgName);
	return isOrgNameValid;
    }
    
    /**
     * Checks whether regex matches the input string.
     * 
     * @param regex
     * @param input
     * @return
     */
    private static boolean isRegexMatches(final String regex, final String input) {
	
	if (input == null) {
	    return true;
	}
	
	Pattern p = Pattern.compile(regex);
	Matcher m = p.matcher(input.trim());
	return m.matches();
    }
}
