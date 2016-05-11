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

package org.lamsfoundation.lams.workspace.dto;

/**
 * @author Manpreet Minhas
 */
public class UpdateContentDTO {

    Long uuid;
    Long version;
    Long folderContentID;

    public UpdateContentDTO(Long uuid, Long version, Long folderContentID) {
	super();
	this.uuid = uuid;
	this.version = version;
	this.folderContentID = folderContentID;
    }

    /**
     * @return Returns the folderContentID.
     */
    public Long getFolderContentID() {
	return folderContentID;
    }

    /**
     * @return Returns the uuID.
     */
    public Long getUuid() {
	return uuid;
    }

    /**
     * @return Returns the versionID.
     */
    public Long getVersion() {
	return version;
    }
}
