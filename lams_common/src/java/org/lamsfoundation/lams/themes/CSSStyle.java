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
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** 
 *        @hibernate.class
 *         table="lams_css_style"
 *     
*/
public class CSSStyle implements Serializable {

    /** identifier field */
    private Long styleId;

    /** persistent field */
    private CSSThemeVisualElement themeElement;

    /** persistent field */
    private Set properties;

    /** full constructor */
    public CSSStyle(Long styleId, CSSThemeVisualElement themeElement, Set properties) {
        this.styleId = styleId;
        this.themeElement = themeElement;
        this.properties = properties;
    }

    /** default constructor */
    public CSSStyle() {
    }

    /** 
     *            @hibernate.id
     *             generator-class="native"
     *             type="java.lang.Long"
     *             column="style_id"
     *         
     */
    public Long getStyleId() {
        return this.styleId;
    }

    public void setStyleId(Long styleId) {
        this.styleId = styleId;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="all"
     *            @hibernate.collection-key
     *             column="style_id"
     *            @hibernate.collection-one-to-many
     *             class="org.lamsfoundation.lams.themes.CSSProperty"
     *         
     */
    public Set getProperties() {
        return this.properties;
    }

    private void setProperties(Set properties) {
        this.properties = properties;
    }
    
    /** Clear all the current properties of this style */
    public void clearProperties() {
        if ( getProperties() != null ) {
            getProperties().clear();
        } 
    }

    /** Add a property to this style */
    public void addProperty(CSSProperty property) {
        if ( getProperties() == null ) {
            Set set = new HashSet();
            set.add(property);
            setProperties(set);
        } else {
            getProperties().add(property);
        }
        property.setStyle(this);
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     * 			   cascade="none"
     * 			   update="true"
     * 			   insert="true"
    *            @hibernate.column name="theme_ve_id"         
     *         
     */
    public CSSThemeVisualElement getThemeElement() {
        return this.themeElement;
    }

    public void setThemeElement(CSSThemeVisualElement themeElement) {
        this.themeElement = themeElement;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("styleId", getStyleId())
            .append("properties", getProperties())
            .toString();
    }

}
