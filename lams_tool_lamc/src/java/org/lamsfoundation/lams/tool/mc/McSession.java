package org.lamsfoundation.lams.tool.mc;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import org.lamsfoundation.lams.tool.mc.McContent;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class McSession implements Serializable {

    /** identifier field */
    private Long uid;

    /** persistent field */
    private Long mcSessionId;

    /** nullable persistent field */
    private Date session_start_date;

    /** nullable persistent field */
    private Date session_end_date;

    /** nullable persistent field */
    private String session_status;

    /** nullable persistent field */
    private Long mcContentId;

    /** nullable persistent field */
    private McContent mcContent;

    /** persistent field */
    private Set mcQueUsers;

    /** full constructor */
    public McSession(Long mcSessionId, Date session_start_date, Date session_end_date, String session_status, Long mcContentId, McContent mcContent, Set mcQueUsers) {
        this.mcSessionId = mcSessionId;
        this.session_start_date = session_start_date;
        this.session_end_date = session_end_date;
        this.session_status = session_status;
        this.mcContentId = mcContentId;
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

    public Date getSession_start_date() {
        return this.session_start_date;
    }

    public void setSession_start_date(Date session_start_date) {
        this.session_start_date = session_start_date;
    }

    public Date getSession_end_date() {
        return this.session_end_date;
    }

    public void setSession_end_date(Date session_end_date) {
        this.session_end_date = session_end_date;
    }

    public String getSession_status() {
        return this.session_status;
    }

    public void setSession_status(String session_status) {
        this.session_status = session_status;
    }

    public Long getMcContentId() {
        return this.mcContentId;
    }

    public void setMcContentId(Long mcContentId) {
        this.mcContentId = mcContentId;
    }

    public McContent getMcContent() {
        return this.mcContent;
    }

    public void setMcContent(McContent mcContent) {
        this.mcContent = mcContent;
    }

    public Set getMcQueUsers() {
        return this.mcQueUsers;
    }

    public void setMcQueUsers(Set mcQueUsers) {
        this.mcQueUsers = mcQueUsers;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("uid", getUid())
            .toString();
    }

}
