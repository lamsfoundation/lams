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


package org.lamsfoundation.lams.contentrepository.exception;

/**
 * Main runtime exception thrown by content repository classes. This is only
 * used for unexpected internal errors, such that the calling code could
 * never recover.
 *
 * @see RepositoryCheckedException
 */
public class RepositoryRuntimeException extends RuntimeException {

    /**
     * Constructs a new instance of this class.
     */
    public RepositoryRuntimeException() {
	this("Content Repository Runtime Error.");
    }

    /**
     * Constructs a new instance of this class given a message describing the
     * failure cause.
     *
     * @param s
     *            description
     */
    public RepositoryRuntimeException(String s) {
	super(s);
    }

    /**
     * Constructs a new instance of this class given a message describing the
     * failure and a root throwable.
     *
     * @param s
     *            description
     * @param cause
     *            root throwable cause
     */
    public RepositoryRuntimeException(String s, Throwable cause) {
	super(s, cause);

    }

    /**
     * Constructs a new instance of this class given a root throwable.
     *
     * @param cause
     *            root failure cause
     */
    public RepositoryRuntimeException(Throwable cause) {
	this("Content Repository Runtime Error.", cause);
    }

    @Override
    public String getMessage() {

	String s1 = super.getMessage();
	if (s1 == null) {
	    s1 = "";
	}

	Throwable cause = getCause();
	String s2 = cause != null ? cause.getMessage() : null;
	return s2 != null ? s1 + ":" + s2 : s1;

    }

}
