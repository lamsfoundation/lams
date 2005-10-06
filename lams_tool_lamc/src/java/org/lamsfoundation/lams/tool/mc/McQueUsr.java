package org.lamsfoundation.lams.tool.mc;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class McQueUsr implements Serializable {

    /** identifier field */
    private Long uid;

    /** persistent field */
    private Long queUsrId;

    /** nullable persistent field */
    private String username;

    /** nullable persistent field */
    private String fullname;

    /** nullable persistent field */
    private Long mcSessionId;

    /** nullable persistent field */
    private org.lamsfoundation.lams.tool.mc.McSession mcSession;

    /** persistent field */
    private Set mcUsrAttempts;

    /** full constructor */
    public McQueUsr(Long queUsrId, String username, String fullname,  org.lamsfoundation.lams.tool.mc.McSession mcSession, Set mcUsrAttempts) {
        this.queUsrId = queUsrId;
        this.username = username;
        this.fullname = fullname;
        this.mcSession = mcSession;
        this.mcUsrAttempts = mcUsrAttempts;
    }

    /** default constructor */
    public McQueUsr() {
    }

    /** minimal constructor */
    public McQueUsr(Long queUsrId, Set mcUsrAttempts) {
        this.queUsrId = queUsrId;
        this.mcUsrAttempts = mcUsrAttempts;
    }

    public Long getUid() {
        return this.uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getQueUsrId() {
        return this.queUsrId;
    }

    public void setQueUsrId(Long queUsrId) {
        this.queUsrId = queUsrId;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return this.fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Long getMcSessionId() {
        return this.mcSessionId;
    }

    public void setMcSessionId(Long mcSessionId) {
        this.mcSessionId = mcSessionId;
    }

    public org.lamsfoundation.lams.tool.mc.McSession getMcSession() {
        return this.mcSession;
    }

    public void setMcSession(org.lamsfoundation.lams.tool.mc.McSession mcSession) {
        this.mcSession = mcSession;
    }

    public Set getMcUsrAttempts() {
    	if (this.mcUsrAttempts == null)
        	setMcUsrAttempts(new HashSet());
        return this.mcUsrAttempts;
    }
    
    
    public void setMcUsrAttempts(Set mcUsrAttempts) {
        this.mcUsrAttempts = mcUsrAttempts;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("uid", getUid())
            .toString();
    }

}
