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


package org.lamsfoundation.lams.contentrepository;

/**
 * @author Fiona Malikoff
 *
 *         Names of known properties, such as MIMETYPE, INITIALPATH
 */
public final class PropertyName {

    /**
     * VERSIONDESC is text description relating to a version
     * It should contain text meaningful to the user. Applicable to all node types.
     */
    public static final String VERSIONDESC = "VERSIONDESC";

    /** MIMETYPE is required for a file node - it is set by the call to add the file stream. */
    public static final String MIMETYPE = "MIMETYPE";
    /** FILENAME is required for a file node - it is set by the call to add the file stream. */
    public static final String FILENAME = "FILENAME";

    /** INITIALPATH is required for a package node */
    public static final String INITIALPATH = "INITIALPATH";

}
