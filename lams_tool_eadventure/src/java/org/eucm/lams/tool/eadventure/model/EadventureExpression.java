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
 * @hibernate.class table="tl_eueadv10_condition_expression"
 *
 */
public class EadventureExpression implements Cloneable {

    private static final Logger log = Logger.getLogger(EadventureExpression.class);

    private Long uid;
    private EadventureParam firstOp;
    // The both values allows to introduce the second operand this value shows if the
    private String valueIntroduced;
    private EadventureParam varIntroduced;
    //TODO preguntar si hay problemas de eficiencia por hacer esto con objeto en vez de con id (facilita el codigo, pero cargamos objetos)
    //  private Long condition_uid;
    private EadventureCondition condition;
    private String expresionOp;
    // the op (and / or) to link with the next operation
    private String nextOp;
    private int sequenceId;

    /**
     * Default construction method.
     */
    public EadventureExpression() {

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
	result = prime * result + ((firstOp == null) ? 0 : firstOp.hashCode());
	result = prime * result + sequenceId;
	result = prime * result + ((valueIntroduced == null) ? 0 : valueIntroduced.hashCode());
	result = prime * result + ((varIntroduced == null) ? 0 : varIntroduced.hashCode());
	result = prime * result + ((expresionOp == null) ? 0 : expresionOp.hashCode());
	result = prime * result + ((uid == null) ? 0 : uid.hashCode());
	//result = prime * result + ((condition_uid == null) ? 0 : condition_uid.hashCode());
	result = prime * result + ((nextOp == null) ? 0 : nextOp.hashCode());
	//result = prime * result + ((condition == null) ? 0 : condition.getUid().hashCode());
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
	final EadventureExpression other = (EadventureExpression) obj;

	if (!firstOp.equals(other.firstOp)) {
	    return false;
	}

	if (valueIntroduced == null) {
	    if (other.valueIntroduced != null) {
		return false;
	    }
	} else if (!valueIntroduced.equals(other.valueIntroduced)) {
	    return false;
	}

	if (!varIntroduced.equals(other.varIntroduced)) {
	    return false;
	}

	if (expresionOp == null) {
	    if (other.expresionOp != null) {
		return false;
	    }
	} else if (!expresionOp.equals(other.expresionOp)) {
	    return false;
	}

	if (nextOp == null) {
	    if (other.nextOp != null) {
		return false;
	    }
	} else if (!nextOp.equals(other.nextOp)) {
	    return false;
	}

	if (sequenceId != other.sequenceId) {
	    return false;
	}

	if (uid == null) {
	    if (other.uid != null) {
		return false;
	    }
	} else if (!uid.equals(other.uid)) {
	    return false;
	}

	if (condition.getUid() == null) {
	    if (other.condition.getUid() != null) {
		return false;
	    }
	} else if (!condition.getUid().equals(condition.getUid())) {
	    return false;
	}
	return true;
    }

    /**
     * {@Override}
     */
    @Override
    public Object clone() {

	EadventureExpression expression = null;
	try {
	    expression = (EadventureExpression) super.clone();
	    expression.setUid(null);
	    expression.setCondition(null);
	    if (firstOp != null) {
		expression.firstOp = (EadventureParam) firstOp.clone();
	    }
	    if (varIntroduced != null) {
		expression.varIntroduced = (EadventureParam) varIntroduced.clone();

	    }

	} catch (CloneNotSupportedException e) {
	    log.error("When clone " + EadventureExpression.class + " failed");
	}

	return expression;

    }

    //**********************************************************
    // Get/set methods
    //**********************************************************

    /**
     * Returns <code>EadventureExpression</code> id.
     * 
     * @return EadventureExpression id
     * 
     * @hibernate.id column="uid" generator-class="native"
     */
    public Long getUid() {
	return uid;
    }

    /**
     * Sets <code>EadventureExpression</code> id.
     * 
     * @param uid
     *            EadventureExpression id
     */
    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     * @hibernate.many-to-one column="first_op"
     *                        cascade="none"
     * @return
     */
    public EadventureParam getFirstOp() {
	return firstOp;
    }

    public void setFirstOp(EadventureParam firstOp) {
	this.firstOp = firstOp;
    }

    /**
     * Returns value introduced for the expression .
     * 
     * @return condition's name.
     *
     * @hibernate.property
     * 		    column="value_introduced"
     *
     */
    public String getValueIntroduced() {
	return valueIntroduced;
    }

    public void setValueIntroduced(String valueIntroduced) {
	this.valueIntroduced = valueIntroduced;
    }

    /**
     * @hibernate.many-to-one column="var_introduced"
     *                        cascade="none"
     * @return
     */
    public EadventureParam getVarIntroduced() {
	return varIntroduced;
    }

    public void setVarIntroduced(EadventureParam varIntroduced) {
	this.varIntroduced = varIntroduced;
    }

    /**
     * Returns condition's name.
     * 
     * @return condition's name.
     *
     * @hibernate.property
     * 		    column="expresion_op"
     *
     */
    public String getExpresionOp() {
	return expresionOp;
    }

    public void setExpresionOp(String expresionOp) {
	this.expresionOp = expresionOp;
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

    public void setSequenceId(int sequenceId) {
	this.sequenceId = sequenceId;
    }

    /**
     * 
     * @hibernate.many-to-one column="condition_uid"
     *                        cascade="save-update"
     * @return
     */

    public EadventureCondition getCondition() {
	return condition;
    }

    public void setCondition(EadventureCondition condition) {
	this.condition = condition;
    }

    /**
     * Returns condition's name.
     * 
     * @return condition's name.
     *
     * @hibernate.property
     * 		    column="next_op"
     *
     */
    public String getNextOp() {
	return nextOp;
    }

    public void setNextOp(String nextOp) {
	this.nextOp = nextOp;
    }
}
