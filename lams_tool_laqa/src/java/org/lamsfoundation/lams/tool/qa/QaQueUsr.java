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
package org.lamsfoundation.lams.tool.qa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
/**
 * 
 * @author Ozgur Demirtas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * 
 * QaQueUsr Value Object
 * The value object that maps to our model database table: tl_laqa11_que_usr
 * The relevant hibernate mapping resides in: QaQueUsr.hbm.xml
 * 
 * Represents tool users. 
 */
public class QaQueUsr implements Serializable, Comparable, Nullable
{
	static Logger logger = Logger.getLogger(QaQueUsr.class.getName());
	
	/** identifier field */
    private Long queUsrId;
    
    /** nullable persistent field */
    private String username;
    
    /** nullable persistent field */
    private String fullname;
    
    /** persistent field */
    private QaSession qaSession;
    
    /** persistent field */
    private Long qaSessionId;
    
    /** persistent field */
    private QaQueContent qaQueContent;
    
    /** persistent field */
    private Set qaUsrResps;  
    
    public QaQueUsr()
	{
		logger.debug(logger + " " + this.getClass().getName() +  "in constructor: QaQueUsr()");
	}
    
    
    
    /** full constructor */
    public QaQueUsr(Long queUsrId,
                    String username,
                    String fullname,
                    QaQueContent qaQueContent,
					QaSession qaSession,
					Set qaUsrResps) 
                        
    {
        this.queUsrId 	= queUsrId;
        this.username 	= username;
        this.fullname 	= fullname;
        this.qaQueContent = qaQueContent;
        this.qaSession 	= qaSession; 
        this.qaUsrResps	=qaUsrResps;
    }

    /** minimal constructor */
    public QaQueUsr(QaQueContent qaQueContent,
    				QaSession qaSession,
					Set qaUsrResps) 
                    
    {
        this.qaQueContent = qaQueContent;
        this.qaSession = qaSession; 
        this.qaUsrResps	=qaUsrResps;
    }
    
    
    public QaQueUsr(Long queUsrId,
	            String username,
	            String fullname,
	            QaQueContent qaQueContent,
	            QaSession qaSession)
	{
		this.queUsrId = queUsrId;
		this.username = username;
		this.fullname = fullname;
		this.qaQueContent = qaQueContent;
		this.qaSession = qaSession;
	}
    
    

    public QaQueUsr(String username,
                    String fullname,
                    QaQueContent qaQueContent,
					QaSession qaSession,
					Set qaUsrResps) 
                        
    {
        this.username 	= username;
        this.fullname 	= fullname;
        this.qaQueContent = qaQueContent;
        this.qaSession 	= qaSession; 
        this.qaUsrResps	=qaUsrResps;
    }

    

    //implement this, double check this, what is it for?
    /**
     * Copy construtor; We copy all data except the hibernate id field.
     * @param queUsr the original survey question user object.
     * @return the survey question user object.
     */
    public QaQueUsr newInstance(QaQueUsr queUsr)
    {
    	return new QaQueUsr(queUsr.getQueUsrId(),
		                queUsr.getUsername(),
		                queUsr.getFullname(),
		                queUsr.getQaQueContent(),
		                queUsr.getQaSession(),
		                queUsr.getQaUsrResps());
    }
    
        
	/**
	 * @return Returns the fullname.
	 */
	public String getFullname() {
		return fullname;
	}
	/**
	 * @param fullName The fullName to set.
	 */
	public void setFullname(String fullname) {
		this.fullname = fullname;
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
	 * @return Returns the qaSession.
	 */
	public QaSession getQaSession() {
		return qaSession;
	}
	/**
	 * @param qaSession The qaSession to set.
	 */
	public void setQaSession(QaSession qaSession) {
		this.qaSession = qaSession;
	}
	/**
	 * @return Returns the qaUsrResps.
	 */
	public Set getQaUsrResps() 
	{
		if (this.qaUsrResps == null)
            setQaUsrResps(new TreeSet());
        return this.qaUsrResps;
	}
	/**
	 * @param qaUsrResps The qaUsrResps to set.
	 */
	public void setQaUsrResps(Set qaUsrResps) {
		this.qaUsrResps = qaUsrResps;
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
	 * @return Returns the username.
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username The username to set.
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	
	public String toString()
    {
        return new ToStringBuilder(this).append("queUsrId", getQueUsrId())
        								.append("username",getUsername())
        								.append("full name",getFullname())
                                        .toString();
    }
	
	
	public boolean equals(Object other)
    {
        if (!(other instanceof QaQueUsr))
            return false;
        QaQueUsr castOther = (QaQueUsr) other;
        return new EqualsBuilder().append(this.getQueUsrId(),castOther.getQueUsrId())
                                  //.append(this.getUserId(),castOther.getUserId())  
                                  //.append(this.getQaQueContent(),castOther.getQaQueContent())
                                  .isEquals();
    }

    public int hashCode()
    {
        return new HashCodeBuilder().append(getQueUsrId())
                                    //.append(getUserId()) 
                                    .toHashCode();
    }

	

    //---------------------------------------------------------------------
    // Convenient Service Methods
    //---------------------------------------------------------------------
    /**
     * Check up question responsed by current user against a list user responses.
     * Return <code>true</code> if 
     * @param responses
     * @return the validation result
     */
    public boolean checkUpQueUsrHas(List responses)
    {
        if (responses == null)
            throw new IllegalArgumentException("Invalid responses from "
                    + this.getFullname() + ": Can't validate null responses"
                    + "against current survey questions");

        //make defensive copy to avoid list mutation outside this class.
        ArrayList resps = new ArrayList(responses);

        for (Iterator i = resps.iterator(); i.hasNext();)
        {
            QaUsrResp resp = (QaUsrResp) i.next();
            if (doesQueUserHas(resp))
                return true;
        }
        return false;
    }

	
	
    /**
     * The helper function to validate the availability of a user response for
     * this question user.
     * The condition to return true is:<br>
     * <li>the requested response has a reference to a question user object 
     * and reference id is the same as current question user object.</li>
     * 
     * @param response the user response
     * @return the validation result
     */
    public boolean doesQueUserHas(QaUsrResp response)
    {
        if (response.getQaQueUser() == null)
            throw new IllegalArgumentException("Invalid response :"
                    + " Can't validate the availability"
                    + " of a response without the reference to a user");
        
        if(response.getQaQueUser().getQueUsrId()==null||this.getQueUsrId()==null)
            return false;
        
        if (this.getQueUsrId().equals(response.getQaQueUser().getQueUsrId()))
            return true;
        return false;
    }	
	
    /**
     * @param responses
     * @param responseSet
     */
    public void removeResponseBy(ArrayList responses)
    {
        Set responseSet = new TreeSet(this.getQaUsrResps());
        //remove responses no longer exist.
        for(Iterator i = responseSet.iterator();i.hasNext();)
        {
            QaUsrResp resp = (QaUsrResp) i.next();
            if(!resp.doesRespExistIn(responses))
                this.getQaUsrResps().remove(resp);
        }
    }

	
    /**
     * Update the user responses of this question user object against a list of
     * new user responses.
     * @param responses
     */
    public void updateQueUsr(List responses)
    {
        if (responses == null)
            throw new IllegalArgumentException("Invalid responses from "
                    + this.getFullname() + ": Can't update null responses"
                    + "against current survey questions");

        //make defensive copy to avoid list mutation outside this class.
        ArrayList resps = new ArrayList(responses);
        //clean up all the existing reponses
        removeResponseBy(resps);
        addNewResponsesBy(resps);
        updateExistingResp(resps);
    }

    /**
     * @param resps
     * @param responseList
     */
    public void addNewResponsesBy(ArrayList resps)
    {
        ArrayList responseList = new ArrayList(this.getQaUsrResps());
        //add all associated new responses into the current question user.
        for (Iterator i = resps.iterator(); i.hasNext();)
        {
            QaUsrResp resp = (QaUsrResp) i.next();
            if (!resp.doesRespExistIn(responseList)&&doesQueUserHas(resp))
                addUserResponse(resp);
        }
    }

    /**
     * @param responses
     * @param responseSet
     */
    public void updateExistingResp(ArrayList responses)
    {
        //update existing responses
        for(Iterator i = this.getQaUsrResps().iterator();i.hasNext();)
        {
            QaUsrResp resp = (QaUsrResp) i.next();
            if(resp.doesRespExistIn(responses))
                resp.updateResponseBy(responses);                
        }
    }

    /**
     * @param resp
     */
    public void addUserResponse(QaUsrResp resp)
    {
        if (resp != null && !resp.isResponseValid())
            throw new IllegalArgumentException("Invalid response for update ");

        this.getQaUsrResps().add(resp);
    }

    
    /**
     * Get a list of user response Strings that are correspondent to the 
     * authored defined candidate answers. Currently, we include the free
     * text answer to this category as well. 
     * 
     * @return the list of String user responses
     */
    public List getPredefinedResponse()
    {
        LinkedList responses = new LinkedList();
        
        for(Iterator i = this.getQaUsrResps().iterator();i.hasNext();)
        {
            QaUsrResp res = (QaUsrResp)i.next();
            if(res.isPredefinedResponse())
            {
                responses.add(res.getAnswer());
            }
        }
        return responses;
    }

    
    public String getOtherResponse()
    {
        for(Iterator i = this.getQaUsrResps().iterator();i.hasNext();)
        {
        	QaUsrResp res = (QaUsrResp)i.next();
            if(!res.isPredefinedResponse())
                return res.getAnswer();
        }
        
        return "";
    }
    
    public int compareTo(Object o)
    {
        
    	QaQueUsr qUser = (QaQueUsr) o;
    	/*
    	if (this.qaQueContent == null
                && qUser.getQaQueContent() == null)
            return String.CASE_INSENSITIVE_ORDER.compare(username,
                                                         qUser.username);
        if (this.qaQueContent == null)
            return 1;
        if (qUser.getQaQueContent() == null)
            return -1;
        */      
        return this.getQaQueContent().compareTo(qUser.getQaQueContent());
    }
    
    public boolean isNull()
    {
        return false;
    }
    
	/**
	 * @return Returns the qaSessionId.
	 */
	public Long getQaSessionId() {
		return qaSessionId;
	}
	/**
	 * @param qaSessionId The qaSessionId to set.
	 */
	public void setQaSessionId(Long qaSessionId) {
		this.qaSessionId = qaSessionId;
	}
}
