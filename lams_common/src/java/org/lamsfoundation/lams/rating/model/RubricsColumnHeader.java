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
@Table(name = "lams_rating_rubrics_column_header")
public class RubricsColumnHeader implements java.io.Serializable, Cloneable {

    private static final long serialVersionUID = 5230206250617216351L;

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(name = "rubrics_id")
    private Integer rubricsId;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column
    private String title;

    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    public Integer getRubricsId() {
	return rubricsId;
    }

    public void setRubricsId(Integer rubricsId) {
	this.rubricsId = rubricsId;
    }

    public Integer getDisplayOrder() {
	return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
	this.displayOrder = displayOrder;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }
}