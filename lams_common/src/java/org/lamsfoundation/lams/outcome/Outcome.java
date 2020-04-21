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
@Table(name = "lams_outcome")
public class Outcome implements Serializable {

    private static final long serialVersionUID = -7175245687448269571L;

    @Id
    @Column(name = "outcome_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long outcomeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scale_id")
    private OutcomeScale scale;

    @Column
    private String name;

    @Column
    private String code;

    @Column
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "create_by")
    private User createBy;

    @Column(name = "create_date_time")
    private Date createDateTime;

    public Long getOutcomeId() {
	return outcomeId;
    }

    public void setOutcomeId(Long outcomeId) {
	this.outcomeId = outcomeId;
    }

    public OutcomeScale getScale() {
	return scale;
    }

    public void setScale(OutcomeScale scale) {
	this.scale = scale;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getCode() {
	return code;
    }

    public void setCode(String code) {
	this.code = code;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
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