package org.lamsfoundation.lams.outcome;

import java.io.Serializable;
import java.util.Date;

import org.lamsfoundation.lams.usermanagement.User;

public class OutcomeResult implements Serializable {
    private static final long serialVersionUID = 1703649292232336661L;

    private Long resultId;
    private OutcomeMapping mapping;
    private User user;
    private Integer value;
    private User createBy;
    private Date createDateTime;

    public Long getResultId() {
	return resultId;
    }

    public void setResultId(Long resultId) {
	this.resultId = resultId;
    }

    public OutcomeMapping getMapping() {
	return mapping;
    }

    public void setMapping(OutcomeMapping mapping) {
	this.mapping = mapping;
    }

    public User getUser() {
	return user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    public Integer getValue() {
	return value;
    }

    public void setValue(Integer value) {
	this.value = value;
    }

    public User getCreateBy() {
	return createBy;
    }

    public void setCreateBy(User createBy) {
	this.createBy = createBy;
    }

    public Date getCreateDateTime() {
	return createDateTime;
    }

    public void setCreateDateTime(Date createDateTime) {
	this.createDateTime = createDateTime;
    }
}