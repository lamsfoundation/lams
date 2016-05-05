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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */
package org.eucm.lams.tool.eadventure.model;

import org.apache.log4j.Logger;

/**
 * Eadventure
 * 
 * @author Angel del Blanco
 *
 * @hibernate.class table="tl_eueadv10_param"
 *
 */
public class EadventureParam implements Cloneable {
    private static final Logger log = Logger.getLogger(EadventureParam.class);

    private Long uid;
    private String name;
    private String type;
    private boolean input;
    private Long eadventure_uid;

    public EadventureParam() {

    }

    public EadventureParam(String name, String type, boolean input) {
	this.name = name;
	this.type = type;
	this.input = input;
    }

    @Override
    public Object clone() {
	Object obj = null;
	try {
	    obj = super.clone();
	    ((EadventureParam) obj).setUid(null);
	    /*
	     * if (eadventure != null) {
	     * ((EadventureParam)obj).setEadventure((Eadventure)eadventure.clone());
	     * 
	     * }
	     */
	} catch (CloneNotSupportedException e) {
	    log.error("When clone " + EadventureParam.class + " failed");
	}

	return obj;
    }

    /**
     * @hibernate.id generator-class="native" type="java.lang.Long" column="uid"
     * @return Returns the log Uid.
     */
    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     * @hibernate.property column="name"
     * @return
     */
    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    /**
     * @hibernate.property column="type"
     * @return
     */
    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

    /**
     * @hibernate.property column="input"
     * @return
     */
    public boolean isInput() {
	return input;
    }

    public void setInput(boolean input) {
	this.input = input;
    }

    /**
     * @hibernate.property column="eadventure_uid"
     * @return
     */
    public Long getEadventure_uid() {
	return eadventure_uid;
    }

    public void setEadventure_uid(Long eadventure_uid) {
	this.eadventure_uid = eadventure_uid;
    }

}
