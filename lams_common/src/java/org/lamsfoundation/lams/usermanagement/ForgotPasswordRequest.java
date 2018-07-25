package org.lamsfoundation.lams.usermanagement;

import java.io.Serializable;
import java.util.Date;

/**
 *
 *
 *
 *
 * @author lfoxton
 *
 */
public class ForgotPasswordRequest implements Serializable {
    /** identifier field */
    private Integer requestId;

    /** identifier field */
    private Integer userId;

    /** persistent field */
    private String requestKey;

    /** persistent field */
    private Date requestDate;

    /** Full Constructor */
    public ForgotPasswordRequest(Integer requestId, Integer userId, String requestKey, Date requestDate) {
	super();
	this.requestId = requestId;
	this.userId = userId;
	this.requestKey = requestKey;
	this.requestDate = requestDate;
    }

    /** default constructor */
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
