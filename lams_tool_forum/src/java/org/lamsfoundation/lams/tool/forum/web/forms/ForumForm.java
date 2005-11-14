package org.lamsfoundation.lams.tool.forum.web.forms;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.ValidatorForm;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.tool.forum.persistence.Attachment;
import org.lamsfoundation.lams.tool.forum.persistence.Forum;
import org.lamsfoundation.lams.util.UploadFileUtil;

/**
 *
 * 	Message Form.
 *	@struts.form name="forumForm"
 *
 * User: conradb
 * Date: 10/06/2005
 * Time: 15:44:36
 */
public class ForumForm extends ValidatorForm {
	private static final long serialVersionUID = -6054354910960460120L;
	private static Logger logger = Logger.getLogger(ForumForm.class.getName());

	//Forum fields
	private Long toolContentID;
	private int currentTab;
    private FormFile offlineFile;
    private FormFile onlineFile;
    private List onlineFileList;
    private List offlineFileList;
    private Forum forum;

    /**
     * Empty construction method
     */
    public ForumForm() {
    	
    	this.toolContentID = new Long(0);
        this.forum = new Forum();
        this.forum.setTitle("New Forum");
    }
    /**
     * Initialize this form by given <code>Forum</code> instance.
     * @param forum
     */
    public void setForum(Forum forum) {
        this.forum = forum;
        //set Form special varaible from given forum
        if(forum != null){
        	this.toolContentID = forum.getContentId();
    		onlineFileList = new ArrayList();
    		offlineFileList = new ArrayList();
    		Set fileSet = forum.getAttachments();
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
        }
        
    }
    /**
     * Forum validation method from STRUCT interface.
     * 
     */
    public ActionErrors validate(ActionMapping mapping, javax.servlet.http.HttpServletRequest request) {
		ActionErrors errors = super.validate(mapping, request);
		ActionMessage ae;
		try {
			if ("".equals(forum.getTitle())) {
				ActionMessage error = new ActionMessage("error.valueReqd");
				errors.add("forum.title", error);
			}
			if (onlineFile != null && !(onlineFile.getFileName().trim().equals(""))
					&& convertToMeg(onlineFile.getFileSize()) > UploadFileUtil.getMaxFileSize()) {
				ae = new ActionMessage("error.inputFileTooLarge");
				errors.add("onlineFile", ae);
			}
			if (offlineFile != null && !(offlineFile.getFileName().trim().equals(""))
					&& convertToMeg(offlineFile.getFileSize()) > UploadFileUtil.getMaxFileSize()) {
				ae = new ActionMessage("error.inputFileTooLarge");
				errors.add("offlineFile", ae);
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		return errors;
	}

    //-------------------------get/set methods----------------
    public Forum getForum() {
        return forum;
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


    private float convertToMeg( int numBytes ) {
        return numBytes != 0 ? numBytes / 1024 / 1024 : 0;
    }
    

	public int getCurrentTab() {
		return currentTab;
	}

	public void setCurrentTab(int currentTab) {
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
}
