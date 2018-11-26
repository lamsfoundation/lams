/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.contentrepository;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.exception.ValueFormatException;

@Entity
@Table(name = "lams_cr_node_version_property")
public class CrNodeVersionProperty implements IValue, Serializable {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String value;

    @Column
    private int type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nv_id")
    private org.lamsfoundation.lams.contentrepository.CrNodeVersion crNodeVersion;

    /** full constructor */
    public CrNodeVersionProperty(String name, String value, int type,
	    org.lamsfoundation.lams.contentrepository.CrNodeVersion crNodeVersion) {
	this.name = name;
	this.value = value;
	this.type = type;
	this.crNodeVersion = crNodeVersion;
    }

    /** default constructor */
    public CrNodeVersionProperty() {
    }

    public Long getId() {
	return this.id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getName() {
	return this.name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getValue() {
	return this.value;
    }

    public void setValue(String value) {
	this.value = value;
    }

    @Override
    public int getType() {
	return this.type;
    }

    /**
     * Set the type of the node. Should be a value from
     * PropertyType.
     *
     * @param type
     */
    public void setType(int type) {
	this.type = type;
    }

    public org.lamsfoundation.lams.contentrepository.CrNodeVersion getCrNodeVersion() {
	return this.crNodeVersion;
    }

    public void setCrNodeVersion(org.lamsfoundation.lams.contentrepository.CrNodeVersion crNodeVersion) {
	this.crNodeVersion = crNodeVersion;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("id", getId()).append("name", getName()).append("value", getValue())
		.append("type", getType()).toString();
    }

    @Override
    public boolean equals(Object other) {
	if ((this == other)) {
	    return true;
	}
	if (!(other instanceof CrNodeVersionProperty)) {
	    return false;
	}
	CrNodeVersionProperty castOther = (CrNodeVersionProperty) other;
	return new EqualsBuilder().append(this.getId(), castOther.getId()).append(this.getName(), castOther.getName())
		.append(this.getValue(), castOther.getValue()).append(this.getType(), castOther.getType())
		.append(this.getCrNodeVersion(), castOther.getCrNodeVersion()).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(getId()).append(getName()).append(getValue()).append(getType())
		.append(getCrNodeVersion()).toHashCode();
    }

    /* ** Implementation of IValue interface ** */

    @Transient
    protected Logger log = Logger.getLogger(CrNodeVersionProperty.class);

    /**
     * Returns a string representation of the value.
     *
     * @throws ValueFormatException
     *             If able to convert the value to a string.
     *
     * @throws IllegalStateException
     *             If calling getString() on a file and the stream cannot be read.
     *
     * @throws RepositoryException
     *             If another error occurs.
     */
    @Override
    public String getString() {
	return value;
    }

    /**
     * Returns a double representation of the value.
     *
     * @throws ValueFormatException
     *             If able to convert the value to a double.
     */
    @Override
    public double getDouble() throws ValueFormatException {
	try {
	    return Double.parseDouble(value);
	} catch (NumberFormatException nfe) {
	    throw new ValueFormatException("Unable to convert value " + value + " to double.");
	}
    }

    /**
     * Returns a Calendar representation of the value.
     *
     * @throws ValueFormatException
     *             If able to convert the value to a Calendar.
     */
    @Override
    public Calendar getDate() throws ValueFormatException {
	SimpleDateFormat df = new SimpleDateFormat();
	Date date = null;
	try {
	    date = df.parse(value);
	} catch (ParseException e) {
	    log.debug("Parse exception occured converting " + value + " to date.", e);
	}
	if (date == null) {
	    throw new ValueFormatException("Unable to convert value " + value + " to Calendar.");
	}

	Calendar calendar = new GregorianCalendar();
	calendar.setTime(date);

	return calendar;
    }

    /**
     * Returns a long representation of the value.
     *
     * @throws ValueFormatException
     *             If able to convert the value to a long.
     */
    @Override
    public long getLong() throws ValueFormatException {
	try {
	    return Long.parseLong(value);
	} catch (NumberFormatException nfe) {
	    throw new ValueFormatException("Unable to convert value " + value + " to long.");
	}
    }

    /**
     * Returns a boolean representation of the value.
     */
    @Override
    public Boolean getBoolean() throws ValueFormatException {
	return Boolean.valueOf(value);
    }

}
