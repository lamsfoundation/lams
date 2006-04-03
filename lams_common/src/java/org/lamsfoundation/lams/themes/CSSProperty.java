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
package org.lamsfoundation.lams.themes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.builder.ToStringBuilder;


/** 
 *        @hibernate.class
 *         table="lams_css_property"
 *     
*/
public class CSSProperty implements Serializable {

    /** identifier field */
    private Long propertyId;
 
    /** persistent field */
    private String name;

    /** persistent field */
    private String value;

    /** persistent field */
    private byte type;

    /** persistent field */
    private String styleSubset;

    /** persistent field */
    private CSSStyle style;
    
    /** full constructor */
    public CSSProperty(Long propertyId, String name, String value, byte type, String styleSubset, CSSStyle style) {
        this.propertyId = propertyId;
        this.name = name;
        this.value = value;
        this.type = type;
        this.styleSubset = styleSubset;
        this.style = style;
    }

    /** Create a property using just the name and the value (and any style subset e.g. _tf). 
     * Generates the appropriate type. */
    public CSSProperty(String name, Object value, String styleSubset) {
        this.name = name;
        this.value = value != null ? value.toString() : null;
        this.type = getValueType(value);
        this.styleSubset = styleSubset;
    }

    /** default constructor */
    public CSSProperty() {
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.Long"
     *             column="property_id"
     *         
     */
    public Long getPropertyId() {
        return this.propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    /** 
     *            @hibernate.property
     *             column="name"
     *             length="255"
     *             not-null="true"
     *         
     */
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /** 
     *            @hibernate.property
     *             column="value"
     *             length="100"
     *             not-null="true"
     *         
     */
    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /** 
     * Should be a value in PropertyType.
     * 
     *            @hibernate.property
     *             column="type"
     *             length="4"
     *             not-null="true"
     *         
     */
    public byte getType() {
        return this.type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    /** 
     *            @hibernate.property
     *             column="style_subset"
     *             length="20"
     *             not-null="true"
     *         
     */
    public String getStyleSubset() {
        return this.styleSubset;
    }

    public void setStyleSubset(String styleSubset) {
        this.styleSubset = styleSubset;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     * 			   cascade="none"
     * 			   update="true"
     * 			   insert="true"
    *            @hibernate.column name="style_id"         
     *         
     */
    public CSSStyle getStyle() {
        return this.style;
    }

    public void setStyle(CSSStyle style) {
        this.style = style;
    }
    
    public String toString() {
        return new ToStringBuilder(this)
            .append("propertyId", getPropertyId())
            .append("name", name)
            .append("value", value)
            .append("type", type)
            .append("styleSubset",styleSubset)
            .toString();
    }
    
    /** Get the value of the property as its correct type e.g. Integer */
    public Object getValueAsObject() throws NumberFormatException, ParseException {
        if ( value != null ) {
	        switch ( getType() ) {
	        	case PropertyType.BOOLEAN:
	        	    return Boolean.valueOf(value);
	        	case PropertyType.DATE:
	        	    return getDate();
	        	case PropertyType.DOUBLE:
	        	    return Double.valueOf(value);
	        	case PropertyType.LONG:
	        	    return Long.valueOf(value);
	        	default:
	        	    return (String) value;
	        }
        } else {
            return null;
        }
    }
    
    /** Determines the object's appropriate type. Coded in order of most expected to least expected. */
    protected byte getValueType(Object value) {
        if ( value != null ) {
            if ( String.class.isInstance(value)) {
                return PropertyType.STRING;
            }
            if ( Boolean.class.isInstance(value) ) {
                return PropertyType.BOOLEAN;
            }
            if ( Integer.class.isInstance(value) || Long.class.isInstance(value) || 
                    Short.class.isInstance(value) || BigInteger.class.isInstance(value)) {
                return PropertyType.LONG;
            }
            if ( Double.class.isInstance(value) || Float.class.isInstance(value) || 
                    BigDecimal.class.isInstance(value) ) {
                return PropertyType.DOUBLE;
            }
            if ( Calendar.class.isInstance(value) || Date.class.isInstance(value)) {
                return PropertyType.DATE;
            }
        }
    	return PropertyType.STRING;
    }
        /**
    	 * Returns a Calendar representation of the value. 
    	 *
         * @throws ValueFormatException If able to convert the value to a Calendar.
         */
        private Calendar getDate() throws ParseException {
        	SimpleDateFormat df = new SimpleDateFormat();
    		Date date = df.parse(value);
    		Calendar calendar = new GregorianCalendar();
    		calendar.setTime(date);
    		return calendar;
    	}

}
