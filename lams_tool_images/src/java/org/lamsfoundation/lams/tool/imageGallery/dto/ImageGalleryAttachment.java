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

package org.lamsfoundation.lams.tool.imageGallery.dto;

/**
 * A Wrapper class for uploaded files. Used for learning design export/import functionality, namely, for export/import
 * of the uploaded images.
 * 
 * @author Andrey Balan
 */
public class ImageGalleryAttachment implements Cloneable {

    private Long fileUuid;
    private Long fileVersionId;
    private String fileType;
    private String fileName;

    // **********************************************************
    // get/set methods
    // **********************************************************

    public Long getFileVersionId() {
	return fileVersionId;
    }

    public void setFileVersionId(Long version) {
	this.fileVersionId = version;
    }

    public String getFileType() {
	return fileType;
    }

    public void setFileType(String type) {
	this.fileType = type;
    }

    public String getFileName() {
	return fileName;
    }

    public void setFileName(String name) {
	this.fileName = name;
    }

    public Long getFileUuid() {
	return fileUuid;
    }

    public void setFileUuid(Long uuid) {
	this.fileUuid = uuid;
    }
}
