package org.lamsfoundation.lams.integration;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.usermanagement.User;

@Entity
@Table(name = "lams_ext_user_userid_map")
public class ExtUserUseridMap implements Serializable {

    private static final long serialVersionUID = 1755818193730728064L;

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sid;

    @Column(name = "external_username")
    private String extUsername;

    @Column(name = "lti_adv_username")
    private String ltiAdvUsername;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ext_server_org_map_id")
    private ExtServer extServer;

    /** id of gradebook item in LTI tool consumer */
    @Column(name = "tc_gradebook_id")
    private String tcGradebookId;

    public ExtUserUseridMap() {
    }

    public Integer getSid() {
	return this.sid;
    }

    public void setSid(Integer sid) {
	this.sid = sid;
    }

    public String getExtUsername() {
	return this.extUsername;
    }

    public void setExtUsername(String extUsername) {
	this.extUsername = extUsername;
    }

    public String getLtiAdvUsername() {
	return ltiAdvUsername;
    }

    public void setLtiAdvUsername(String ltiAdvUsername) {
	this.ltiAdvUsername = ltiAdvUsername;
    }

    public User getUser() {
	return this.user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    public ExtServer getExtServer() {
	return this.extServer;
    }

    public void setExtServer(ExtServer extServer) {
	this.extServer = extServer;
    }

    public String getTcGradebookId() {
	return this.tcGradebookId;
    }

    public void setTcGradebookId(String tcGradebookId) {
	this.tcGradebookId = tcGradebookId;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("sid", getSid()).append("extUsername", getExtUsername()).toString();
    }
}