/* 
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
USA

http://www.gnu.org/licenses/gpl.txt 
*/
package org.lamsfoundation.lams.themes.dto;

import java.text.ParseException;
import java.util.Hashtable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learningdesign.dto.BaseDTO;
import org.lamsfoundation.lams.themes.CSSStyle;
import org.lamsfoundation.lams.themes.CSSThemeVisualElement;
import org.lamsfoundation.lams.util.wddx.WDDXProcessor;
import org.lamsfoundation.lams.util.wddx.WDDXProcessorConversionException;

/**
 * Models the Theme structure that is used to communicate with Flash.
 * Includes all the information from Theme down to each individual property.
 * Based on the native Flash structure for Style objects.
 * 
 * Can't resue the ThemeDTO as that has more fields, which would may be output
 * in the Flash packet. Also the name of the style field is different.
 * 
 * @author Fiona Malikoff
 */
public class CSSVisualElementDTO extends BaseDTO {

	protected Logger log = Logger.getLogger(CSSVisualElementDTO.class);	

	/* The variables must have the same names as the tags in CSSThemeDTO, or the WDDX packet will have the 
	 * wrong tag names when sent to Flash. We can model the style objects as CSSStyleDTO and 
	 * CSSVisualElementDTO any arbitrary Java class to a hashtable. Note: the baseStyleObject
	 */
    private String name;
    private CSSStyleDTO styleObject; // contains _tf if needed
    
    /**
     * Create the DTO using the data from Flash
     * @throws WDDXProcessorConversionException
     */
    public CSSVisualElementDTO(Hashtable wddxData) throws WDDXProcessorConversionException {
        if ( wddxData != null ) {
            this.name = WDDXProcessor.convertToString(wddxData,CSSThemeDTO.NAME_TAG);
            Hashtable so = (Hashtable) wddxData.get(CSSThemeDTO.STYLE_OBJECT_TAG);
    		if ( so != null ) {
    		    styleObject = new CSSStyleDTO(so);
    		}
        }
    }

    /**
     * Create the DTO from a database object.
     * 
     * @throws ParseException
     * 
     */
    public CSSVisualElementDTO(CSSThemeVisualElement visualElement)  {
        
        if ( visualElement != null ) {
            this.name = visualElement.getName();
            CSSStyle style = visualElement.getStyle();
            if ( style != null ) {
                styleObject = new CSSStyleDTO(style);
            }
            
        }
    }
    
    /**
     * Create the database object from this DTO. Can only create 
     * a whole new object as the id isn't stored in the DTO.
     * 
     * Don't call it getCSSVisualElement, or CSSVisualElement will be be 
     * written out in the WDDX packet created from the DTO!
     *
     */
    public CSSThemeVisualElement createCSSThemeVisualElement() {
        CSSThemeVisualElement visualElement = new CSSThemeVisualElement();
        visualElement.setName(name);
        visualElement.setStyle(styleObject.createNewCSSStyle());
        visualElement.setTheme(false);
        return visualElement;
    }

    public String toString() {
       return new ToStringBuilder(this)
            .append("name", name)
            .append("styleObject", styleObject)
            .toString();
    }
    
    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }
    /**
     * @return Returns the styleObject.
     */
    public CSSStyleDTO getStyleObject() {
        return styleObject;
    }
}
