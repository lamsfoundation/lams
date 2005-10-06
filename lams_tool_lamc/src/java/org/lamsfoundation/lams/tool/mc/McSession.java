package org.lamsfoundation.lams.tool.mc;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class McSession implements Serializable {

    /** identifier field */
    private Long uid;

    /** persistent field */
    private Long mcSessionId;

    /** nullable persistent field */
    private Date sessionStartDate;

    /** nullable persistent field */
    private Date sessionEndDate;

    /** nullable persistent field */
    private String sessionStatus;

    /** nullable persistent field */
    private Long mcContentId;

    /** nullable persistent field */
    private org.lamsfoundation.lams.tool.mc.McContent mcContent;

    /** persistent field */
    private Set mcQueUsers;

    /** full constructor */
    public McSession(Long mcSessionId, Date sessionStartDate, Date sessionEndDate, String sessionStatus, org.lamsfoundation.lams.tool.mc.McContent mcContent, Set mcQueUsers) {
        this.mcSessionId = mcSessionId;
        this.sessionStartDate = sessionStartDate;
        this.sessionEndDate = sessionEndDate;
        this.sessionStatus = sessionStatus;
        this.mcContent = mcContent;
        this.mcQueUsers = mcQueUsers;
    }
    
    
    /** default constructor */
    public McSession() {
    }

    /** minimal constructor */
    public McSession(Long mcSessionId, Set mcQueUsers) {
        this.mcSessionId = mcSessionId;
        this.mcQueUsers = mcQueUsers;
    }

    public Long getUid() {
        return this.uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getMcSessionId() {
        return this.mcSessionId;
    }

    public void setMcSessionId(Long mcSessionId) {
        this.mcSessionId = mcSessionId;
    }

    
    public Long getMcContentId() {
        return this.mcContentId;
    }

    public void setMcContentId(Long mcContentId) {
        this.mcContentId = mcContentId;
    }

    public org.lamsfoundation.lams.tool.mc.McContent getMcContent() {
        return this.mcContent;
    }

    public void setMcContent(org.lamsfoundation.lams.tool.mc.McContent mcContent) {
        this.mcContent = mcContent;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("uid", getUid())
            .toString();
    }

	/**
	 * @return Returns the sessionEndDate.
	 */
	public Date getSessionEndDate() {
		return sessionEndDate;
	}
	/**
	 * @param sessionEndDate The sessionEndDate to set.
	 */
	public void setSessionEndDate(Date sessionEndDate) {
		this.sessionEndDate = sessionEndDate;
	}
	/**
	 * @return Returns the sessionStartDate.
	 */
	public Date getSessionStartDate() {
		return sessionStartDate;
	}
	/**
	 * @param sessionStartDate The sessionStartDate to set.
	 */
	public void setSessionStartDate(Date sessionStartDate) {
		this.sessionStartDate = sessionStartDate;
	}
	/**
	 * @return Returns the sessionStatus.
	 */
	public String getSessionStatus() {
		return sessionStatus;
	}
	/**
	 * @param sessionStatus The sessionStatus to set.
	 */
	public void setSessionStatus(String sessionStatus) {
		this.sessionStatus = sessionStatus;
	}
	/**
	 * @return Returns the mcQueUsers.
	 */
	
	public Set getMcQueUsers() {
		if (this.mcQueUsers == null)
			setMcQueUsers(new HashSet());
	    return this.mcQueUsers;
	}
	/**
	 * @param mcQueUsers The mcQueUsers to set.
	 */
	public void setMcQueUsers(Set mcQueUsers) {
		this.mcQueUsers = mcQueUsers;
	}
}
