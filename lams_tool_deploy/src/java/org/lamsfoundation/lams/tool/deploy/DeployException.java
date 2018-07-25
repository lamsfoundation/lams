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


package org.lamsfoundation.lams.tool.deploy;

/**
 * Exception thrown by deployment process.
 * 
 * @author chris
 */
public class DeployException extends java.lang.RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 4637002190190219303L;

    /**
     * Creates a new instance of <code>DeployException</code> without detail message.
     */
    public DeployException() {
    }

    /**
     * Constructs an instance of <code>DeployException</code> with the specified detail message.
     * 
     * @param msg
     *            the detail message.
     */
    public DeployException(String msg) {
	super(msg);
    }

    public DeployException(String msg, Throwable cause) {
	super(msg, cause);
    }

    public DeployException(Throwable cause) {
	super(cause);
    }
}
