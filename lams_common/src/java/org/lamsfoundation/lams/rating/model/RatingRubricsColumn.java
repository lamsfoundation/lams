package org.lamsfoundation.lams.rating.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The actual rating left by a user
 */
@Entity
@Table(name = "lams_rating_rubrics_columns")
public class RatingRubricsColumn implements java.io.Serializable, Cloneable {

    private static final long serialVersionUID = 5230206250617216351L;

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(name = "rating_criteria_group_id")
    private Integer ratingCriteriaGroupId;

    @Column(name = "rating_criteria_id")
    private Long ratingCriteriaId;

    @Column(name = "order_id")
    private Integer orderId;

    @Column
    private String name;

    public RatingRubricsColumn() {
    }

    public RatingRubricsColumn(Integer orderId, String name) {
	this.orderId = orderId;
	this.name = name;
    }

    @Override
    public RatingRubricsColumn clone() {
	return new RatingRubricsColumn(this.getOrderId(), this.getName());
    }

    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    public Integer getRatingCriteriaGroupId() {
	return ratingCriteriaGroupId;
    }

    public void setRatingCriteriaGroupId(Integer rubricsId) {
	this.ratingCriteriaGroupId = rubricsId;
    }

    public Long getRatingCriteriaId() {
	return ratingCriteriaId;
    }

    public void setRatingCriteriaId(Long ratingCriteriaId) {
	this.ratingCriteriaId = ratingCriteriaId;
    }

    public Integer getOrderId() {
	return orderId;
    }

    public void setOrderId(Integer displayOrder) {
	this.orderId = displayOrder;
    }

    public String getName() {
	return name;
    }

    public void setName(String title) {
	this.name = title;
    }
}