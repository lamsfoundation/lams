/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */
package org.lamsfoundation.lams.tool.sbmt.dto;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.tool.sbmt.InstructionFiles;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesContent;
/**
 * 
 * @author Steve.Ni
 * @serial 3555065437595921234L
 * $version$
 */
public class AuthoringDTO implements Serializable {

	private static final long serialVersionUID = 3555065437595921234L;
	private Long contentID;
	//basic tab
	private String title;
	private String instruction;
	//instructions
	private String onlineInstruction;
	private String offlineInstruction;
	private List onlineFiles;
	private List offlineFiles;
	//advance
	private boolean lockOnFinished;
	
	public AuthoringDTO(){
	}
	public AuthoringDTO(SubmitFilesContent content){
		if(content == null)
			return;
		try {
			PropertyUtils.copyProperties(this,content);
		} catch (IllegalAccessException e) {
			throw new DTOException(e);
		} catch (InvocationTargetException e) {
			throw new DTOException(e);
		} catch (NoSuchMethodException e) {
			throw new DTOException(e);
		}
		
		onlineFiles = new ArrayList();
		offlineFiles = new ArrayList();
		Set fileSet = content.getInstructionFiles();
		if(fileSet != null){
			Iterator iter = fileSet.iterator();
			while(iter.hasNext()){
				InstructionFiles file = (InstructionFiles) iter.next();
				if(StringUtils.equalsIgnoreCase(file.getType(),IToolContentHandler.TYPE_OFFLINE))
					offlineFiles.add(file);
				else
					onlineFiles.add(file);
			}
		}
		
	}
	/**
	 * @return Returns the contentID.
	 */
	public Long getContentID() {
		return contentID;
	}
	/**
	 * @param contentID The contentID to set.
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
	 * @param instruction The instruction to set.
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
	 * @param lockOnFinished The lockOnFinished to set.
	 */
	public void setLockOnFinished(boolean lockOnFinished) {
		this.lockOnFinished = lockOnFinished;
	}
	/**
	 * @return Returns the offlineFiles.
	 */
	public List getOfflineFiles() {
		return offlineFiles;
	}
	/**
	 * @param offlineFiles The offlineFiles to set.
	 */
	public void setOfflineFiles(List offlineFiles) {
		this.offlineFiles = offlineFiles;
	}
	/**
	 * @return Returns the offlineInstruction.
	 */
	public String getOfflineInstruction() {
		return offlineInstruction;
	}
	/**
	 * @param offlineInstruction The offlineInstruction to set.
	 */
	public void setOfflineInstruction(String offlineInstruction) {
		this.offlineInstruction = offlineInstruction;
	}
	/**
	 * @return Returns the onlineFiles.
	 */
	public List getOnlineFiles() {
		return onlineFiles;
	}
	/**
	 * @param onlineFiles The onlineFiles to set.
	 */
	public void setOnlineFiles(List onlineFiles) {
		this.onlineFiles = onlineFiles;
	}
	/**
	 * @return Returns the onlineInstruction.
	 */
	public String getOnlineInstruction() {
		return onlineInstruction;
	}
	/**
	 * @param onlineInstruction The onlineInstruction to set.
	 */
	public void setOnlineInstruction(String onlineInstruction) {
		this.onlineInstruction = onlineInstruction;
	}
	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
}
