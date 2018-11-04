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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "lams_configuration")
public class ConfigurationItem implements Serializable {
    private static final long serialVersionUID = -2458856157870283305L;

    public static String STRING_FORMAT = "STRING";
    public static String LONG_FORMAT = "LONG";
    public static String BOOLEAN_FORMAT = "BOOLEAN";

    @Id
    @Column(name = "config_key")
    private String key;

    @Column(name = "config_value")
    private String value;

    @Column(name = "description_key")
    private String descriptionKey;

    @Column(name = "header_name")
    private String headerName;

    @Column
    private String format;

    /** defaults in db to false */
    @Column
    private Boolean required;

    public ConfigurationItem() {
    }

    public String getKey() {
	return this.key;
    }

    public void setKey(String key) {
	this.key = key;
    }

    public String getValue() {
	return this.value;
    }

    public void setValue(String value) {
	this.value = value;
    }

    public String getDescriptionKey() {
	return descriptionKey;
    }

    public void setDescriptionKey(String descriptionKey) {
	this.descriptionKey = descriptionKey;
    }

    public String getHeaderName() {
	return headerName;
    }

    public void setHeaderName(String headerName) {
	this.headerName = headerName;
    }

    public String getFormat() {
	return format;
    }

    public void setFormat(String format) {
	this.format = format;
    }

    public Boolean getRequired() {
	return required;
    }

    public void setRequired(Boolean required) {
	this.required = required;
    }
}