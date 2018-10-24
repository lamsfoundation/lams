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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.tool.commonCartridge.model;

import org.apache.log4j.Logger;

/**
 *
 * @author Andrey Balan
 */
public class CommonCartridgeItemInstruction implements Cloneable {
    private static final Logger log = Logger.getLogger(CommonCartridgeItemInstruction.class);

    private Long uid;
    private int sequenceId;
    private String description;

    @Override
    public Object clone() {
	Object obj = null;
	try {
	    obj = super.clone();
	    ((CommonCartridgeItemInstruction) obj).setUid(null);
	} catch (CloneNotSupportedException e) {
	    log.error("When clone " + CommonCartridgeItemInstruction.class + " failed");
	}

	return obj;
    }

// **********************************************************
    //		Get/Set methods
//	  **********************************************************
    /**
     *
     * @return Returns the uid.
     */
    public Long getUid() {
	return uid;
    }

    /**
     * @param uid
     *            The uid to set.
     */
    public void setUid(Long userID) {
	this.uid = userID;
    }

    /**
     *
     * @return
     */
    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    /**
     *
     * @return
     */
    public int getSequenceId() {
	return sequenceId;
    }

    public void setSequenceId(int sequenceId) {
	this.sequenceId = sequenceId;
    }

}
