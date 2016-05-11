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


package org.eucm.lams.tool.eadventure.model;

import org.apache.log4j.Logger;

/**
 * Eadventure
 * 
 * @author Angel del Blanco
 *
 * @hibernate.class table="tl_eueadv10_var"
 *
 */
public class EadventureVars implements Cloneable {

    private static final Logger log = Logger.getLogger(EadventureVars.class);

    private Long uid;
    private String name;
    private String type;
    private String value;
    //TODO necesito tener el visitLog entero o solo el uid??¡?¿??
    private EadventureItemVisitLog visitLog;

    public EadventureVars() {

    }

    /**
     * {@Override}
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	result = prime * result + ((type == null) ? 0 : type.hashCode());
	result = prime * result + ((value == null) ? 0 : value.hashCode());
	result = prime * result + ((uid == null) ? 0 : uid.hashCode());
	result = prime * result + ((visitLog == null) ? 0 : visitLog.hashCode());
	return result;
    }

    /**
     * {@Override}
     */
    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	final EadventureVars other = (EadventureVars) obj;
	if (name == null) {
	    if (other.name != null) {
		return false;
	    }
	} else if (!name.equals(other.name)) {
	    return false;
	}

	if (type == null) {
	    if (other.type != null) {
		return false;
	    }
	} else if (!type.equals(other.type)) {
	    return false;
	}

	if (value == null) {
	    if (other.value != null) {
		return false;
	    }
	} else if (!value.equals(other.value)) {
	    return false;
	}

	if (visitLog == null) {
	    if (other.visitLog != null) {
		return false;
	    }
	} else if (!visitLog.equals(other.visitLog)) {
	    return false;
	}

	if (uid == null) {
	    if (other.uid != null) {
		return false;
	    }
	} else if (!uid.equals(other.uid)) {
	    return false;
	}

	return true;
    }

    /**
     * {@Override}
     */
    @Override
    public Object clone() {

	EadventureVars vars = null;
	try {
	    vars = (EadventureVars) super.clone();
	    vars.setUid(null);
	    vars.setVisitLog(null);

	} catch (CloneNotSupportedException e) {
	    log.error("When clone " + EadventureVars.class + " failed");
	}

	return vars;
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
     * @hibernate.property column="value"
     *                     type="text"
     * @return
     */
    public String getValue() {
	return value;
    }

    public void setValue(String value) {
	this.value = value;
    }

    /**
     * 
     * @hibernate.many-to-one column="visit_log_uid"
     *                        cascade="save-update"
     * @return
     */
    public EadventureItemVisitLog getVisitLog() {
	return visitLog;
    }

    public void setVisitLog(EadventureItemVisitLog visitLog) {
	this.visitLog = visitLog;
    }

}
