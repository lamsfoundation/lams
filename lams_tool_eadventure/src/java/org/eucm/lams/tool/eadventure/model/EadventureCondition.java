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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * Eadventure
 * 
 * @author Angel del Blanco
 *
 * @hibernate.class table="tl_eueadv10_condition"
 *
 */
public class EadventureCondition implements Cloneable {

    private static final Logger log = Logger.getLogger(EadventureCondition.class);

    private Long uid;
    //unique name
    private String name;
    private int sequenceId;
    private Long eadventure_uid;
    //taskList Items
    private Set eadListExpression;

    /**
     * Default contruction method.
     */
    public EadventureCondition() {
	eadListExpression = new HashSet();
    }

    //  **********************************************************
    //		Function method for TaskList
    //  **********************************************************

    /**
     * {@Override}
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	result = prime * result + sequenceId;
	result = prime * result + ((eadListExpression == null) ? 0 : eadListExpression.hashCode());
	result = prime * result + ((uid == null) ? 0 : uid.hashCode());
	result = prime * result + ((eadventure_uid == null) ? 0 : eadventure_uid.hashCode());
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
	final EadventureCondition other = (EadventureCondition) obj;
	if (name == null) {
	    if (other.name != null) {
		return false;
	    }
	} else if (!name.equals(other.name)) {
	    return false;
	}
	if (sequenceId != other.sequenceId) {
	    return false;
	}
	if (eadListExpression == null) {
	    if (other.eadListExpression != null) {
		return false;
	    }
	} else if (!eadListExpression.equals(other.eadListExpression)) {
	    return false;
	}
	if (uid == null) {
	    if (other.uid != null) {
		return false;
	    }
	} else if (!uid.equals(other.uid)) {
	    return false;
	}
	if (eadventure_uid == null) {
	    if (other.eadventure_uid != null) {
		return false;
	    }
	} else if (!eadventure_uid.equals(other.eadventure_uid)) {
	    return false;
	}
	return true;
    }

    /**
     * {@Override}
     */
    @Override
    public Object clone() {

	EadventureCondition condition = null;
	try {
	    condition = (EadventureCondition) super.clone();
	    condition.setUid(null);
	    if (eadListExpression != null) {
		Iterator iter = eadListExpression.iterator();
		Set set = new HashSet();
		while (iter.hasNext()) {
		    EadventureExpression expr = (EadventureExpression) iter.next();
		    EadventureExpression newExpr = (EadventureExpression) expr.clone();
		    newExpr.setCondition(condition);

		    set.add(newExpr);
		}
		condition.eadListExpression = set;
	    }

	} catch (CloneNotSupportedException e) {
	    log.error("When clone " + EadventureCondition.class + " failed");
	}

	return condition;
    }

    //**********************************************************
    // Get/set methods
    //**********************************************************

    /**
     * Returns <code>EadventureCondition</code> id.
     * 
     * @return EadventureCondition id
     * 
     * @hibernate.id column="uid" generator-class="native"
     */
    public Long getUid() {
	return uid;
    }

    /**
     * Sets <code>EadventureCondition</code> id.
     * 
     * @param uid
     *            EadventureCondition id
     */
    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     * Returns condition's name.
     * 
     * @return condition's name.
     *
     * @hibernate.property
     * 		    column="name"
     *
     */
    public String getName() {
	return name;
    }

    /**
     * Sets the condition's name. Should be unique for the parent EadventureCondition.
     * 
     * @param title
     *            condition's name
     */
    public void setName(String name) {
	this.name = name;
    }

    /**
     * Return set of EadventureExpression
     * 
     * @return set of EadventureCondition
     * 
     * @hibernate.set lazy="true"
     *                inverse="false"
     *                cascade="none"
     *                order-by = "sequence_id"
     * @hibernate.collection-key column="condition_uid"
     * @hibernate.collection-one-to-many class="org.eucm.lams.tool.eadventure.model.EadventureExpression"
     */
    public Set getEadListExpression() {
	return eadListExpression;
    }

    public void setEadListExpression(Set eadListExpression) {
	this.eadListExpression = eadListExpression;
    }

    /**
     * Returns condition's sequence number. Order is very important for
     * conditions as the conditions will be tested in the order shown on the
     * screen.
     * 
     * @return condition's sequence number
     * 
     * @hibernate.property column="sequence_id"
     */
    public int getSequenceId() {
	return sequenceId;
    }

    /**
     * Sets condition's sequence number. Order is very important for
     * conditions as the conditions will be tested in the order shown on the
     * screen.
     *
     * @param sequenceId
     *            condition's sequence number
     */
    public void setSequenceId(int sequenceId) {
	this.sequenceId = sequenceId;
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