package org.lamsfoundation.lams.outcome;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.lamsfoundation.lams.usermanagement.User;

@Entity
@Table(name = "lams_outcome_result")
public class OutcomeResult implements Serializable {
    private static final long serialVersionUID = 1703649292232336661L;

    @Id
    @Column(name = "result_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resultId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mapping_id")
    private OutcomeMapping mapping;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private Integer value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "create_by")
    private User createBy;

    @Column(name = "create_date_time")
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