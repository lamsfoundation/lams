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

package org.lamsfoundation.lams.config;

import java.io.Serializable;

/**
 *
 * @hibernate.class table="lams_configuration"
 */
public class ConfigurationItem implements Serializable {

    public static String STRING_FORMAT = "STRING";
    public static String LONG_FORMAT = "LONG";
    public static String BOOLEAN_FORMAT = "BOOLEAN";

    /** identifier field */
    private String key;

    /** persistent field */
    private String value;

    /** persistent field */
    private String descriptionKey;

    /** persistent field */
    private String headerName;

    /** persistent field */
    private String format;

    /** persistent field - defaults in db to false */
    private Boolean required;

    /** default constructor */
    public ConfigurationItem() {
    }

    /** full constructor */
    public ConfigurationItem(String key, String value, String descriptionKey, String headerName, String format,
	    Boolean required) {
	this.key = key;
	this.value = value;
	this.descriptionKey = descriptionKey;
	this.headerName = headerName;
	this.format = format;
	this.required = required;
    }

    /**
     * @hibernate.id generator-class="assigned" type="java.lang.String" length="30" column="config_key"
     */
    public String getKey() {
	return this.key;
    }

    public void setKey(String key) {
	this.key = key;
    }

    /**
     * @hibernate.property column="config_value" length="255"
     *                     not-null="false"
     */
    public String getValue() {
	return this.value;
    }

    public void setValue(String value) {
	this.value = value;
    }

    /**
     * @hibernate.property column="description_key" length="255"
     */
    public String getDescriptionKey() {
	return descriptionKey;
    }

    public void setDescriptionKey(String descriptionKey) {
	this.descriptionKey = descriptionKey;
    }

    /**
     * @hibernate.property column="header_name" length="255"
     */
    public String getHeaderName() {
	return headerName;
    }

    public void setHeaderName(String headerName) {
	this.headerName = headerName;
    }

    /**
     * @hibernate.property column="format" length="30"
     */
    public String getFormat() {
	return format;
    }

    public void setFormat(String format) {
	this.format = format;
    }

    /**
     * @hibernate.property column="required" not-null="true"
     */
    public Boolean getRequired() {
	return required;
    }

    public void setRequired(Boolean required) {
	this.required = required;
    }

}
