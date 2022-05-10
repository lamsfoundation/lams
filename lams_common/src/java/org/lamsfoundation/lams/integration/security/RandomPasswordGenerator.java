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
package org.lamsfoundation.lams.integration.security;

import java.io.IOException;
import java.util.Properties;
import java.util.Random;

import org.apache.commons.lang.ArrayUtils;
import org.hibernate.HibernateException;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.UUIDGenerator;
import org.hibernate.type.StringType;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.FileUtilException;
import org.lamsfoundation.lams.util.ValidationUtil;

/**
 * RandomPasswordGenerator generates a random password with the specified length.
 *
 * @author <a href="mailto:anthony.xiao@lamsinternational.com">Anthony Xiao</a>
 */
public class RandomPasswordGenerator {

    private static final char[] UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    private static final char[] LOWER_CASE = "abedefghijklmnopqrstuvwxyz".toCharArray();

    private static final char[] DIGITS = "0123456789".toCharArray();

    private static final char[] SYMBOLS = "`~!@#$%^&*\\()_-+={}[]|:;\"'<>,.?/".toCharArray();

    //define the legal characters in our random password
    private static final char[] SIMPLE_PASSWORD_RANGE = ArrayUtils.addAll(ArrayUtils.addAll(UPPER_CASE, LOWER_CASE),
	    DIGITS);

    private static final char NULL_CHAR = '\u0000';

    private static final int MINIMUM_PASSWORD_LENGTH = 10;

    /**
     * Get the next random password
     *
     * @param length
     *            - the length of the password
     * @return the next random password
     */
    public static String nextPassword(int length) {
	char res[] = new char[length];
	Random generator = new Random();

	for (int i = 0; i < length; i++) {
	    res[i] = SIMPLE_PASSWORD_RANGE[generator.nextInt(SIMPLE_PASSWORD_RANGE.length)];
	}

	return new String(res);
    }

    /**
     * Produce a password with
     */
    public static String nextPassword() {
	Integer passwordLength = Configuration.getAsInt(ConfigurationKeys.PASSWORD_POLICY_MINIMUM_CHARACTERS);
	if (passwordLength == null || passwordLength < MINIMUM_PASSWORD_LENGTH) {
	    passwordLength = MINIMUM_PASSWORD_LENGTH;
	}
	return RandomPasswordGenerator.nextPassword(passwordLength);
    }

    /**
     * Build a password that passes configured validation
     */
    public static String nextPasswordValidated() {
	Random generator = new Random();
	int length = Math.max(MINIMUM_PASSWORD_LENGTH,
		Configuration.getAsInt(ConfigurationKeys.PASSWORD_POLICY_MINIMUM_CHARACTERS));
	char res[] = new char[length];
	int index = 0;
	// make sure that there is at least one character of each set
	for (char[] set : new char[][] { UPPER_CASE, LOWER_CASE, DIGITS, SYMBOLS }) {
	    do {
		index = generator.nextInt(length);
		if (res[index] == NULL_CHAR) {
		    res[index] = set[generator.nextInt(set.length)];
		    break;
		}
	    } while (true);
	}

	// fill other characters
	for (index = 0; index < length; index++) {
	    if (res[index] == NULL_CHAR) {
		res[index] = SIMPLE_PASSWORD_RANGE[generator.nextInt(SIMPLE_PASSWORD_RANGE.length)];
	    }
	}

	String result = String.valueOf(res);
	if (!ValidationUtil.isPasswordValueValid(result, result)) {
	    throw new RuntimeException("Password generator created password which does not pass validation: " + result);
	}

	return result;
    }

    /**
     * Generates the unique key used for the forgot password request
     *
     * @return a unique key
     * @throws HibernateException
     * @throws FileUtilException
     * @throws IOException
     */
    public static String generateForgotPasswordKey() {
	Properties props = new Properties();

	IdentifierGenerator uuidGen = new UUIDGenerator();
	((Configurable) uuidGen).configure(StringType.INSTANCE, props, null);

	return ((String) uuidGen.generate(null, null)).toLowerCase();
    }
}