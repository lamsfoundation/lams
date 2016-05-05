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

import java.util.Random;

/**
 * RandomPasswordGenerator generates a random password with the specified length.
 *
 * @author <a href="mailto:anthony.xiao@lamsinternational.com">Anthony Xiao</a>
 */
public class RandomPasswordGenerator {

    //define the legal characters in our random password
    private static final char[] CHAR_ARRAY = "ABCDEFGHIJKLMNOPQRSTUVWXYZabedefghijklmnopqrstuvwxyz01234567890"
	    .toCharArray();

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
	    res[i] = CHAR_ARRAY[generator.nextInt(CHAR_ARRAY.length)];
	}

	return new String(res);
    }
}
