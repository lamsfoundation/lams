/*
 * Created on Jul 25, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool.noticeboard.web;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent;

/**
 * @author mtruong
 *
 * ----------------XDoclet Tags--------------------
 * 
 * @struts:form name="NbExportForm" type="org.lamsfoundation.lams.tool.noticeboard.web.NbExportForm"
 *
 * ----------------XDoclet Tags--------------------
 */

public class NbExportForm extends ActionForm {
    
    static Logger logger = Logger.getLogger(NbExportForm.class.getName());
    
    private String title;
	private String content;
	
	private String toolContentId;
	private String toolSessionId;
	private String userId;
	private String mode;
	
	
	

    /**
     * @return Returns the logger.
     */
    public static Logger getLogger() {
        return logger;
    }
    /**
     * @param logger The logger to set.
     */
    public static void setLogger(Logger logger) {
        NbExportForm.logger = logger;
    }
    /**
     * @return Returns the content.
     */
    public String getContent() {
        return content;
    }
    /**
     * @param content The content to set.
     */
    public void setContent(String content) {
        this.content = content;
    }
    /**
     * @return Returns the mode.
     */
    public String getMode() {
        return mode;
    }
    /**
     * @param mode The mode to set.
     */
    public void setMode(String mode) {
        this.mode = mode;
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
    /**
     * @return Returns the toolContentId.
     */
    public String getToolContentId() {
        return toolContentId;
    }
    /**
     * @param toolContentId The toolContentId to set.
     */
    public void setToolContentId(String toolContentId) {
        this.toolContentId = toolContentId;
    }
    /**
     * @return Returns the toolSessionId.
     */
    public String getToolSessionId() {
        return toolSessionId;
    }
    /**
     * @param toolSessionId The toolSessionId to set.
     */
    public void setToolSessionId(String toolSessionId) {
        this.toolSessionId = toolSessionId;
    }
    /**
     * @return Returns the userId.
     */
    public String getUserId() {
        return userId;
    }
    /**
     * @param userId The userId to set.
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public void reset()
    {
        this.mode = null;
        this.title = null;
        this.content = null;
        this.toolContentId = null;
        this.toolSessionId = null;
        this.userId = null;
    }
    
    public void populateForm(NoticeboardContent content)
    {
        setTitle(content.getTitle());
        setContent(content.getContent());
    }
}
