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

/* $$Id$$ */	
package org.lamsfoundation.lams.tool.wiki.web.forms;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.wiki.persistence.Attachment;
import org.lamsfoundation.lams.tool.wiki.persistence.Wiki;
import org.lamsfoundation.lams.util.UploadFileUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 *
 * 	Message Form.
 *	@struts.form name="wikiForm"
 *
 * User: conradb
 * Date: 10/06/2005
 * Time: 15:44:36
 */
public class WikiForm extends ActionForm {
	private static final long serialVersionUID = -6054354910960460120L;
	private static Logger logger = Logger.getLogger(WikiForm.class.getName());

	//Wiki fields
	private Long toolContentID;
	private String currentTab;
	private String sessionMapID;
	private String contentFolderID;
	
    private FormFile offlineFile;
    private FormFile onlineFile;
    private List onlineFileList;
    private List offlineFileList;

    private Wiki wiki;

    /**
     * Empty construction method
     */
    public WikiForm() {
    	
    	this.toolContentID = new Long(0);
        this.wiki = new Wiki();
        this.wiki.setTitle("");
        this.currentTab = "";
    }
    /**
     * Initialize this form by given <code>Wiki</code> instance.
     * @param wiki
     */
    public void setWiki(Wiki wiki) {
        this.wiki = wiki;
        //set Form special varaible from given wiki
        if(wiki != null){
        	this.toolContentID = wiki.getContentId();
    		onlineFileList = new ArrayList();
    		offlineFileList = new ArrayList();
    		Set fileSet = wiki.getAttachments();
    		if(fileSet != null){
    			Iterator iter = fileSet.iterator();
    			while(iter.hasNext()){
    				Attachment file = (Attachment) iter.next();
    				if(StringUtils.equalsIgnoreCase(file.getFileType(),IToolContentHandler.TYPE_OFFLINE))
    					offlineFileList.add(file);
    				else
    					onlineFileList.add(file);
    			}
    		}
        }else{
        	logger.error("Initial WikiForm failed by null value of Wiki.");
        }
        
    }
  
    public void reset(ActionMapping mapping, HttpServletRequest request){
    	wiki.setAllowAnonym(false);
    	wiki.setLockWhenFinished(false);
    	wiki.setAllowNewWikiPage(false);
    	wiki.setAllowAttachImage(false);
    	wiki.setAllowInsertWikiLink(false);
    	wiki.setAllowInsertExternalLink(false);
    	wiki.setAllowRevertPage(false);
    	wiki.setReflectOnActivity(false);
    }
    //-------------------------get/set methods----------------
    public Wiki getWiki() {
        return wiki;
    }

    public void setOnlineFile(FormFile onlineFile) {
        this.onlineFile = onlineFile;
    }

    public FormFile getOnlineFile() {
        return onlineFile;
    }

    public void setOfflineFile(FormFile offlineFile) {
        this.offlineFile = offlineFile;
    }

    public FormFile getOfflineFile() {
        return offlineFile;
    }

	public String getCurrentTab() {
		return currentTab;
	}

	public void setCurrentTab(String currentTab) {
		this.currentTab = currentTab;
	}

	public Long getToolContentID() {
		return toolContentID;
	}

	public void setToolContentID(Long toolContentID) {
		this.toolContentID = toolContentID;
	}

	public List getOfflineFileList() {
		return offlineFileList;
	}

	public void setOfflineFileList(List offlineFileList) {
		this.offlineFileList = offlineFileList;
	}

	public List getOnlineFileList() {
		return onlineFileList;
	}

	public void setOnlineFileList(List onlineFileList) {
		this.onlineFileList = onlineFileList;
	}
	public String getSessionMapID() {
		return sessionMapID;
	}
	public void setSessionMapID(String sessionMapID) {
		this.sessionMapID = sessionMapID;
	}
	public String getContentFolderID() {
		return contentFolderID;
	}
	public void setContentFolderID(String contentFolderID) {
		this.contentFolderID = contentFolderID;
	}

}
