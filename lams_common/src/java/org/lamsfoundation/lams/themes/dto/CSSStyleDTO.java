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
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.themes.CSSProperty;
import org.lamsfoundation.lams.themes.CSSStyle;
import org.lamsfoundation.lams.util.wddx.WDDXProcessor;

/**
 * Models the Theme structure that is used to communicate with Flash.
 * Includes all the information from Theme down to each individual property.
 * Based on the native Flash structure for Style objects.
 *
 * This class extends Hashtable to make it look like the normal Flash object
 * structure in a wddx packet. This class does not implement the Map interface.
 * 
 * @author Fiona Malikoff
 */
public class CSSStyleDTO extends Hashtable {

	protected Logger log = Logger.getLogger(CSSStyleDTO.class);	
  
    /**
     * Create the DTO using the data from Flash
     * 
     * @param styleHashtable
     */
    public CSSStyleDTO(Hashtable styleHashtable) {
        // copy all the entries from the input table to the new table.
    	// don't copy any null entries.
        Iterator iter = styleHashtable.entrySet().iterator();
        while ( iter.hasNext() ) {
        	Map.Entry entry = (Map.Entry) iter.next();
        	if ( ! WDDXProcessor.isNull(entry.getValue())) {
        		this.put(entry.getKey(), entry.getValue());
        	}
        }
    }
    
    /**
     * Create the DTO from a database object.
     * 
     * @param styleObject
     * @throws ParseException
     */
    public CSSStyleDTO(CSSStyle styleObject) {
        super.put(CSSThemeDTO.TEXT_FORMAT_TAG, new Hashtable());
        Set properties = styleObject.getProperties();
        if ( properties != null && properties.size() > 0 ) {
            Iterator iter = properties.iterator();
            while (iter.hasNext()) {
                CSSProperty element = (CSSProperty) iter.next();
                add(element);
            }
        }
    }

    /**
     * Create the database object from this DTO. Can only create 
     * a whole new object as the id isn't stored in the DTO.
     * 
     * Don't call it getCSSStyle, or CSSStyle will be be written out in the 
     * WDDX packet created from the DTO!
      */
    public CSSStyle createNewCSSStyle() {
        CSSStyle style = new CSSStyle();
        if ( this.size() > 0 ) {
            Iterator iter= this.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry element = (Map.Entry) iter.next();
                String key = (String) element.getKey();
                Object value = element.getValue();
                if ( CSSThemeDTO.TEXT_FORMAT_TAG.equals(key) && Map.class.isInstance(value) ) {
                    processTF((Map)value, style);
                } else {
                    CSSProperty property = new CSSProperty(key, value, null);
                    style.addProperty(property);
                }
            }
        }
        return style;
    }

    /** Flatten the _tf entries, marking them using the styleSubset field */
    private void processTF(Map tfMap, CSSStyle style) {
        Iterator iter = tfMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry element = (Map.Entry) iter.next();
            String key = (String) element.getKey();
            Object value = element.getValue();
            CSSProperty property = new CSSProperty(key, value, CSSThemeDTO.TEXT_FORMAT_TAG);
            style.addProperty(property);
        }
    }

    /** Can't do a putAll on this special class */
    public void putAll(Map m) { 
        return;
    }
    
    /** Can't put any arbitrary object in this special class */
    public Object put(Object key, Object value) {
        return null;
    }

    /** 
     * Add a database property object to this style object, doing the necessary conversions.
     * 
     * @param property
     * @throws NumberFormatException
     * @throws ParseException
     */
    public void add(CSSProperty property)  {
        try {
	        if ( CSSThemeDTO.TEXT_FORMAT_TAG.equals(property.getStyleSubset()) ) {
	            Hashtable tf = (Hashtable) this.get(CSSThemeDTO.TEXT_FORMAT_TAG);
	            tf.put(property.getName(), property.getValueAsObject() );
	        } else {
	            super.put(property.getName(), property.getValueAsObject() );
	        }
        } catch ( NumberFormatException e ) {
            log.error("NumberFormatException thrown processing property "+property+". Skipping this property",e);
        } catch ( ParseException e ) {
            log.error("ParseException thrown processing property "+property+". Skipping this property",e);
        }
    }
    
    public String toString() {
        StringBuffer buf = new StringBuffer(1000);
        buf.append("[CSSStyleDTO: ");
        Iterator iter = this.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry element = (Map.Entry) iter.next();
            buf.append(element.getKey()+"="+element.getValue());
        }
        buf.append("]");
        return buf.toString();
    }

}
