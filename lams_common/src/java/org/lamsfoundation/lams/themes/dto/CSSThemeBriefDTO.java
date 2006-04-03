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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.themes.dto;

import org.lamsfoundation.lams.learningdesign.dto.BaseDTO;
import org.lamsfoundation.lams.themes.CSSThemeVisualElement;

/**
 * Contains only the basic details for a theme - name, id, etc. Does not
 * contain any of the style details. Designed for returning the list
 * of all themes to Flash.
 * 
 * @author Fiona Malikoff
 */
public class CSSThemeBriefDTO extends BaseDTO {

	public final static String ID_TAG = "id";
	public final static String NAME_TAG = "name"; 
	public final static String DESCRIPTION_TAG = "description";

	/* The variables must have the same names as the tags above, or the WDDX packet will have the 
	 * wrong tag names when sent to Flash. We can model the visual elements as CSSVisualElementDTO 
	 * any arbitrary Java class to a hashtable. There is no point modelling the style objects
	 * as anything but a hashtable, as all they contain are properties.
	 */
	private Long id;
	private String name;
	private String description;
    
    /**
     * Create the DTO from a database object.
     */
    public CSSThemeBriefDTO(CSSThemeVisualElement theme) {
        if ( theme != null ) {
    		this.id = theme.getId();
    		this.name = theme.getName();
    		this.description = theme.getDescription();
        }
    }
    
    public CSSThemeBriefDTO(Long id, String name, String description)
    {
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
