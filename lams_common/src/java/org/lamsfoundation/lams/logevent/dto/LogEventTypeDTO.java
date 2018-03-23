/***************************************************************************
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
 * ***********************************************************************/

package org.lamsfoundation.lams.logevent.dto;

import org.lamsfoundation.lams.logevent.LogEventType;

/**
 * Contains the user text descriptions as well as the db fields 
 */
public class LogEventTypeDTO {
    private Integer id;
    private String description;
    private String areaCode;
    private String areaDescription;
    
    public LogEventTypeDTO(LogEventType dbObject, String i18NTypeDescription, String i18NAreaDescription) {
	id = dbObject.getId();
	description = i18NTypeDescription;
	areaCode = dbObject.getArea();
	areaDescription = i18NAreaDescription;
    }
    public Integer getId() {
	return id;
    }
    public void setId(Integer id) {
	this.id = id;
    }
    public String getAreaCode() {
	return areaCode;
    }
    public void setAreaCode(String areaCode) {
	this.areaCode = areaCode;
    }
    public String getDescription() {
	return description;
    }
    public void setDescription(String description) {
	this.description = description;
    }
    public String getAreaDescription() {
	return areaDescription;
    }
    public void setAreaDescription(String areaDescription) {
	this.areaDescription = areaDescription;
    }
    
    
}
