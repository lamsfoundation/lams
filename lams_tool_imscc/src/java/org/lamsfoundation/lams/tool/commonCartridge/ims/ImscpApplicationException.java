/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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

package org.lamsfoundation.lams.tool.commonCartridge.ims;

/**
 * Generic exception for the imscr tool.
 *
 * @author Fiona Malikoff
 */
public class ImscpApplicationException extends Exception {

    /**
     * 
     */
    public ImscpApplicationException() {
	super();
    }

    /**
     * @param arg0
     */
    public ImscpApplicationException(String arg0) {
	super(arg0);
    }

    /**
     * @param arg0
     */
    public ImscpApplicationException(Throwable arg0) {
	super(arg0);
    }

    /**
     * @param arg0
     * @param arg1
     */
    public ImscpApplicationException(String arg0, Throwable arg1) {
	super(arg0, arg1);
    }

}
