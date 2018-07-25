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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */


package org.lamsfoundation.lams.tool.imageGallery.web.form;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

/**
 * Multiple Images Form.
 *
 *
 * @author Andrey Balan
 */
public class MultipleImagesForm extends ActionForm {

    private static final long serialVersionUID = -5595679171112282994L;

    private String sessionMapID;

    // flag of this item has attachment or not
    private boolean hasFile;
    private Long fileUuid;
    private String fileName;
    private FormFile file1;
    private FormFile file2;
    private FormFile file3;
    private FormFile file4;
    private FormFile file5;

    //used only in monitoring
    private String imageUid;

    public String getSessionMapID() {
	return sessionMapID;
    }

    public void setSessionMapID(String sessionMapID) {
	this.sessionMapID = sessionMapID;
    }

    public String getFileName() {
	return fileName;
    }

    public void setFileName(String fileName) {
	this.fileName = fileName;
    }

    public Long getFileUuid() {
	return fileUuid;
    }

    public void setFileUuid(Long fileUuid) {
	this.fileUuid = fileUuid;
    }

    public boolean isHasFile() {
	return hasFile;
    }

    public void setHasFile(boolean hasFile) {
	this.hasFile = hasFile;
    }

    public FormFile getFile1() {
	return file1;
    }

    public void setFile1(FormFile file) {
	this.file1 = file;
    }

    public FormFile getFile2() {
	return file2;
    }

    public void setFile2(FormFile file) {
	this.file2 = file;
    }

    public FormFile getFile3() {
	return file3;
    }

    public void setFile3(FormFile file) {
	this.file3 = file;
    }

    public FormFile getFile4() {
	return file4;
    }

    public void setFile4(FormFile file) {
	this.file4 = file;
    }

    public FormFile getFile5() {
	return file5;
    }

    public void setFile5(FormFile file) {
	this.file5 = file;
    }

    public String getImageUid() {
	return imageUid;
    }

    public void setImageUid(String imageUid) {
	this.imageUid = imageUid;
    }
}
