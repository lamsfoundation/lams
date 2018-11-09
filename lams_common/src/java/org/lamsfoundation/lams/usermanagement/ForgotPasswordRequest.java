package org.lamsfoundation.lams.usermanagement;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author lfoxton
 */

@Entity
@Table(name = "lams_password_request")
public class ForgotPasswordRequest implements Serializable {
    private static final long serialVersionUID = -7140792259303426167L;

    @Id
    @Column(name = "request_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer requestId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "request_key")
    private String requestKey;

    @Column(name = "request_date")
    private Date requestDate;

    public ForgotPasswordRequest() {
    }

    public Integer getRequestId() {
	return requestId;
    }

    public void setRequestId(Integer requestId) {
	this.requestId = requestId;
    }

    public Integer getUserId() {
	return userId;
    }

    public void setUserId(Integer userId) {
	this.userId = userId;
    }

    public String getRequestKey() {
	return requestKey;
    }

    public void setRequestKey(String requestKey) {
	this.requestKey = requestKey;
    }

    public Date getRequestDate() {
	return requestDate;
    }

    public void setRequestDate(Date requestDate) {
	this.requestDate = requestDate;
    }
}