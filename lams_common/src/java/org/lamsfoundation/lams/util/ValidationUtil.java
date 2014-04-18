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

    private final static Pattern REGEX_USER_NAME = Pattern.compile("^[^<>^!#&()/\\|\"?,:{}= ~`*%$]*$");

    private final static Pattern REGEX_FIRST_LAST_NAME = Pattern.compile("^[\\p{L}]++(?:[' -][\\p{L}]++)*+\\.?$");

    private final static Pattern REGEX_EMAIL = Pattern
	    .compile("^[_A-Za-z0-9-\\+']+(\\.[_A-Za-z0-9-']+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

    private final static Pattern REGEX_ORGANISATION_NAME = Pattern.compile("^[^<>^*@%$]*$");

    /**
     * Checks whether supplied username is valid. Username can only contain alphanumeric characters and no spaces.
     */
    public static boolean isUserNameValid(String userName) {
	
	boolean isValidationRequired = Configuration.getAsBoolean(ConfigurationKeys.USER_VALIDATION_REQUIRED_USERNAME);
	if (isValidationRequired) {
	    return ValidationUtil.isRegexMatches(ValidationUtil.REGEX_USER_NAME, userName);
	}
	return true;
    }

    /**
     * Checks whether supplied name is valid, which should only contain letters and the following characters ''',' ','-'
     * 
     * @param name
     * @return
     */
    public static boolean isFirstLastNameValid(String name) {
	
	boolean isValidationRequired = Configuration.getAsBoolean(ConfigurationKeys.USER_VALIDATION_REQUIRED_FIRST_LAST_NAME);
	if (isValidationRequired) {
	    return ValidationUtil.isRegexMatches(ValidationUtil.REGEX_FIRST_LAST_NAME, name);
	}
	return true;
    }

    /**
     * Checks whether supplied email address is valid.
     */
    public static boolean isEmailValid(String email) {
	
	boolean isValidationRequired = Configuration.getAsBoolean(ConfigurationKeys.USER_VALIDATION_REQUIRED_EMAIL);
	if (isValidationRequired) {
	    return ValidationUtil.isRegexMatches(ValidationUtil.REGEX_EMAIL, email);
	}
	return true;
    }

    /**
     * Checks whether supplied string is valid. Name cannot contain any of these characters < > ^ * @ % $
     */
    public static boolean isOrgNameValid(String orgName) {
	boolean isOrgNameValid = ValidationUtil.isRegexMatches(ValidationUtil.REGEX_ORGANISATION_NAME, orgName);
	return isOrgNameValid;
    }

    /**
     * Checks whether regex matches the input string.
     */
    private static boolean isRegexMatches(Pattern pattern, String input) {
	if (input == null) {
	    return true;
	}

	Matcher m = pattern.matcher(input.trim());
	return m.matches();
    }
}
