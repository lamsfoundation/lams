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
package org.lamsfoundation.lams.admin.web.form;

/**
 *
 * Form for theme management
 *
 * @author lfoxton
 *
 *
 */
public class ThemeForm {

    private static final long serialVersionUID = -3127221000563399156L;

    public ThemeForm() {
    }

    private Long id;
    private String name;
    private String description;
    private String imageDirectory;
    private Boolean currentDefaultTheme;
    private String type;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String getImageDirectory() {
	return imageDirectory;
    }

    public void setImageDirectory(String imageDirectory) {
	this.imageDirectory = imageDirectory;
    }

    public Boolean getCurrentDefaultTheme() {
	return currentDefaultTheme;
    }

    public void setCurrentDefaultTheme(Boolean currentDefaultTheme) {
	this.currentDefaultTheme = currentDefaultTheme;
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

    public void clear() {
	this.id = null;
	this.name = null;
	this.description = null;
	this.imageDirectory = null;
	this.currentDefaultTheme = null;
	this.type = null;
    }
}
