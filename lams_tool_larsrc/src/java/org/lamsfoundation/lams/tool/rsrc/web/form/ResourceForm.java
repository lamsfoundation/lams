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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.tool.rsrc.web.form;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.ValidatorForm;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.tool.rsrc.model.Resource;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceAttachment;

/**
 *
 * 	Resource Form.
 *	@struts.form name="resourceForm"
 *
 * User: Dapeng.Ni
 */
public class ResourceForm extends ValidatorForm {
	private static final long serialVersionUID = -6054354910960460120L;
	private static Logger logger = Logger.getLogger(ResourceForm.class.getName());

	//Forum fields
	private Long toolContentID;
	private int currentTab;
    private FormFile offlineFile;
    private FormFile onlineFile;
    private List onlineFileList;
    private List offlineFileList;

    private Resource resource;
    
    public ResourceForm(){
    	this.toolContentID = new Long(0);
    	resource = new Resource();
    	resource.setTitle("Shared Resource");
    	currentTab = 1;
    }
	
	public void setResource(Resource resource) {
        this.resource = resource;
        //set Form special varaible from given forum
        if(resource != null){
        	this.toolContentID = resource.getContentId();
    		onlineFileList = new ArrayList();
    		offlineFileList = new ArrayList();
    		Set fileSet = resource.getAttachments();
    		if(fileSet != null){
    			Iterator iter = fileSet.iterator();
    			while(iter.hasNext()){
    				ResourceAttachment file = (ResourceAttachment) iter.next();
    				if(StringUtils.equalsIgnoreCase(file.getFileType(),IToolContentHandler.TYPE_OFFLINE))
    					offlineFileList.add(file);
    				else
    					onlineFileList.add(file);
    			}
    		}
        }else{
        	logger.error("Initial ResourceForum failed by null value of Resource.");
        }
	}
    public void reset(ActionMapping mapping, HttpServletRequest request){
    	resource.setAllowAddFiles(false);
    	resource.setAllowAddUrls(false);
    	resource.setLockWhenFinished(false);
    	resource.setDefineLater(false);
    	resource.setRunAuto(false);
    	resource.setRunOffline(false);
    }

	public int getCurrentTab() {
		return currentTab;
	}


	public void setCurrentTab(int currentTab) {
		this.currentTab = currentTab;
	}


	public FormFile getOfflineFile() {
		return offlineFile;
	}


	public void setOfflineFile(FormFile offlineFile) {
		this.offlineFile = offlineFile;
	}


	public List getOfflineFileList() {
		return offlineFileList;
	}


	public void setOfflineFileList(List offlineFileList) {
		this.offlineFileList = offlineFileList;
	}


	public FormFile getOnlineFile() {
		return onlineFile;
	}


	public void setOnlineFile(FormFile onlineFile) {
		this.onlineFile = onlineFile;
	}


	public List getOnlineFileList() {
		return onlineFileList;
	}


	public void setOnlineFileList(List onlineFileList) {
		this.onlineFileList = onlineFileList;
	}


	public Long getToolContentID() {
		return toolContentID;
	}


	public void setToolContentID(Long toolContentID) {
		this.toolContentID = toolContentID;
	}

	public Resource getResource() {
		return resource;
	}


}
