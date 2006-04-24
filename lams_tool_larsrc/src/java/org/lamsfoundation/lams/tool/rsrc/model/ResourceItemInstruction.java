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
package org.lamsfoundation.lams.tool.rsrc.model;

import java.util.Set;

import org.apache.log4j.Logger;
/**
 * @hibernate.class table="tl_larsrc11_item_instruction"
 * @author Steve.Ni
 * 
 * @version $Revision$
 */
public class ResourceItemInstruction  implements Cloneable{
	private static final Logger log = Logger.getLogger(ResourceItemInstruction.class);
	
	private Long uid;
	private int sequenceId;
	private String description;

    public Object clone(){
		Object obj = null;
		try {
			obj = super.clone();
			((ResourceItemInstruction)obj).setUid(null);
		} catch (CloneNotSupportedException e) {
			log.error("When clone " + ResourceItemInstruction.class + " failed");
		}
		
		return obj;
	}
// **********************************************************
	  	//		Get/Set methods
//	  **********************************************************
		/**
		 * @hibernate.id generator-class="identity" type="java.lang.Long" column="uid"
		 * @return Returns the uid.
		 */
		public Long getUid() {
			return uid;
		}
		/**
		 * @param uid The uid to set.
		 */
		public void setUid(Long userID) {
			this.uid = userID;
		}
		/**
		 * @hibernate.property column="description"
		 * @return
		 */
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		/**
		 * @hibernate.property column="sequence_id"
		 * @return
		 */
		public int getSequenceId() {
			return sequenceId;
		}
		public void setSequenceId(int sequenceId) {
			this.sequenceId = sequenceId;
		}

}
