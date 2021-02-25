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

package org.lamsfoundation.lams.util;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.usermanagement.User;

import com.codahale.passpol.BreachDatabase;
import com.codahale.passpol.PasswordPolicy;
import com.codahale.passpol.Status;

/**
 * Utility methods for String validation.
 */
public class ValidationUtil {

    private static Logger log = Logger.getLogger(ValidationUtil.class);

    private final static Pattern REGEX_USER_NAME = Pattern.compile("^[^<>^!#&()/\\|\"?,:{}= ~`*%$]*$");

    private final static Pattern REGEX_FIRST_LAST_NAME = Pattern.compile("^[\\p{L}]++(?:[' -][\\p{L}]++)*+\\.?$");

    private final static Pattern REGEX_EMAIL = Pattern
	    .compile("^[_A-Za-z0-9-\\+']+(\\.[_A-Za-z0-9-']+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

    private final static Pattern REGEX_ORGANISATION_NAME = Pattern.compile("^[^<>^*@%$]*$");

    private final static Pattern REGEX_PASSWORD_UPPER_CASE = Pattern.compile("[A-Z]");

    private final static Pattern REGEX_PASSWORD_LOWER_CASE = Pattern.compile("[a-z]");

    private final static Pattern REGEX_PASSWORD_NUMERICS = Pattern.compile("\\d");

    private final static Pattern REGEX_PASSWORD_SYMBOLS = Pattern
	    .compile("[`~!@#$%^&*\\(\\)_\\-+={}\\[\\]\\\\|:\\;\\\"\\'\\<\\>,.?\\/]");

    private final static Pattern REGEX_PASSWORD_CHARATERS_ALLOWED = Pattern
	    .compile("^[A-Za-z0-9\\d`~!@#$%^&*\\(\\)_\\-+={}\\[\\]\\\\|:\\;\\\"\\'\\<\\>,.?\\/]*$");

    private static BreachDatabase TOP_100K_PASSWORDS_DB = null;

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

	boolean isValidationRequired = Configuration
		.getAsBoolean(ConfigurationKeys.USER_VALIDATION_REQUIRED_FIRST_LAST_NAME);
	if (isValidationRequired) {
	    return ValidationUtil.isRegexMatches(ValidationUtil.REGEX_FIRST_LAST_NAME, name);
	}
	return true;
    }

    public static boolean isPasswordValueValid(String password, String password2) {
	return ValidationUtil.isPasswordValueValid(password, password2, null);
    }

    public static boolean isPasswordValueValid(String password, String password2, User user) {

	if (password == null || password2 == null || !password.equals(password2)) {
	    return false;
	}

	boolean isCharactersAllowed = ValidationUtil.isRegexMatches(ValidationUtil.REGEX_PASSWORD_CHARATERS_ALLOWED,
		password);
	if (!isCharactersAllowed) {
	    return false;
	}

	int isValidationRequiredMinLength = Configuration
		.getAsInt(ConfigurationKeys.PASSWORD_POLICY_MINIMUM_CHARACTERS);
	if (password.length() < isValidationRequiredMinLength) {
	    return false;
	}

	boolean isValidationRequiredSymbols = Configuration.getAsBoolean(ConfigurationKeys.PASSWORD_POLICY_SYMBOLS);

	if (isValidationRequiredSymbols) {

	    boolean isSymbols = ValidationUtil.isRegexFound(ValidationUtil.REGEX_PASSWORD_SYMBOLS, password);
	    if (!isSymbols) {
		return false;
	    }
	}

	boolean isValidationRequiredNumerics = Configuration.getAsBoolean(ConfigurationKeys.PASSWORD_POLICY_NUMERICS);
	if (isValidationRequiredNumerics) {

	    boolean isNumerics = ValidationUtil.isRegexFound(ValidationUtil.REGEX_PASSWORD_NUMERICS, password);
	    if (!isNumerics) {
		return false;
	    }
	}

	boolean isValidationRequiredLowerCase = Configuration.getAsBoolean(ConfigurationKeys.PASSWORD_POLICY_LOWERCASE);
	if (isValidationRequiredLowerCase) {
	    boolean isLowerCase = ValidationUtil.isRegexFound(ValidationUtil.REGEX_PASSWORD_LOWER_CASE, password);
	    if (!isLowerCase) {
		return false;
	    }

	}

	boolean isValidationRequiredUpperCase = Configuration.getAsBoolean(ConfigurationKeys.PASSWORD_POLICY_UPPERCASE);
	if (isValidationRequiredUpperCase) {
	    boolean isUpperCase = ValidationUtil.isRegexFound(ValidationUtil.REGEX_PASSWORD_UPPER_CASE, password);
	    if (!isUpperCase) {
		return false;
	    }

	}

	boolean isPasswordNotUserDetails = ValidationUtil.isPasswordNotUserDetails(password, user);
	if (!isPasswordNotUserDetails) {
	    return false;
	}

	return ValidationUtil.isPasswordNotBreached(password);
    }

    /**
     * Checks if password is not the same as user ID, login, email or names.
     */
    public static boolean isPasswordNotUserDetails(String password, User user) {
	if (user == null || StringUtils.isBlank(password)) {
	    return true;
	}
	if (user.getUserId() != null && password.equals(user.getUserId().toString())) {
	    return false;
	}
	if (StringUtils.isNotBlank(user.getLogin()) && password.equalsIgnoreCase(user.getLogin().trim())) {
	    return false;
	}
	if (StringUtils.isNotBlank(user.getEmail()) && password.equalsIgnoreCase(user.getEmail().trim())) {
	    return false;
	}
	if (StringUtils.isNotBlank(user.getFirstName()) && password.equalsIgnoreCase(user.getFirstName().trim())) {
	    return false;
	}
	if (StringUtils.isNotBlank(user.getLastName()) && password.equalsIgnoreCase(user.getLastName().trim())) {
	    return false;
	}

	return true;
    }

    /**
     * Checks if password has not been used in recent history.
     */
    public static boolean isPasswordNotInHistory(String newPassword, Collection<String> hashesAndSalts) {
	int historyLimit = Configuration.getAsInt(ConfigurationKeys.PASSWORD_HISTORY_LIMIT);
	if (historyLimit <= 0) {
	    return true;
	}
	for (String hashAndSalt : hashesAndSalts) {
	    String[] hashAndSaltSplit = hashAndSalt.split("=");
	    String oldHash = hashAndSaltSplit[0];
	    String oldSalt = hashAndSaltSplit[1];
	    String newHash = HashUtil.sha256(newPassword, oldSalt);
	    if (oldHash.equals(newHash)) {
		return false;
	    }
	}
	return true;
    }

    public static boolean isPasswordNotBreached(String password) {
	try {
	    // load file with passwords only once
	    if (TOP_100K_PASSWORDS_DB == null) {
		TOP_100K_PASSWORDS_DB = BreachDatabase.top100K();
	    }
	    // check for a static list of 100k known weak passwords
	    PasswordPolicy commonPasswordPolicy = new PasswordPolicy(TOP_100K_PASSWORDS_DB, 0, Integer.MAX_VALUE);
	    if (Status.OK != commonPasswordPolicy.check(password)) {
		return false;
	    }

	    // check online DB
	    PasswordPolicy pwnedPolicy = new PasswordPolicy(BreachDatabase.haveIBeenPwned(), 0, Integer.MAX_VALUE);
	    if (Status.OK != pwnedPolicy.check(password)) {
		return false;
	    }

	} catch (Exception e) {
	    log.error("Error while checking password for breach", e);
	}
	return true;
    }

    /**
     * Checks whether supplied email address is valid. It validates email only if USER_VALIDATION_REQUIRED_EMAIL LAMS
     * configuration is ON.
     */
    public static boolean isEmailValid(String email) {
	return ValidationUtil.isEmailValid(email, true);
    }

    /**
     * Checks whether supplied email address is valid.
     *
     * @param email
     *            supplied email
     * @param checkConfiguration
     *            whether it should take into account LAMS server configuration setting USER_VALIDATION_REQUIRED_EMAIL
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

    private static boolean isRegexFound(Pattern pattern, String input) {
	if (input == null) {
	    return true;
	}

	Matcher m = pattern.matcher(input.trim());
	return m.find();
    }

    /**
     * Checks whether min words limit is reached
     *
     * @param text
     * @param minWordsLimit
     * @param isDerivedFromCKEditor
     *            whether the text was acquired from CKEditor
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

	int wordCount = 0;
	if (text.length() > 0) {
	    String cleanedString = text.replaceAll("[\'\";:,\\.\\?\\-!]+", "").trim();
	    wordCount = cleanedString.split("\\S+").length;//.match(/\S+/g) || []) ;
	    // special case - if only one word and no spaces then the split array is empty.
	    if (wordCount == 0 && cleanedString.length() > 0) {
		wordCount = 1;
	    }
	}

	// check min words limit is reached
	return (wordCount >= minWordsLimit);
    }
}
