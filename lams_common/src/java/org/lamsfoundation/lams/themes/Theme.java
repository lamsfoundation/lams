/****************************************************************
 * Copyright (C) 2009 LAMS Foundation (http://lamsfoundation.org)
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
/* $$Id$$ */
package org.lamsfoundation.lams.themes;

/**
 * @author lfoxton
 *
 *         Model for theme whether it be flash, css or other
 */
public class Theme {

    public static final int TYPE_CSS = 1;
    public static final int TYPE_FLASH = 2;

    /** identifier field */
    private Long themeId;

    /** persistent field */
    private String name;

    /** nullable persistent field */
    private String description;

    /** persistent field */
    private String imageDirectory;

    /** persistent field */
    private Integer type;

    /** non-persistent field */
    private Boolean currentDefaultTheme;

    /** non-persistent field */
    private Boolean notEditable;

    /** default constructor */
    public Theme() {
    }

    public Long getThemeId() {
	return themeId;
    }

    public void setThemeId(Long themeId) {
	this.themeId = themeId;
    }

    public String getName() {
	return this.name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getDescription() {
	return this.description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String getImageDirectory() {
	return this.imageDirectory;
    }

    public void setImageDirectory(String imageDirectory) {
	this.imageDirectory = imageDirectory;
    }

    public Integer getType() {
	return type;
    }

    public void setType(Integer type) {
	this.type = type;
    }

    public Boolean getCurrentDefaultTheme() {
	return currentDefaultTheme;
    }

    public void setCurrentDefaultTheme(Boolean currentDefaultTheme) {
	this.currentDefaultTheme = currentDefaultTheme;
    }

    public Boolean getNotEditable() {
	return notEditable;
    }

    public void setNotEditable(Boolean notEditable) {
	this.notEditable = notEditable;
    }
}
