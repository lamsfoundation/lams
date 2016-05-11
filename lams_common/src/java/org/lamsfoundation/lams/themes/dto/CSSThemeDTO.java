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

import java.text.ParseException;
import java.util.Hashtable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learningdesign.dto.BaseDTO;
import org.lamsfoundation.lams.themes.Theme;
import org.lamsfoundation.lams.util.wddx.WDDXProcessor;
import org.lamsfoundation.lams.util.wddx.WDDXProcessorConversionException;

/**
 * Models the Theme structure that is used to communicate with Flash. Includes
 * all the information from Theme down to each individual property. Based on the
 * native Flash structure for Style objects.
 *
 * @author Fiona Malikoff
 */
public class CSSThemeDTO extends BaseDTO {

    protected Logger log = Logger.getLogger(CSSThemeDTO.class);

    public final static String ID_TAG = "id";
    public final static String NAME_TAG = "name";
    public final static String DESCRIPTION_TAG = "description";
    public final static String BASE_STYLE_OBJECT_TAG = "baseStyleObject";
    public final static String VISUAL_ELEMENTS_TAG = "visualElements";
    public final static String TEXT_FORMAT_TAG = "_tf";
    public final static String STYLE_OBJECT_TAG = "styleObject";

    /*
     * The variables must have the same names as the tags above, or the WDDX
     * packet will have the wrong tag names when sent to Flash. We can model the
     * visual elements as CSSVisualElementDTO any arbitrary Java class to a
     * hashtable. There is no point modelling the style objects as anything but
     * a hashtable, as all they contain are properties.
     */
    private Long id;
    private String name;
    private String description;

    /**
     * Create the DTO using the data from Flash
     *
     * @throws WDDXProcessorConversionException
     */
    public CSSThemeDTO(Hashtable wddxData) throws WDDXProcessorConversionException {
	if (wddxData != null) {
	    this.id = WDDXProcessor.convertToLong(wddxData, ID_TAG);
	    this.name = WDDXProcessor.convertToString(wddxData, NAME_TAG);
	    this.description = WDDXProcessor.convertToString(wddxData, DESCRIPTION_TAG);
	}
    }

    /**
     * Create the DTO from a database object.
     *
     * @throws ParseException
     *
     */
    public CSSThemeDTO(Theme theme) {
	if (theme != null) {
	    this.id = theme.getThemeId();
	    this.name = theme.getName();
	    this.description = theme.getDescription();
	}
    }

    /**
     * Creates the database format object from this DTO. Does not look up any
     * existing entries in the database. Does not set the users field.
     *
     * Don't call it getCSSTheme, or CSSTheme will be be written out in the WDDX
     * packet created from the DTO!
     */
    public Theme createCSSThemeVisualElement() {
	Theme theme = new Theme();
	theme.setDescription(description);
	theme.setName(name);
	theme.setThemeId(id);
	return theme;
    }

    /**
     * Update an existing CSSTheme object ( probably from the database) with the
     * current values. Arguably this functionality belongs in the CSSTheme
     * object but that would confused matters as some of the field copying code
     * would be in this object, and other bits would be in the CSSTheme object.
     * Does not change the user's field.
     *
     * Changes the parameter object (currentTheme) and returns the modified
     * object.
     */
    public Theme updateCSSTheme(Theme currentTheme) {

	currentTheme.setDescription(description);
	currentTheme.setName(name);
	return currentTheme;
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

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("id", id).append("name", name).append("description", description)
		.toString();
    }

}
