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


package org.lamsfoundation.lams.tool;

/**
 * Thrown when we try to get a output as the wrong type.
 */
public class ToolOutputFormatException extends RuntimeException {
    /**
     * Constructs a new instance of this class.
     */
    public ToolOutputFormatException() {
	this("Tried to get value in the wrong format.");
    }

    /**
     * Constructs a new instance of this class given a message describing the
     * failure cause.
     *
     * @param s
     *            description
     */
    public ToolOutputFormatException(String s) {
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
    public ToolOutputFormatException(String s, Throwable cause) {
	super(s, cause);

    }

    /**
     * Constructs a new instance of this class given a root throwable.
     *
     * @param cause
     *            root failure cause
     */
    public ToolOutputFormatException(Throwable cause) {
	this("Tried to get value in the wrong format.", cause);
    }

}
