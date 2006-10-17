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
/* $$Id$$ */
package org.lamsfoundation.lams.config;

import java.io.Serializable;


/** 
 * 
 * @hibernate.class table="lams_configuration"
 */
public class ConfigurationItem implements Serializable {

    /** identifier field */
    private String key;

    /** persistent field */
    private String value;

    /** default constructor */
    public ConfigurationItem() {
    }
    
    /** full constructor */
    public ConfigurationItem(String key, 
                String value) 
    {
    	this.key = key;
    	this.value = value;
    }

    /** 
     * @hibernate.id type="java.lang.String" length="30"
     *             	 column="config_key"      
     */
    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    /** 
     * @hibernate.property column="config_value" length="255"
     *            		   not-null="false"    
     */
    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
