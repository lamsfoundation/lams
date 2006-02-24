/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
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
 * ***********************************************************************/
package org.lamsfoundation.lams.tool.qa;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * 
 * @author Ozgur Demirtas
 * 
 * QaUsrResp Value Object
 * The value object that maps to our model database table: tl_laqa11_usr_resp
 * The relevant hibernate mapping resides in: QaQueResp.hbm.xml
 * 
 *  Holds user responses to questions 
 */

public class QaUsrResp implements Serializable, Comparable {

    /** identifier field */
    private Long responseId;

    /** nullable persistent field */
    private String answer;
    
    /** nullable persistent field */
    private boolean hidden;

    /** nullable persistent field */
    private Date attemptTime;

    /** nullable persistent field */
    private QaQueContent qaQueContent;
    
    /** nullable persistent field */
    private Long qaQueContentId; //added to enable deletion by the resp dao
    
    /** nullable persistent field */
    private Long queUsrId; 		//added to enable deletion by the resp dao

    /** nullable persistent field */
    private QaQueUsr qaQueUser;

    /** nullable persistent field */
    private String timezone;
    
        
    /** full constructor */
     public QaUsrResp(Long responseId,
    				String answer,
    				boolean hidden,
    				Date attemptTime,
    				String timezone,
    				QaQueContent qaQueContent, 
    				QaQueUsr qaQueUser) {
     	this.responseId =responseId;
        this.answer = answer;
        this.hidden = hidden;
        this.attemptTime = attemptTime;
        this.timezone = timezone;
        this.qaQueContent = qaQueContent;
        this.qaQueUser = qaQueUser;
    }

     
    public QaUsrResp(String answer,
				boolean hidden,
				Date attemptTime,
				String timezone,
				QaQueContent qaQueContent, 
				QaQueUsr qaQueUser) {
		this.answer = answer;
		this.hidden = hidden;
		this.attemptTime = attemptTime;
		this.timezone = timezone;
		this.qaQueContent = qaQueContent;
		this.qaQueUser = qaQueUser;
	}
     
    public QaUsrResp(String answer,
    				boolean hidden,
    				Date attemptTime,
    				String timezone,
    				QaQueContent qaQueContent,					
    				Long qaQueUsrUid) {
        this.answer = answer;
        this.hidden = hidden;
        this.attemptTime = attemptTime;
        this.timezone = timezone;
        this.qaQueContent = qaQueContent;
        this.queUsrId = qaQueUsrUid;
    }
    
    
    public QaUsrResp(String answer,
            Date attemptTime,
            QaQueContent question)
    {
		this.answer = answer;
		this.attemptTime = attemptTime;
		this.qaQueContent = question;
    }

    /** default constructor */
    public QaUsrResp(){}
    
    /**
     * Copy construtor. Delegate to full construtor to achieve the object
     * creation.
     * @param response the original survey user response
     * @return the new qa user response cloned from original object
     */
    public static QaUsrResp newInstance(QaUsrResp response)
    {
        return new QaUsrResp(response.getResponseId(),
                             response.getAnswer(),
                             response.isHidden(),
                             response.getAttemptTime(),
							 response.getTimezone(),
                             response.getQaQueContent(),
                             response.qaQueUser);
    }

    
    
    public String toString() {
        return new ToStringBuilder(this)
            .append("responseId: ", getResponseId())
			.append("answer:", getAnswer())
			.append("attempt time: ", getAttemptTime())
			.toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof QaUsrResp) ) return false;
        QaUsrResp castOther = (QaUsrResp) other;
        return new EqualsBuilder()
            .append(this.getResponseId(), castOther.getResponseId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getResponseId())
            .toHashCode();
    }

	/**
	 * @return Returns the answer.
	 */
	public String getAnswer() {
		return answer;
	}
	/**
	 * @param answer The answer to set.
	 */
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	/**
	 * @return Returns the attemptTime.
	 */
	public Date getAttemptTime() {
		return attemptTime;
	}
	/**
	 * @param attemptTime The attemptTime to set.
	 */
	public void setAttemptTime(Date attemptTime) {
		this.attemptTime = attemptTime;
	}
	/**
	 * @return Returns the qaQueContent.
	 */
	public QaQueContent getQaQueContent() {
		return qaQueContent;
	}
	/**
	 * @param qaQueContent The qaQueContent to set.
	 */
	public void setQaQueContent(QaQueContent qaQueContent) {
		this.qaQueContent = qaQueContent;
	}
	/**
	 * @return Returns the qaQueUsr.
	 */
	public QaQueUsr getQaQueUser() {
		return qaQueUser;
	}
	/**
	 * @param qaQueUsr The qaQueUsr to set.
	 */
	public void setQaQueUser(QaQueUsr qaQueUser) {
		this.qaQueUser = qaQueUser;
	}
	/**
	 * @return Returns the responseId.
	 */
	public Long getResponseId() {
		return responseId;
	}
	/**
	 * @param responseId The responseId to set.
	 */
	public void setResponseId(Long responseId) {
		this.responseId = responseId;
	}
	
	
	/**
     * @param responses
     * @return
     */
    public boolean doesRespExistIn(List responses)
    {
        for(Iterator i = responses.iterator();i.hasNext();)
        {
            QaUsrResp resp = (QaUsrResp)i.next();
            if((resp.getAnswer().trim()).equals(this.getAnswer().trim())
                    &&resp.getQaQueUser().getQueUsrId()==this.getQaQueUser().getQueUsrId())
            return true;
        }
        return false;
    }
    
    
    /**
     * @param responses
     */
    public void updateResponseBy(List responses)
    {
        for(Iterator i = responses.iterator();i.hasNext();)
        {
            QaUsrResp resp = (QaUsrResp)i.next();
            if(resp.getQaQueUser().getQueUsrId()==this.getQaQueUser().getQueUsrId())
//                   && resp.getQaAnsContent().getDisplayOrder()==this.getQaAnsContent().getDisplayOrder())
                this.updateResponse(resp);

        }
    }

    /**
     * The response is not valid if it doesn't have a reference to question 
     * object and question user object.
     * @param resp the response to be validated
     * @return the validation result.
     */
    public boolean isResponseValid()
    {
		return this.getQaQueUser()!=null && this.getQaQueContent()!=null;
			 //  &&this.getQaAnsContent()!=null;
    }
    
    
    /**
     * Update current object according to the new response object.
     * @param resp
     */
    public void updateResponse(QaUsrResp resp)
    {
        if(!resp.isResponseValid())
            throw new IllegalArgumentException("Invalid response for update ");
        
        this.setAnswer(resp.getAnswer());
        this.setAttemptTime(resp.getAttemptTime());
        this.setQaQueContent(resp.getQaQueContent());

        this.setQaQueUser(resp.getQaQueUser());
        
    }
    
    
    /**
     * Validate whether the current response is correspondent to an author 
     * defined answer.
     * 
     * @return boolean value
     */
    public boolean isPredefinedResponse()
    {
    	//may need to add more logic here
        return false;
    }
    
    
    public int compareTo(Object o)
    {
        QaUsrResp response = (QaUsrResp) o;

        if (responseId == null)
            return -1;
        if (response.responseId == null)
            return 1;

        return (int) (responseId.longValue() - response.responseId.longValue());
    }

    
    
	/**
	 * @return Returns the qaQueContentId.
	 */
	public Long getQaQueContentId() {
		return qaQueContentId;
	}
	/**
	 * @param qaQueContentId The qaQueContentId to set.
	 */
	public void setQaQueContentId(Long qaQueContentId) {
		this.qaQueContentId = qaQueContentId;
	}
	/**
	 * @return Returns the queUsrId.
	 */
	public Long getQueUsrId() {
		return queUsrId;
	}
	/**
	 * @param queUsrId The queUsrId to set.
	 */
	public void setQueUsrId(Long queUsrId) {
		this.queUsrId = queUsrId;
	}
	/**
	 * @return Returns the hidden.
	 */
	public boolean isHidden() {
		return hidden;
	}
	/**
	 * @param hidden The hidden to set.
	 */
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	
	/**
	 * @return Returns the timezone.
	 */
	public String getTimezone() {
		return timezone;
	}
	/**
	 * @param timezone The timezone to set.
	 */
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
}
