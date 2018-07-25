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



package org.lamsfoundation.lams.tool.sbmt.dto;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesContent;

/**
 *
 * @author Steve.Ni
 * @serial 3555065437595921234L
 */
public class AuthoringDTO implements Serializable {

    private static final long serialVersionUID = 3555065437595921234L;

    private Long contentID;

    // basic tab
    private String title;

    private String instruction;

    // advance
    private boolean useSelectLeaderToolOuput;
    
    private boolean lockOnFinished;

    private boolean limitUpload;

    private int limitUploadNumber;

    private boolean reflectOnActivity;

    private String reflectInstructions;

    private boolean notifyLearnersOnMarkRelease;

    private boolean notifyTeachersOnFileSubmit;

    public AuthoringDTO() {
    }

    public AuthoringDTO(SubmitFilesContent content) {
	if (content == null) {
	    return;
	}
	try {
	    PropertyUtils.copyProperties(this, content);
	} catch (IllegalAccessException e) {
	    throw new DTOException(e);
	} catch (InvocationTargetException e) {
	    throw new DTOException(e);
	} catch (NoSuchMethodException e) {
	    throw new DTOException(e);
	}

    }

    /**
     * @return Returns the contentID.
     */
    public Long getContentID() {
	return contentID;
    }

    /**
     * @param contentID
     *            The contentID to set.
     */
    public void setContentID(Long contentID) {
	this.contentID = contentID;
    }

    /**
     * @return Returns the instruction.
     */
    public String getInstruction() {
	return instruction;
    }

    /**
     * @param instruction
     *            The instruction to set.
     */
    public void setInstruction(String instruction) {
	this.instruction = instruction;
    }

    /**
     * @return Returns the lockOnFinished.
     */
    public boolean isLockOnFinished() {
	return lockOnFinished;
    }

    /**
     * @param lockOnFinished
     *            The lockOnFinished to set.
     */
    public void setLockOnFinished(boolean lockOnFinished) {
	this.lockOnFinished = lockOnFinished;
    }

    /**
     * @return Returns the title.
     */
    public String getTitle() {
	return title;
    }

    /**
     * @param title
     *            The title to set.
     */
    public void setTitle(String title) {
	this.title = title;
    }

    public boolean isLimitUpload() {
	return limitUpload;
    }

    public void setLimitUpload(boolean limitUpload) {
	this.limitUpload = limitUpload;
    }

    public int getLimitUploadNumber() {
	return limitUploadNumber;
    }

    public void setLimitUploadNumber(int limitUploadNumber) {
	this.limitUploadNumber = limitUploadNumber;
    }

    public boolean isReflectOnActivity() {
	return reflectOnActivity;
    }

    public void setReflectOnActivity(boolean reflectOnActivity) {
	this.reflectOnActivity = reflectOnActivity;
    }

    public String getReflectInstructions() {
	return reflectInstructions;
    }

    public void setReflectInstructions(String reflectInstructions) {
	this.reflectInstructions = reflectInstructions;
    }

    public boolean isNotifyLearnersOnMarkRelease() {
	return notifyLearnersOnMarkRelease;
    }

    public void setNotifyLearnersOnMarkRelease(boolean notifyLearnersOnMarkRelease) {
	this.notifyLearnersOnMarkRelease = notifyLearnersOnMarkRelease;
    }

    public boolean isNotifyTeachersOnFileSubmit() {
	return notifyTeachersOnFileSubmit;
    }

    public void setNotifyTeachersOnFileSubmit(boolean notifyTeachersOnFileSubmit) {
	this.notifyTeachersOnFileSubmit = notifyTeachersOnFileSubmit;
    }
    
    public boolean isUseSelectLeaderToolOuput() {
   	return useSelectLeaderToolOuput;
    }

    public void setUseSelectLeaderToolOuput(boolean useSelectLeaderToolOuput) {
   	this.useSelectLeaderToolOuput = useSelectLeaderToolOuput;
    }
}
