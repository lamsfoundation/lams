package org.lamsfoundation.lams.outcome;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.usermanagement.User;

@Entity
@Table(name = "lams_outcome_scale")
public class OutcomeScale implements Serializable {
    private static final long serialVersionUID = 216274187123917942L;

    @Id
    @Column(name = "scale_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scaleId;
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

    @OneToMany(mappedBy = "scale", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("value")
    private Set<OutcomeScaleItem> items = new LinkedHashSet<>();

    /**
     * Split comma separated values into a list
     */
    public static List<String> parseItems(String itemString) {
	return StringUtils.isBlank(itemString) ? null : Arrays.asList(itemString.split(","));
    }

    /**
     * Build a string of comma separated values
     */
    public String getItemString() {
	StringBuilder itemString = new StringBuilder();
	for (OutcomeScaleItem item : items) {
	    itemString.append(item.getName()).append(",");
	}
	return itemString.length() == 0 ? null : itemString.substring(0, itemString.length() - 1);
    }

    public Long getScaleId() {
	return scaleId;
    }

    public void setScaleId(Long scaleId) {
	this.scaleId = scaleId;
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

    public Set<OutcomeScaleItem> getItems() {
	return items;
    }

    public void setItems(Set<OutcomeScaleItem> items) {
	this.items = items;
    }
}