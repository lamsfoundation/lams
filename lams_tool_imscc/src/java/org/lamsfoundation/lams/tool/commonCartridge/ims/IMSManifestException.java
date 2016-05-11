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
 * This exception is thrown when an error occurs that may be related to the formatting of the IMS content package. e.g.
 * the manifest file is missing.
 *
 * @author Fiona Malikoff
 */
public class IMSManifestException extends ImscpApplicationException {

    /**
     * 
     */
    public IMSManifestException() {
	super();
    }

    /**
     * @param arg0
     */
    public IMSManifestException(String arg0) {
	super(arg0);
    }

    /**
     * @param arg0
     */
    public IMSManifestException(Throwable arg0) {
	super(arg0);
    }

    /**
     * @param arg0
     * @param arg1
     */
    public IMSManifestException(String arg0, Throwable arg1) {
	super(arg0, arg1);
    }

}
