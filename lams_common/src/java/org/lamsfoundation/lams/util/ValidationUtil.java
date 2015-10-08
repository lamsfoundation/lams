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

import org.apache.commons.lang.StringUtils;

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
     * Checks whether supplied email address is valid. It validates email only if USER_VALIDATION_REQUIRED_EMAIL LAMS configuration is ON.
     */
    public static boolean isEmailValid(String email) {
	return isEmailValid(email, true);
    }
    
    /**
     * Checks whether supplied email address is valid.
     * 
     * @param email supplied email
     * @param checkConfiguration whether it should take into account LAMS server configuration setting USER_VALIDATION_REQUIRED_EMAIL
     * @return
     */
    public static boolean isEmailValid(String email, boolean checkConfiguration) {
	
	boolean isValidationRequired = Configuration.getAsBoolean(ConfigurationKeys.USER_VALIDATION_REQUIRED_EMAIL);
	if (!checkConfiguration || checkConfiguration && isValidationRequired) {
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
    
    /**
     * Checks whether min words limit is reached
     * 
     * @param text
     * @param minWordsLimit
     * @param isDerivedFromCKEditor whether the text was acquired from CKEditor
     * @return
     */
    public static boolean isMinWordsLimitReached(String text, int minWordsLimit, boolean isDerivedFromCKEditor) {
	
	if (minWordsLimit <= 0) {
	    return true;
	} else if (StringUtils.isBlank(text)) {
	    return false;
	}

	// HTML tags stripping
	if (isDerivedFromCKEditor) {
	    text = WebUtil.removeHTMLtags(text); // text.replaceAll("/<\/?[a-z][^>]*>/gi", '');
	} else {
	    text = text.replaceAll("(?:<BR>)", " ");
	}
	    
	int wordCount = (text.length() == 0) ? 0 : text.replaceAll("[\'\";:,\\.\\?\\-!]+", "").trim().split("\\S+").length;//.match(/\S+/g) || []) ;

	// check min words limit is reached
	return (wordCount >= minWordsLimit);
    }
}
