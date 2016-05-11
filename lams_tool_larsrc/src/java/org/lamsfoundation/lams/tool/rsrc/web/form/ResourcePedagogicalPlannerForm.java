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


package org.lamsfoundation.lams.tool.rsrc.web.form;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.tool.rsrc.ResourceConstants;
import org.lamsfoundation.lams.tool.rsrc.model.Resource;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceItem;
import org.lamsfoundation.lams.util.FileValidatorUtil;
import org.lamsfoundation.lams.web.planner.PedagogicalPlannerActivityForm;

/**
 * @struts.form name="pedagogicalPlannerForm"
 */
public class ResourcePedagogicalPlannerForm extends PedagogicalPlannerActivityForm {
    private List<String> title;
    private List<String> url;
    private List<FormFile> file;
    private List<String> fileName;
    private List<Long> fileUuid;
    private List<Long> fileVersion;
    private List<Short> type;
    private String instructions;
    private String contentFolderID;

    @Override
    public ActionMessages validate() {
	ActionMessages errors = new ActionMessages();
	boolean allEmpty = true;
	if (title != null && !title.isEmpty()) {
	    for (int index = 0; index < title.size(); index++) {
		if (!StringUtils.isEmpty(title.get(index))) {
		    // at least one item exists and it has non-blank title
		    allEmpty = false;
		    Short itemType = type.get(index);
		    // URL should not be blank
		    if (itemType.equals(ResourceConstants.RESOURCE_TYPE_URL) && StringUtils.isEmpty(url.get(index))) {
			ActionMessage error = new ActionMessage("error.planner.url.blank", index + 1);
			errors.add(ActionMessages.GLOBAL_MESSAGE, error);

		    } else if (itemType.equals(ResourceConstants.RESOURCE_TYPE_FILE)) {
			/*
			 * File should be saved already or it should be provided. This functionality required some
			 * changes in pedagogicalPlanner.js in lams_central (see prepareFormData() there)
			 */
			FileValidatorUtil.validateFileSize(file.get(index), true, errors);
			if (fileUuid.get(index) == null && file.get(index) == null) {
			    ActionMessage error = new ActionMessage("error.planner.file.blank", index + 1);
			    errors.add(ActionMessages.GLOBAL_MESSAGE, error);
			}
		    }
		}
	    }
	}
	if (allEmpty) {
	    ActionMessage error = new ActionMessage("error.planner.no.resource.save");
	    errors.add(ActionMessages.GLOBAL_MESSAGE, error);
	    title = null;
	    url = null;
	    fileName = null;
	    fileUuid = null;
	    fileVersion = null;
	    type = null;
	}

	setValid(errors.isEmpty());
	return errors;
    }

    public void fillForm(Resource resource) {
	if (resource != null) {
	    setToolContentID(resource.getContentId());
	    setInstructions(resource.getInstructions());

	    title = new ArrayList<String>();
	    url = new ArrayList<String>();
	    fileName = new ArrayList<String>();
	    file = new ArrayList<FormFile>();
	    type = new ArrayList<Short>();
	    Set<ResourceItem> items = resource.getResourceItems();
	    if (items != null) {
		int topicIndex = 0;
		for (ResourceItem item : items) {
		    short itemType = item.getType();
		    setTitle(topicIndex, item.getTitle());
		    setType(topicIndex, itemType);
		    setUrl(topicIndex, itemType == ResourceConstants.RESOURCE_TYPE_URL ? item.getUrl() : null);
		    setFileName(topicIndex,
			    itemType == ResourceConstants.RESOURCE_TYPE_FILE ? item.getFileName() : null);
		    setFileUuid(topicIndex,
			    itemType == ResourceConstants.RESOURCE_TYPE_FILE ? item.getFileUuid() : null);
		    setFileVersion(topicIndex,
			    itemType == ResourceConstants.RESOURCE_TYPE_FILE ? item.getFileVersionId() : null);
		    topicIndex++;
		}
	    }
	}
    }

    public void setTitle(int number, String formTitle) {
	if (title == null) {
	    title = new ArrayList<String>();
	}
	while (number >= title.size()) {
	    title.add(null);
	}
	title.set(number, formTitle);
	// URL items do not declare the file field, but addition is required for consistency
	if (file == null || file.size() <= number) {
	    setFile(number, null);
	}
    }

    public String getTitle(int number) {
	if (title == null || number >= title.size()) {
	    return null;
	}
	return title.get(number);
    }

    public void setUrl(int number, String formUrl) {
	if (url == null) {
	    url = new ArrayList<String>();
	}
	while (number >= url.size()) {
	    url.add(null);
	}
	url.set(number, formUrl);
    }

    public String getUrl(int number) {
	if (url == null || number >= url.size()) {
	    return null;
	}
	return url.get(number);
    }

    public void setType(int number, Short formType) {
	if (type == null) {
	    type = new ArrayList<Short>();
	}
	while (number >= type.size()) {
	    type.add(null);
	}
	type.set(number, formType);
    }

    public Short getType(int number) {
	if (type == null || number >= type.size()) {
	    return null;
	}
	return type.get(number);
    }

    public void setFile(int number, FormFile formFile) {
	if (file == null) {
	    file = new ArrayList<FormFile>();
	}
	while (number >= file.size()) {
	    file.add(null);
	}
	file.set(number, formFile);
    }

    public FormFile getFile(int number) {
	if (file == null || number >= file.size()) {
	    return null;
	}
	return file.get(number);
    }

    public void setFileName(int number, String formFileName) {
	if (fileName == null) {
	    fileName = new ArrayList<String>();
	}
	while (number >= fileName.size()) {
	    fileName.add(null);
	}
	fileName.set(number, formFileName);
    }

    public String getFileName(int number) {
	if (fileName == null || number >= fileName.size()) {
	    return null;
	}
	return fileName.get(number);
    }

    public void setFileVersion(int number, Long formFileVersion) {
	if (fileVersion == null) {
	    fileVersion = new ArrayList<Long>();
	}
	while (number >= fileVersion.size()) {
	    fileVersion.add(null);
	}
	fileVersion.set(number, formFileVersion);
    }

    public Long getFileVersion(int number) {
	if (fileVersion == null || number >= fileVersion.size()) {
	    return null;
	}
	return fileVersion.get(number);
    }

    public void setFileUuid(int number, Long formFileUuid) {
	if (fileUuid == null) {
	    fileUuid = new ArrayList<Long>();
	}
	while (number >= fileUuid.size()) {
	    fileUuid.add(null);
	}
	if (new Long(0).equals(formFileUuid)) {
	    fileUuid.set(number, null);
	} else {
	    fileUuid.set(number, formFileUuid);
	}
    }

    public Long getFileUuid(int number) {
	if (fileUuid == null || number >= fileUuid.size()) {
	    return null;
	}
	return fileUuid.get(number);
    }

    public Integer getItemCount() {
	return title == null ? 0 : title.size();
    }

    public boolean removeItem(int number) {
	if (title == null || number >= title.size()) {
	    return false;
	}
	title.remove(number);
	url.remove(number);
	type.remove(number);
	file.remove(number);
	fileName.remove(number);
	fileUuid.remove(number);
	fileVersion.remove(number);
	return true;
    }

    public List<Short> getTypeList() {
	return type;
    }

    public List<String> getFileNameList() {
	return fileName;
    }

    /**
     * Special method that is used to fool the browser. If there was no file provided, there was an argument type
     * mismatch excpetion since browser tried to fill an empty string to file field. Now in pedagogicalPlanner.js in
     * lams_central it is detected and the field name is changed to fileDummy, which sets the file to <code>NULLM</code>
     * here in Java form file.
     *
     * @param number
     *            where to set the empty file
     * @param emptyString
     *            never used; the file is set to <code>NULL</code> anyway
     */
    public void setFileDummy(int number, String emptyString) {
	setFile(number, null);
    }

    public String getInstructions() {
	return instructions;
    }

    public void setInstructions(String instructions) {
	this.instructions = instructions;
    }

    public String getContentFolderID() {
	return contentFolderID;
    }

    public void setContentFolderID(String contentFolderID) {
	this.contentFolderID = contentFolderID;
    }
}