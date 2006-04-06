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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $$Id$$ */	

package org.lamsfoundation.lams.tool.forum.web.forms;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;
import org.lamsfoundation.lams.tool.forum.dto.MessageDTO;
import org.lamsfoundation.lams.tool.forum.persistence.ForumUser;
/**
 * @struts.form name="markForm"
 * @author Steve.Ni
 * 
 * @version $Revision$
 */
public class MarkForm  extends ValidatorForm{
	private static final long serialVersionUID = -4967675292027554366L;
	private static Logger logger = Logger.getLogger(MarkForm.class.getName());
	
	private String mark;
	private String comment;
	private ForumUser user;
	private MessageDTO messageDto;
	private Long sessionId;

	/**
     * MessageForm validation method from STRUCT interface.
     * 
     */
    public ActionErrors validate(ActionMapping mapping,
                                 javax.servlet.http.HttpServletRequest request) {
        ActionErrors errors = super.validate(mapping, request);
        try{
            if ("".equals(mark)) {
              ActionMessage error = new ActionMessage("error.valueReqd");
              errors.add("report.mark", error);
            }else if(!NumberUtils.isNumber(mark)){
            	ActionMessage error = new ActionMessage("error.mark.needNumber");
            	errors.add("report.mark", error);
            }else {
            	try{
            		Float.parseFloat(mark);
            	}catch(Exception e){
                  	ActionMessage error = new ActionMessage("error.mark.invalid.number");
                	errors.add("report.mark", error);
            	}
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        return errors;
    }
    
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	
    public MessageDTO getMessageDto() {
		return messageDto;
	}

	public void setMessageDto(MessageDTO message) {
		this.messageDto = message;
	}

	public ForumUser getUser() {
		return user;
	}

	public void setUser(ForumUser user) {
		this.user = user;
	}
	public Long getSessionId() {
		return sessionId;
	}

	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}
}
