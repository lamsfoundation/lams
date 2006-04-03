/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.themes.dto;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learningdesign.dto.BaseDTO;
import org.lamsfoundation.lams.themes.CSSThemeVisualElement;
import org.lamsfoundation.lams.util.wddx.WDDXProcessor;
import org.lamsfoundation.lams.util.wddx.WDDXProcessorConversionException;

/**
 * Models the Theme structure that is used to communicate with Flash.
 * Includes all the information from Theme down to each individual property.
 * Based on the native Flash structure for Style objects.
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

	/* The variables must have the same names as the tags above, or the WDDX packet will have the 
	 * wrong tag names when sent to Flash. We can model the visual elements as CSSVisualElementDTO 
	 * any arbitrary Java class to a hashtable. There is no point modelling the style objects
	 * as anything but a hashtable, as all they contain are properties.
	 */
	private Long id;
	private String name;
	private String description;
	private CSSStyleDTO baseStyleObject;
	private List visualElements;
    
    /**
     * Create the DTO using the data from Flash
     * @throws WDDXProcessorConversionException
     */
    public CSSThemeDTO(Hashtable wddxData) throws WDDXProcessorConversionException {
        if ( wddxData != null ) {
    		this.id = WDDXProcessor.convertToLong(wddxData,ID_TAG);
    		this.name = WDDXProcessor.convertToString(wddxData,NAME_TAG);
    		this.description = WDDXProcessor.convertToString(wddxData,DESCRIPTION_TAG);
    		Hashtable so = (Hashtable) wddxData.get(BASE_STYLE_OBJECT_TAG);
    		if ( so != null ) {
    		    baseStyleObject = new CSSStyleDTO(so);
    		}
    		List ve = (List) wddxData.get(VISUAL_ELEMENTS_TAG);
    		if ( ve != null ) {
    		    int numElements = ve.size();
    		    visualElements = new ArrayList(numElements);
    		    Iterator iter = ve.iterator();
    		    while ( iter.hasNext() ) {
    		        visualElements.add(new CSSVisualElementDTO((Hashtable) iter.next()));
    		    }
    		}
        }
    }

    /**
     * Create the DTO from a database object. 
     * 
     * @throws ParseException
     * 
     */
    public CSSThemeDTO (CSSThemeVisualElement theme){
        if ( theme != null ) {
            this.id = theme.getId();
            this.name = theme.getName();
            this.description = theme.getDescription();
    		if ( theme.getStyle()!= null ) {
    		    this.baseStyleObject = new CSSStyleDTO(theme.getStyle());
    		}
    		if ( theme.getElements() != null ) {
    		    int numElements = theme.getElements().size();
    		    if ( numElements > 0 ) {
    		        this.visualElements = new ArrayList(numElements);
    		        Iterator iter = theme.getElements().iterator();
    		        while ( iter.hasNext() ) {
    		            CSSThemeVisualElement element = (CSSThemeVisualElement) iter.next();
    		            if ( element.isTheme() ) {
    		                log.error("Theme found within another theme. Flash packet currently doesn't support this format. "
    		                        +" Theme being skipped. "+element);
    		            } else {
    		                this.visualElements.add(new CSSVisualElementDTO(element));
    		            }
    		        }
    		    }
    		}
        }
    }
    
    /**
     * Creates the database format object from this DTO. Does not look up any 
     * existing entries in the database. Does not set the users field.
     * 
     * Don't call it getCSSTheme, or CSSTheme will be be written out in the 
     * WDDX packet created from the DTO!
     */
    public CSSThemeVisualElement createCSSThemeVisualElement() {
        CSSThemeVisualElement theme = new CSSThemeVisualElement();
        if ( baseStyleObject != null ) {
            theme.setStyle(baseStyleObject.createNewCSSStyle());
        }
        theme.setDescription(description);
        theme.setName(name);
        theme.setId(id);
        theme.setTheme(true);
        if ( visualElements != null && visualElements.size() > 0 ) {
            Iterator iter = visualElements.iterator();
            while (iter.hasNext()) {
                CSSVisualElementDTO element = (CSSVisualElementDTO) iter.next();
                theme.addElement(element.createCSSThemeVisualElement());
            }
        }
        return theme;
    }

    /**
     * Update an existing CSSTheme object ( probably from the database) with the
     * current values. Arguably this functionality belongs in the CSSTheme object but
     * that would confused matters as some of the field copying code would be in 
     * this object, and other bits would be in the CSSTheme object. Does not
     * change the user's field.
     * 
     * Changes the parameter object (currentTheme) and returns the modified object.
     */
    public CSSThemeVisualElement updateCSSTheme(CSSThemeVisualElement currentTheme) {
        if ( baseStyleObject != null ) {
            currentTheme.setStyle(baseStyleObject.createNewCSSStyle());
        } else {
            currentTheme.setStyle(null);
        }
        currentTheme.setDescription(description);
        currentTheme.setName(name);
        currentTheme.setId(id);
        currentTheme.clearElements();
        if ( visualElements != null && visualElements.size() > 0 ) {
            Iterator iter = visualElements.iterator();
            while (iter.hasNext()) {
                CSSVisualElementDTO element = (CSSVisualElementDTO) iter.next();
                currentTheme.addElement(element.createCSSThemeVisualElement());
            }
        }
        return currentTheme;
    }

    /**
     * @return Returns the baseStyleObject.
     */
    public CSSStyleDTO getBaseStyleObject() {
        return baseStyleObject;
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
    /**
     * @return Returns the visualElements.
     */
    public List getVisualElements() {
        return visualElements;
    }
    
    public String toString() {
        return new ToStringBuilder(this)
            .append("id", id)
            .append("name", name)
            .append("description", description)
            .append("baseStyleObject", baseStyleObject)
            .append("visualElements", visualElements)
            .toString();
    }

}
