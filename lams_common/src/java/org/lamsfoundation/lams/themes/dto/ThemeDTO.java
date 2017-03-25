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

package org.lamsfoundation.lams.themes.dto;

import java.io.Serializable;

import org.lamsfoundation.lams.learningdesign.dto.BaseDTO;
import org.lamsfoundation.lams.themes.Theme;

/**
 * Contains only the basic details for a theme - name, id, etc. Does not contain any of the style details.
 *
 * @author Fiona Malikoff
 */
public class ThemeDTO extends BaseDTO implements Serializable {
    private Long id;
    private String name;
    private String description;

    /**
     * Create the DTO from a database object.
     */
    public ThemeDTO(Theme theme) {
	if (theme != null) {
	    this.id = theme.getThemeId();
	    this.name = theme.getName();
	    this.description = theme.getDescription();
	}
    }

    public ThemeDTO(Long id, String name, String description) {
	this.id = id;
	this.name = name;
	this.description = description;
    }

    /**
     * @return Returns the description.
     */
    public String getDescription() {
	return description;
    }

    /**
     * @return Returns the id.
     */
    public Long getId() {
	return id;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
	return name;
    }
}
