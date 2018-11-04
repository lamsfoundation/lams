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

package org.lamsfoundation.lams.themes;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author lfoxton
 */
@Entity
@Table(name = "lams_theme")
public class Theme {

    @Id
    @Column(name = "theme_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long themeId;

    @Column
    private String name;

    @Column
    private String description;

    @Column(name = "image_directory")
    private String imageDirectory;

    @Transient
    private Boolean currentDefaultTheme;

    @Transient
    private Boolean notEditable;

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